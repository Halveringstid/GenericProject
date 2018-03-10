package me.rozkmin.generic.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
class LocationProviderImpl @Inject constructor() : LocationProvider {

    companion object {
        const val minTime = 700L
        const val minDistance = 2f
        val TAG: String = LocationProviderImpl::class.java.simpleName
    }

    private val locationSubject: PublishSubject<LatLng> = PublishSubject.create()

    private var locationManager: LocationManager? = null

    var myLocation: LatLng? = null

    @SuppressLint("MissingPermission")
    override fun start(context: Context) {

        Log.d(TAG, "start: ")

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, minTime, minDistance, object : LocationListener {
            override fun onLocationChanged(p0: Location?) {
                Log.d(TAG, "onLocationChanged: " + p0)

                p0?.apply {
                    myLocation = LatLng(latitude, longitude)
                    locationSubject.onNext(LatLng(latitude, longitude))
                }
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onProviderDisabled(p0: String?) {
            }
        }

        )

        val lastKnownLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocation?.run {
            myLocation = LatLng(latitude, longitude)
        }

    }

    override fun observeLocationUpdates(): Flowable<LatLng> = locationSubject.toFlowable(BackpressureStrategy.LATEST)


    override fun computeDistanceFromMe(lat: Double?, lon: Double?): Double {
        return myLocation?.let {
            lat?.let {
                lon?.let {
                    return distance(myLocation!!.latitude, myLocation!!.longitude, lat, lon)
                }
            }

        } ?: -1.0
    }

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): LatLng = myLocation
            ?: locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?.let {
                        LatLng(it.latitude, it.longitude)
                    } ?: LatLng(50.05273086249454, 19.944638420504692)

}