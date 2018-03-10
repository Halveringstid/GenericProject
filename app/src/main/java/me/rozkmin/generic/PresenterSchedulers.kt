package me.rozkmin.generic

import io.reactivex.Scheduler

/**
 * Created by jaroslawmichalik on 20.01.2018
 */
interface PresenterSchedulers {
    fun getBackgroundScheduler(): Scheduler
    fun getMainThreadScheduler(): Scheduler
}

