package me.rozkmin.generic.maps

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.rozkmin.generic.databinding.MessageDialogBinding

//import me.rozkmin.generic.databinding.MessageDialogBinding


/**
 * Created by edawhuj on 2018-03-10.
 */
class MessageDialog : DialogFragment() {
    companion object {
        private var message: String = ""

        fun newInstance(msg: String): MessageDialog {
            message = msg
            return MessageDialog()
        }
    }

    lateinit var binding: MessageDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MessageDialogBinding.inflate(inflater, container, false)
        return binding.root
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.messageDialogContent.text = message
        super.onViewCreated(view, savedInstanceState)

        binding.messageDialogButtonYes.setOnClickListener{
            this.dismiss()
        }

        binding.messageDialogButtonNo.setOnClickListener{
            this.dismiss()
        }
    }
}