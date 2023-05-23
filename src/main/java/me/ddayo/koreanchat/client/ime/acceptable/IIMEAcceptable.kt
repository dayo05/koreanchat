package me.ddayo.koreanchat.client.ime.acceptable

interface IIMEAcceptable {
    val text: String
    val cursor: Int
    fun accept(str: String, rewritten: Int)
}