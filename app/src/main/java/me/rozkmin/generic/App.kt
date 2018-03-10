package me.rozkmin.generic

import android.app.Application
import me.rozkmin.generic.di.AppModule

/**
 * Created by jaroslawmichalik on 02.03.2018
 */
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        AppModule.attachApp(this)
    }
}