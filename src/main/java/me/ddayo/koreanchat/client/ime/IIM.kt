package me.ddayo.koreanchat.client.ime

interface IIM {
    fun isRegion(ch: Char): Boolean
    fun backspace(str: String, pos: Int): Pair<String, Int>
    fun insert(str: String, ch: Char, pos: Int, modify: Boolean): Pair<String, Int> // modified, rewritten chars
    fun ch(kp: Int, modifier: Int): Char
}