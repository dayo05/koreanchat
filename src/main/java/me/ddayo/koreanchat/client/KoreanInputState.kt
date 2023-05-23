package me.ddayo.koreanchat.client

import me.ddayo.koreanchat.client.ime.EnglishIM
import me.ddayo.koreanchat.client.ime.KoreanIM
import me.ddayo.koreanchat.client.ime.acceptable.IIMEAcceptable
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.BookEditScreen
import net.minecraft.client.gui.widget.TextFieldWidget
import org.lwjgl.glfw.GLFW
import java.util.Stack

object KoreanInputState {
    private var currentIME = 1
    private val imeList = listOf(
        EnglishIM,
        KoreanIM
    )

    private val ime get() = imeList[currentIME]
    private val acceptableStack = Stack<IIMEAcceptable>()
    private val currentInput get() = if(acceptableStack.isEmpty()) null else acceptableStack.peek()
    val isActive get() = acceptableStack.isNotEmpty()
    fun push(input: IIMEAcceptable) {
        acceptableStack.push(input)
        actionCheck = false
    }

    fun pop() {
        acceptableStack.pop()
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
                applyUpdate(backspace(), scanCode)
                mark()
            }
            else applyUpdate(EnglishIM.backspace(currentInput!!.text, currentInput!!.cursor), scanCode)
            return
        } else if (!checkModifier(keyCode)) return

        applyUpdate(ime.insert(currentInput!!.text, ime.ch(keyCode, modifier), currentInput!!.cursor, modify), scanCode)

        mark()
    }

    private fun applyUpdate(p: Pair<String, Int>, scanCode: Int) {
        val (newText, rewritten) = p
        currentInput?.accept(newText, rewritten)
    }

    private fun backspace(): Pair<String, Int> {
        if(currentInput!!.cursor == 0) return "" to 0
        return (imeList.firstOrNull { it.isRegion(currentInput!!.text[currentInput!!.cursor - 1])} ?: EnglishIM)
            .backspace(currentInput!!.text, currentInput!!.cursor)
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