package me.rozkmin.generic.data.di

import dagger.Module
import dagger.Provides
import me.rozkmin.generic.data.BaseDao
import me.rozkmin.generic.data.SeenMarkersRepo

/**
 * Created by jaroslawmichalik on 02.03.2018
 */
@Module
class DatabaseModule {
//    @Provides
//    fun provideSwotDao(dao : SwotRealmDao) : BaseDao<Swot> = dao
//
    @Provides
    fun provideSeenMarkersRepo(impl : SeenMarkersRepo) : BaseDao<String> = impl
}