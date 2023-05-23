package me.ddayo.koreanchat.client.ime.acceptable

import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen

class SignEditScreenAcceptable(private val signScreen: AbstractSignEditScreen): IIMEAcceptable {
    override val text: String
        get() = signScreen.messages[signScreen.currentRow]
    override val cursor: Int
        get() = signScreen.selectionManager!!.selectionStart

    override fun accept(str: String, rewritten: Int) {
        signScreen.selectionManager?.apply {
            delete(-rewritten)
            insert(str)
        }
    }
}