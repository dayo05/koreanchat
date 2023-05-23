package me.ddayo.koreanchat.client.ime.acceptable

import net.minecraft.client.gui.widget.TextFieldWidget

class TextFieldAcceptable(private val textField: TextFieldWidget): IIMEAcceptable {
    override val text: String
        get() = textField.text
    override var cursor: Int
        get() = textField.cursor
        set(value) { textField.cursor = value }

    override fun accept(str: String, rewritten: Int) {
        textField.text = StringBuilder(textField.text).deleteRange(textField.cursor - rewritten, textField.cursor)
            .insert(textField.cursor - rewritten, str).toString()
    }
}