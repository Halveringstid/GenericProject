package me.rozkmin.generic

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem

/**
 * Created by edawhuj on 2018-03-11.
 */

class PositionCluster(val latLng: LatLng) : ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

}