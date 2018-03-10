package me.rozkmin.generic.maps.di

import android.content.Context
import dagger.Module
import dagger.Provides
import me.rozkmin.generic.Position
import me.rozkmin.generic.data.AbstractProvider
import me.rozkmin.generic.data.MessagesProvider
import me.rozkmin.generic.maps.MapsActivity

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
@Module
class MapsModule(val activity: MapsActivity) {
    @Provides
    fun provideContext(): Context = activity

    @Provides
    fun provideMessageProvider(impl : MessagesProvider) : AbstractProvider<Pair<Position, Boolean>> = impl
}