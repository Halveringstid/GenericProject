package me.rozkmin.generic.maps.di

import dagger.Subcomponent
import me.rozkmin.generic.maps.MapsActivity

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
@Subcomponent(modules = [(MapsModule::class)])
interface MapsComponent {
    fun inject(mapsActivity: MapsActivity)
}

