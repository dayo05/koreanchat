package me.ddayo.koreanchat.client

import me.ddayo.koreanchat.client.ime.EnglishIM
import me.ddayo.koreanchat.client.ime.KoreanIM
import net.minecraft.client.gui.screen.ingame.BookEditScreen
import net.minecraft.client.gui.widget.TextFieldWidget
import org.lwjgl.glfw.GLFW

object KoreanInputState {
    private var currentIME = 1
    private val imeList = listOf(
        EnglishIM,
        KoreanIM
    )

    val ime get() = imeList[currentIME]
    private var currentInput: TextFieldWidget? = null
    val isActive get() = currentInput != null
    fun change(input: TextFieldWidget?) {
        currentInput = input
        actionCheck = false
    }

    private var actionCheck = false

    fun onKeyInput(keyCode: Int, scanCode: Int, modifier: Int, action: Int) {
        if (currentInput == null) return
        when (action) {
            0 -> if (actionCheck) return
            1 -> {
                if(keyCode == GLFW.GLFW_KEY_LEFT_CONTROL)
                    currentIME = if(imeList.size == currentIME + 1) 0 else currentIME + 1
                actionCheck = false
                return
            }
            2 -> actionCheck = true
        }
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if(modify) {
                updateText(backspace())
                mark()
            }
            else updateText(EnglishIM.backspace(currentInput!!.text, currentInput!!.cursor))
            return
        } else if (!checkModifier(keyCode)) return

        updateText(ime.insert(currentInput!!.text, ime.ch(keyCode, modifier), currentInput!!.cursor, modify))
        mark()
    }

    private fun backspace(): String {
        return if (currentInput!!.cursor == 0) currentInput!!.text else imeList.firstOrNull { it.isRegion(currentInput!!.text[currentInput!!.cursor - 1]) }
            ?.backspace(currentInput!!.text, currentInput!!.cursor) ?: currentInput!!.text.let { str ->
                currentInput!!.cursor.let { pos ->
                    if (pos == str.length) str.dropLast(1) else str.substring(0, pos - 1) + str.substring(pos)
                }
        }
    }

    private fun updateText(text: String) {
        val cur = currentInput!!.cursor - currentInput!!.text.length
        currentInput!!.text = text
        currentInput!!.cursor = cur + currentInput!!.text.length
    }

    private fun checkModifier(x: Int) = x in 'A'.code..'Z'.code
    fun checkCodepoint(x: Char) = imeList.any { it.isRegion(x) }

    private var modify = false
    fun unmark() {
        modify = false
    }

    fun mark() {
        modify = true
    }
}