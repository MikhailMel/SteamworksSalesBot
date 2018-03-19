package ru.scratty.bot.commands

import org.telegram.telegrambots.api.objects.Update
import ru.scratty.db.User

class GetSteamLoginSecureCommand : Command() {

    override fun checkCommand(update: Update): Boolean {
        if (update.hasMessage() && update.message.hasText()) {
            return update.message.text.toLowerCase().contains("/steamloginsecure|/sls".toRegex())
        }

        return false
    }

    override fun handleCommand(update: Update, user: User): String {
        return "Ваш SteamLoginSecure: " + user.steamLoginSecure
    }
}