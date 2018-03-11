package me.rozkmin.generic.createmessage

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
data class NewMessageBody(
        val message: String,
        val author: String = "Anon Anonowicz",
        val lat: Double,
        val lon: Double)