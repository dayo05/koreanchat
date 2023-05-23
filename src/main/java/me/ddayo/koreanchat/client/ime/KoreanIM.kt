package me.ddayo.koreanchat.client.ime

import org.lwjgl.glfw.GLFW

object KoreanIM: IIM {
    override fun isRegion(ch: Char) =
        isComplatedChar(ch) ||
                isJamo(ch) ||
                isLegacyChar(ch)

    fun isComplatedChar(ch: Char) = ch.code in 0xAC00..0xD7A3
    fun isJamo(ch: Char) = ch.code in 0x1100..0x11FF ||
            ch.code in 0x3130..0x318F // KS X 1001

    fun isConsonants(ch: Char) = ch.code in 0x1100..0x1160 ||
            ch.code in 0x3131..0x314E

    fun isLegacyChar(ch: Char) = ch.code in 0xA960..0xA97F ||
            ch.code in 0xD7B0..0xD7FF

    fun hasFinal(c: Char) = (c.code - 44032) % 28 != 0

    override fun backspace(str: String, pos: Int): Pair<String, Int> {
        val c = str[pos - 1]
        if (isJamo(c))
            return "" to 1
        // [(initial) × 588 + (medial) × 28 + (final)] + 44032
        val x = c.code - 44032
        if (hasFinal(c)) {
            decompress((c.code - 44032) % 28)?.let {
                return (x - (x % 28) + it.first + 44032).toChar().toString() to 1
            } ?: run {
                return (x - (x % 28) + 44032).toChar().toString() to 1
            }
        } else {
            decompressVowel((x / 28) % 21)?.let {
                return (c.code - 28 * (x / 28 % 21 - it.first)).toChar().toString() to 1
            } ?: run {
                return initialBiasToJamo(x / 28 / 21).toString() to 1
            }
        }
    }

    override fun insert(str: String, ch: Char, pos: Int, modify: Boolean): Pair<String, Int> {
        try {
            if (modify && pos != 0) {
                val c = str[pos - 1]
                if (isConsonants(ch)) {
                    if (!isComplatedChar(c))
                        return ch.toString() to 0
                    if (!hasFinal(c))
                        return (c.code + finalBiasTransform(ch)).toChar().toString() to 1
                    val k = combineFinal((c.code - 44032) % 28, finalBiasTransform(ch))
                    if (k != -1) return (c.code - ((c.code - 44032) % 28) + k).toChar().toString() to 1
                } else {
                    if (isComplatedChar(c) && !hasFinal(c)) {
                        val k = compressVowel(((c.code - 44032) / 28) % 21, middleBiasTransform(ch))
                        return if (k == -1)
                            ch.toString() to 0
                        else (c.code - (c.code - 44032) % 588 + k * 28).toChar().toString() to 1
                    }
                    if (!isComplatedChar(c)) {
                        return (44032 + 588 * initialBiasTransform(c) + 28 * middleBiasTransform(ch)).toChar().toString() to 1
                    }
                    val bs = (c.code - 44032) % 28
                    val (k1, k2) = decompress(bs) ?: (0 to bs)
                    return "${(c.code - bs + k1).toChar()}${(44032 + 588 * finalToInitBias(k2) + 28 * middleBiasTransform(ch)).toChar()}" to 1
                }
            } else {
                if (pos == str.length) return ch.toString() to 0
            }
        } catch (_: Exception) {
        }
        return ch.toString() to 0
    }

    override fun ch(kp: Int, modifier: Int) = when (kp) {
        GLFW.GLFW_KEY_A -> 'ㅁ'
        GLFW.GLFW_KEY_B -> 'ㅠ'
        GLFW.GLFW_KEY_C -> 'ㅊ'
        GLFW.GLFW_KEY_D -> 'ㅇ'
        GLFW.GLFW_KEY_E -> if (modifier == 1) 'ㄸ' else 'ㄷ'
        GLFW.GLFW_KEY_F -> 'ㄹ'
        GLFW.GLFW_KEY_G -> 'ㅎ'
        GLFW.GLFW_KEY_H -> 'ㅗ'
        GLFW.GLFW_KEY_I -> 'ㅑ'
        GLFW.GLFW_KEY_J -> 'ㅓ'
        GLFW.GLFW_KEY_K -> 'ㅏ'
        GLFW.GLFW_KEY_L -> 'ㅣ'
        GLFW.GLFW_KEY_M -> 'ㅡ'
        GLFW.GLFW_KEY_N -> 'ㅜ'
        GLFW.GLFW_KEY_O -> if (modifier == 1) 'ㅒ' else 'ㅐ'
        GLFW.GLFW_KEY_P -> if (modifier == 1) 'ㅖ' else 'ㅔ'
        GLFW.GLFW_KEY_Q -> if (modifier == 1) 'ㅃ' else 'ㅂ'
        GLFW.GLFW_KEY_R -> if (modifier == 1) 'ㄲ' else 'ㄱ'
        GLFW.GLFW_KEY_S -> 'ㄴ'
        GLFW.GLFW_KEY_T -> if (modifier == 1) 'ㅆ' else 'ㅅ'
        GLFW.GLFW_KEY_U -> 'ㅕ'
        GLFW.GLFW_KEY_V -> 'ㅍ'
        GLFW.GLFW_KEY_W -> if (modifier == 1) 'ㅉ' else 'ㅈ'
        GLFW.GLFW_KEY_X -> 'ㅌ'
        GLFW.GLFW_KEY_Y -> 'ㅛ'
        GLFW.GLFW_KEY_Z -> 'ㅋ'
        else -> throw IllegalStateException("Not allowed key input: $kp")
    }

    fun initialBiasTransform(ch: Char) = when (ch) {
        'ㄱ' -> 0
        'ㄲ' -> 1
        'ㄴ' -> 2
        'ㄷ' -> 3
        'ㄸ' -> 4
        'ㄹ' -> 5
        'ㅁ' -> 6
        'ㅂ' -> 7
        'ㅃ' -> 8
        'ㅅ' -> 9
        'ㅆ' -> 10
        'ㅇ' -> 11
        'ㅈ' -> 12
        'ㅉ' -> 13
        'ㅊ' -> 14
        'ㅋ' -> 15
        'ㅌ' -> 16
        'ㅍ' -> 17
        'ㅎ' -> 18
        else -> throw IllegalArgumentException("Char $ch cannot be initial.")
    }

    fun finalBiasTransform(ch: Char) = when (ch) {
        'ㄱ' -> 1
        'ㄲ' -> 2
        'ㄳ' -> 3
        'ㄴ' -> 4
        'ㄵ' -> 5
        'ㄶ' -> 6
        'ㄷ' -> 7
        'ㄹ' -> 8
        'ㄺ' -> 9
        'ㄻ' -> 10
        'ㄼ' -> 11
        'ㄽ' -> 12
        'ㄾ' -> 13
        'ㄿ' -> 14
        'ㅀ' -> 15
        'ㅁ' -> 16
        'ㅂ' -> 17
        'ㅄ' -> 18
        'ㅅ' -> 19
        'ㅆ' -> 20
        'ㅇ' -> 21
        'ㅈ' -> 22
        'ㅊ' -> 23
        'ㅋ' -> 24
        'ㅌ' -> 25
        'ㅍ' -> 26
        'ㅎ' -> 27
        else -> throw IllegalArgumentException("Char $ch cannot be final.")
    }

    fun middleBiasTransform(ch: Char) = when (ch) {
        'ㅏ' -> 0
        'ㅐ' -> 1
        'ㅑ' -> 2
        'ㅒ' -> 3
        'ㅓ' -> 4
        'ㅔ' -> 5
        'ㅕ' -> 6
        'ㅖ' -> 7
        'ㅗ' -> 8
        'ㅘ' -> 9
        'ㅙ' -> 10
        'ㅚ' -> 11
        'ㅛ' -> 12
        'ㅜ' -> 13
        'ㅝ' -> 14
        'ㅞ' -> 15
        'ㅟ' -> 16
        'ㅠ' -> 17
        'ㅡ' -> 18
        'ㅢ' -> 19
        'ㅣ' -> 20
        else -> throw IllegalArgumentException("Char $ch cannot be middle.")
    }

    fun combineFinal(a: Int, b: Int): Int {
        if (a == 1 && b == 19) return 3
        if (a == 4 && b == 22) return 5
        if (a == 4 && b == 27) return 6
        if (a == 8 && b == 1) return 9
        if (a == 8 && b == 16) return 10
        if (a == 8 && b == 17) return 11
        if (a == 8 && b == 19) return 12
        if (a == 8 && b == 25) return 13
        if (a == 8 && b == 26) return 14
        if (a == 8 && b == 27) return 15
        if (a == 17 && b == 19) return 18
        return -1
    }

    fun decompress(x: Int) = when (x) {
        //2 -> 1 to 1
        3 -> 1 to 19
        5 -> 4 to 22
        6 -> 4 to 27
        9 -> 8 to 1
        10 -> 8 to 16
        11 -> 8 to 17
        12 -> 8 to 19
        13 -> 8 to 25
        14 -> 8 to 26
        15 -> 8 to 27
        18 -> 17 to 19
        //20 -> 19 to 19
        else -> null
    }

    fun finalToInitBias(x: Int) = when (x) {
        1 -> 0
        2 -> 1
        4 -> 2
        7 -> 3
        8 -> 5
        16 -> 6
        17 -> 7
        19 -> 9
        20 -> 10
        21 -> 11
        22 -> 12
        23 -> 14
        24 -> 15
        25 -> 16
        26 -> 17
        27 -> 18
        else -> throw IllegalArgumentException("Final $x cannot be initial.")
    }

    fun compressVowel(a: Int, b: Int): Int {
        if (a == 8 && b == 0) return 9
        if (a == 8 && b == 1) return 10
        if (a == 8 && b == 20) return 11
        if (a == 13 && b == 4) return 14
        if (a == 13 && b == 5) return 15
        if (a == 13 && b == 20) return 16
        if (a == 18 && b == 20) return 19
        return -1
    }

    fun decompressVowel(a: Int): Pair<Int, Int>? = when (a) {
        9 -> 8 to 0
        10 -> 8 to 1
        11 -> 8 to 20
        14 -> 13 to 4
        15 -> 13 to 5
        16 -> 13 to 20
        19 -> 18 to 20
        else -> null
    }

    fun initialBiasToJamo(x: Int) = when (x) {
        0 -> 0x3131
        1 -> 0x3132
        2 -> 0x3134
        3 -> 0x3137
        4 -> 0x3138
        5 -> 0x3139
        6 -> 0x3141
        7 -> 0x3142
        8 -> 0x3143
        9 -> 0x3145
        10 -> 0x3146
        11 -> 0x3147
        12 -> 0x3148
        13 -> 0x3149
        14 -> 0x314A
        15 -> 0x314B
        16 -> 0x314C
        17 -> 0x314D
        18 -> 0x314E
        else -> throw IllegalArgumentException("Initial bias $x cannot be jamo.")
    }.toChar()
}