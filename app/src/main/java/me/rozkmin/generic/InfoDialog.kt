package me.rozkmin.generic

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.rozkmin.generic.databinding.GenericInfoDialogBinding

/**
 * Created by jaroslawmichalik on 11.03.2018
 */
class InfoDialog() : DialogFragment() {
    var title: Int = 0
    lateinit var binding: GenericInfoDialogBinding

    companion object {
        fun newInstance(): InfoDialog = InfoDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = GenericInfoDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.genericInfoDialogTitle.setText(title)

        binding.executePendingBindings()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
    }
}