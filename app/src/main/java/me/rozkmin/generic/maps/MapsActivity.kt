package me.rozkmin.generic.maps

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_maps.*
import me.rozkmin.generic.Position
import me.rozkmin.generic.R
import me.rozkmin.generic.createmessage.NewMessageDialog
import me.rozkmin.generic.di.AppModule
import me.rozkmin.generic.maps.di.MapsModule
import me.rozkmin.generic.network.NetworkService
import javax.inject.Inject
import me.rozkmin.generic.MainActivity
import android.content.Intent



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var networkService: NetworkService

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        AppModule.appComponent
                .plusMapsComponent(MapsModule(this))
                .inject(this)

        fab.setOnClickListener {
            NewMessageDialog.newInstance(LatLng(0.0, 0.0))
                    .show(supportFragmentManager, "")
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.setOnMarkerClickListener { marker -> (
            if (marker.title == null) {
                Log.d("MapsActivity", "Test")
                false
            } else {
                MessageDialog.newInstance(marker.title).show(supportFragmentManager, "")
                true
            }
            )
        }

        fetchData()
    }


    private fun fetchData() {
        networkService.getAllMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(this.localClassName,it.size.toString())
                    markElementsAtMap(it)
                }, {
                    //error
                })
        //todo fetch messages and display to map
    }

    private fun markElementsAtMap(it: List<Position>?) {
        if (it == null) return
        for (pos in it) {
            mMap.addMarker(MarkerOptions().position(LatLng(pos.lat,pos.lon)).title("BARDZO DLUGI STRING KTORY MA BARDZO DUZO ZNAKOW I NA PEWNO NIE ZMIESCI SIE W CHMURCE"))
        }
    }
}
