package me.ddayo.koreanchat.client.ime.acceptable

import net.minecraft.client.gui.screen.ingame.BookEditScreen

class BookEditScreenAcceptable(private val bookScreen: BookEditScreen): IIMEAcceptable {
    override val text: String
        get() = if(bookScreen.signing) bookScreen.title else bookScreen.currentPageContent
    override val cursor: Int
        get() = if(bookScreen.signing) bookScreen.bookTitleSelectionManager.selectionStart else bookScreen.currentPageSelectionManager.selectionStart

    override fun accept(str: String, rewritten: Int) {
        (if(bookScreen.signing) bookScreen.bookTitleSelectionManager else bookScreen.currentPageSelectionManager).apply {
            delete(-rewritten)
            insert(str)
        }
        if(bookScreen.signing) {
            bookScreen.dirty = true
            bookScreen.updateButtons()
        }
        else bookScreen.invalidatePageContent()
    }
}