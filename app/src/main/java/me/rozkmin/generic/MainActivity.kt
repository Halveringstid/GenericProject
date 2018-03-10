package me.rozkmin.generic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import me.rozkmin.generic.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MapsActivity::class.java))


    }
}
