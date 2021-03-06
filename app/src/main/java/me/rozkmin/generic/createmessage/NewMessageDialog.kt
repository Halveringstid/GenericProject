package me.rozkmin.generic.createmessage

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.rozkmin.generic.databinding.DialogNewMessageBinding

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
class NewMessageDialog : DialogFragment() {

    companion object {
        fun newInstance(): NewMessageDialog {
            return NewMessageDialog()
        }
    }

    var submitFunction: (String, String) -> Unit = { _, _ ->
    }

    lateinit var binding: DialogNewMessageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogNewMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clipToOutline = true

        binding.dialogNewMessageContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.dialogNewMessageCharsLeft.text = "$count / 140"
                s?.apply {
                    validate(this) {
                        binding.dialogNewMessageSubmit.setOnClickListener {
                            val author = binding.dialogNewMessageAuthor.text.toString()
                            Log.d("LOG", author)
                            submitFunction.invoke(this.toString(), author)
                        }
                    }
                }
            }
        })
    }

    private fun validate(chars: CharSequence, block: () -> Unit) {
        when {
            chars.isEmpty() -> binding.dialogNewMessageContent.error = "Cannot be empty"
            chars.length < 5 -> binding.dialogNewMessageContent.error = "Must be longer than 5 chars"
            chars.length > 140 -> binding.dialogNewMessageContent.error = "Cannot be longer than 140 chars"
            else -> block()
        }
    }


}