package me.rozkmin.generic.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date

class SharedPreferencesStorage (app: Activity) {

    companion object {
        val TAG : String = SharedPreferencesStorage::class.java.simpleName
    }

    val sharedPreferences: SharedPreferences by lazy {
        app.getSharedPreferences("storage", Context.MODE_PRIVATE)
    }


    fun getLastMessageTimestamp(): String {
        return sharedPreferences.getString("lastMessageTime","0")

    }

    fun updateLastMessageTimestamp(time: String) {
        Log.d(TAG, "updateLastMessageTime "+time)

        sharedPreferences.edit()
                .putString("lastMessageTime", time)
                .apply()
    }
}