package me.rozkmin.generic.maps

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var locationProvider: LocationProvider

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        AppModule.appComponent
                .plusMapsComponent(MapsModule(this))
                .inject(this)

        fab.setOnClickListener {
            NewMessageDialog.newInstance(LatLng(0.0, 0.0))
                    .apply {
                        submitFunction = {
                            networkService.postNewMessage(
                                    NewMessageBody(
                                            message = it,
                                            lat = locationProvider.getLastKnownLocation().latitude,
                                            lon = locationProvider.getLastKnownLocation().longitude
                                    ))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({

                                    }, {
                                        Toast.makeText(this@MapsActivity, R.string.cant_send_message, Toast.LENGTH_SHORT).show()
                                    })
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        fetchData()


    }

    private fun fetchData() {
        networkService.dupa()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //fetched data
                }, {
                    //error
                })
        //todo fetch messages and display to map
    }
}
