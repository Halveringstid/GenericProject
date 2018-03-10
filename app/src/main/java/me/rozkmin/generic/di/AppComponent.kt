package me.rozkmin.generic.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.realm.RealmConfiguration
import me.rozkmin.generic.PresenterSchedulers
import me.rozkmin.generic.data.di.DatabaseModule
import javax.inject.Singleton

/**
 * Created by jaroslawmichalik on 02.03.2018
 */
@Singleton
@Component(modules = [(DatabaseModule::class)])
interface AppComponent{

//    fun plusCreateNewComponent(module : CreateNewModule) : CreateNewComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        @BindsInstance
        fun realmConfig(realmConfig: RealmConfiguration): Builder
        @BindsInstance
        fun schedulers(presenterSchedulers: PresenterSchedulers) : Builder
        fun build(): AppComponent
    }

}