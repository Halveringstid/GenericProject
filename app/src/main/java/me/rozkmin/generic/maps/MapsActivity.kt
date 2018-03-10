package me.rozkmin.generic.maps

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_maps.*
import me.rozkmin.generic.R
import me.rozkmin.generic.createmessage.NewMessageBody
import me.rozkmin.generic.createmessage.NewMessageDialog
import me.rozkmin.generic.di.AppModule
import me.rozkmin.generic.location.LocationProvider
import me.rozkmin.generic.maps.di.MapsModule
import me.rozkmin.generic.network.NetworkService
import javax.inject.Inject
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.rozkmin.generic.Position
import me.rozkmin.generic.data.AbstractProvider
import java.util.*
import me.rozkmin.generic.data.SharedPreferencesStorage
import java.util.concurrent.TimeUnit


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var locationProvider: LocationProvider

    @Inject
    lateinit var messagesProvider: AbstractProvider<Pair<Position, Boolean>>

    private lateinit var map: GoogleMap

    companion object {
        val TAG: String = MapsActivity::class.java.simpleName
    }

    val mapOfMarkers = hashMapOf<Marker, Position>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SharedPreferencesStorage(this@MapsActivity).sharedPreferences

        setContentView(R.layout.activity_maps)

        AppModule.appComponent
                .plusMapsComponent(MapsModule(this))
                .inject(this)

        fab.setOnClickListener {
            NewMessageDialog.newInstance()
                    .apply {
                        submitFunction = {

                            val test = SharedPreferencesStorage(this@MapsActivity).getLastMessageTimestamp()
                            Log.d(TAG, (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - test.toLong())).toString())
                            if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - test.toLong()) > 2) {
                                Log.d(TAG, "postingNewMessage: " + it)
                                networkService.postNewMessage(
                                        NewMessageBody(
                                                message = it,
                                                lat = locationProvider.getLastKnownLocation().latitude,
                                                lon = locationProvider.getLastKnownLocation().longitude,
                                                author = "Anon Anonowicz"
                                        ))
                                        .applySchedulers()
                                        .subscribe({
                                            updateElementOnMap(Pair(it.data.copy(id = UUID.randomUUID().toString()), true))
                                            this.dismiss()

                                            SharedPreferencesStorage(this@MapsActivity).updateLastMessageTimestamp(System.currentTimeMillis().toString())
                                            val first = SharedPreferencesStorage(this@MapsActivity).getLastMessageTimestamp()
                                            Log.d(TAG, first)

                                        }, {
                                            Toast.makeText(this@MapsActivity, R.string.cant_send_message, Toast.LENGTH_SHORT).show()
                                        })
                            } else {
                                Toast.makeText(this@MapsActivity, "Nie spamuj, do diabła!", Toast.LENGTH_LONG).show()
                                Log.d(TAG, "Próba spamu zablokowana")
                            }
                        }
                    }
                    .show(supportFragmentManager, "")
        }

        checkPermissions {
            if (it) locationProvider.start(this)
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private inline fun AppCompatActivity.checkPermissions(crossinline block: (Boolean) -> Unit) {
        RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION)
                .doOnNext { block.invoke(it) }
                .doOnError { block.invoke(false) }
                .subscribe()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener { marker ->
            mapOfMarkers[marker]?.let {
                centerMapOn(marker.position, 15f)
                setMarkerAsSeen(marker)
                MessageDialog.newInstance().apply {
                    message = marker.title
                }.show(supportFragmentManager, "")
            }.let { true }

        }
        checkPermissions {
            if (it) {
                centerOnMe()
            }
        }
        fetchData()
    }

    private fun setMarkerAsSeen(marker: Marker?) {
        marker?.let {
            mapOfMarkers[it]?.let {
                messagesProvider.update(Pair(it, true))
                        .applySchedulers()
                        .subscribe({
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.station_green, 100, 100))) //set seen icon
                        }, {
                            Log.e(TAG, "setMarkerAsSeen: ", it)
                        })
            }

        }
    }

    private fun centerOnMe() {
        locationProvider.getLastKnownLocation().let {
            centerMapOn(it)
        }
    }

    private fun centerMapOn(latLng: LatLng) {
        centerMapOn(latLng, 11f)
    }

    private fun centerMapOn(latLng: LatLng, zoom: Float) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private var disposable : Disposable? = null

    private fun fetchData() {

        messagesProvider.getAll()
                .applySchedulers()
                .doOnError {
                    Log.e(TAG, "fetchData: ", it)
                }
                .doOnSuccess {
                    it.forEach { updateElementOnMap(it) }
                }
                .subscribe()

        disposable = locationProvider.observeLocationUpdates()
                .doOnNext { updateMyPosition(it) }
                .flatMap { messagesProvider.getAll().toFlowable().applySchedulers() }
                .doOnNext {
                    it.forEach { updateElementOnMap(it) }
                }
                .doOnError {
                    Log.d(TAG, "updateDataOnLocationUpdate: ", it)
                }
                .subscribe()
    }

    private fun updateMyPosition(it: LatLng) {

    }

    private fun updateElementOnMap(element: Pair<Position, Boolean>) {

        val icon = (if (element.second) getBitmap(R.drawable.readable) else getBitmap(R.drawable.spray_icon))
                .let {
                    Bitmap.createScaledBitmap(it, 100, 100, false)
                }

        if(mapOfMarkers.containsValue(element.first)){
            mapOfMarkers.keys.first { mapOfMarkers[it] != null }
                    .setIcon(BitmapDescriptorFactory.fromBitmap(icon))
        } else{
            val marker = map.addMarker(
                    MarkerOptions()
                            .position(LatLng(element.first.lat, element.first.lon)).title(element.first.message)
                            .icon(BitmapDescriptorFactory.fromBitmap(icon)))

            mapOfMarkers[marker] = element.first
        }

    }

    private fun Context.getBitmap(resourceId: Int) =
            (ContextCompat.getDrawable(this, resourceId) as BitmapDrawable).bitmap

    private fun resizeMapIcons(resourceId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = getBitmap(resourceId)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }


    private fun <T> Single<T>.applySchedulers() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

private fun <T> Flowable<T>.applySchedulers() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

class TooManyMessagesException : Throwable() {

}
