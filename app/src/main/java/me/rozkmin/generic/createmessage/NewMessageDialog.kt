package me.rozkmin.generic.createmessage

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import me.rozkmin.generic.R

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
class NewMessageDialog : DialogFragment(){

    companion object {
        fun newInstance(myPosition : LatLng): NewMessageDialog {
            return NewMessageDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater.inflate(R.layout.dialog_new_message, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}