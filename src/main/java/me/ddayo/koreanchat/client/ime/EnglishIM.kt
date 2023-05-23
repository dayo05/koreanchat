package me.ddayo.koreanchat.client.ime

object EnglishIM: IIM {
    override fun isRegion(ch: Char) = ch in 'A'..'Z' || ch in 'a'..'z'

    override fun backspace(str: String, pos: Int) = "" to 1

    override fun insert(str: String, ch: Char, pos: Int, modify: Boolean): Pair<String, Int> {
        return ch.toString() to 0
        //return if (pos == str.length) str + ch else str.substring(0, pos) + ch + str.substring(pos)
    }

    override fun ch(kp: Int, modifier: Int) = if (modifier == 1) kp.toChar() else (kp - 'A'.code + 'a'.code).toChar()
}