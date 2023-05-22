package me.ddayo.koreanchat.client.ime

interface IIM {
    fun isRegion(ch: Char): Boolean
    fun backspace(str: String, pos: Int): String
    fun insert(str: String, ch: Char, pos: Int, modify: Boolean): String
    fun ch(kp: Int, modifier: Int): Char
}