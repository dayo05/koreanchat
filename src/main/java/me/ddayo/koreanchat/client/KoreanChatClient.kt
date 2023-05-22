package me.ddayo.koreanchat.client

import org.apache.logging.log4j.LogManager

object KoreanChatClient {
    @JvmStatic
    val isDebug = false
    fun init() {
        if(isDebug)
            LogManager.getLogger().info("DEBUG MODE ENABLED!!!!!!!!!!!!!!!!!!!!!!!!!!!! :D")
    }
}
