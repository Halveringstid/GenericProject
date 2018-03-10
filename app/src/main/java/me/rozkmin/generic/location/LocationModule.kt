package me.rozkmin.generic.location

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jaroslawmichalik on 15.02.2018
 */
@Module
class LocationModule {
    @Provides
    @Singleton
    fun provideLocationProvider(impl: LocationProviderImpl): LocationProvider = impl
}