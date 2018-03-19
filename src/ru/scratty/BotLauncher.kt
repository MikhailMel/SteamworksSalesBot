package ru.scratty

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.TelegramBotsApi
import ru.scratty.bot.SteamworksSalesBot

object BotLauncher {

    @JvmStatic
    fun main(args: Array<String>) {
        ApiContextInitializer.init()
        val telegramBotsApi = TelegramBotsApi()

        telegramBotsApi.registerBot(SteamworksSalesBot())
    }
}