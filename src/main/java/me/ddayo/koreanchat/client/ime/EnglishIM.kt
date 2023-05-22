package me.ddayo.koreanchat.client.ime

object EnglishIM: IIM {
    override fun isRegion(ch: Char) = ch in 'A'..'Z' || ch in 'a'..'z'

    override fun backspace(str: String, pos: Int) =
        if (pos == str.length) str.dropLast(1) else str.substring(0, pos - 1) + str.substring(pos)

    override fun insert(str: String, ch: Char, pos: Int, modify: Boolean): String {
        return if (pos == str.length) str + ch else str.substring(0, pos) + ch + str.substring(pos)
    }

    override fun ch(kp: Int, modifier: Int) = if (modifier == 1) kp.toChar() else (kp - 'A'.code + 'a'.code).toChar()
}