package me.rozkmin.generic.location

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
interface LocationProvider {
    fun start(context: Context)
    fun observeLocationUpdates(): Flowable<LatLng>
    fun computeDistanceFromMe(lat : Double?, lon : Double?): Double
    fun getLastKnownLocation() : LatLng

    fun distance(lat_a: Double, lng_a: Double, lat_b: Double, lng_b: Double): Double {
        val earthRadius = 3958.75
        val latDiff = Math.toRadians((lat_b - lat_a).toDouble())
        val lngDiff = Math.toRadians((lng_b - lng_a).toDouble())
        val a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(Math.toRadians(lat_a.toDouble())) * Math.cos(Math.toRadians(lat_b.toDouble())) *
                Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadius * c

        val meterConversion = 1609

        return (distance * meterConversion)
    }
}


