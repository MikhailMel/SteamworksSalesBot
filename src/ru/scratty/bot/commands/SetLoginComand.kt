package ru.scratty.bot.commands

import org.telegram.telegrambots.api.objects.Update
import ru.scratty.db.User

class SetLoginComand: Command() {

    override fun checkCommand(update: Update): Boolean {
        if (update.hasMessage() && update.message.hasText()) {
            return update.message.text.toLowerCase().contains("/логин|/login".toRegex())
        }

        return false
    }

    override fun handleCommand(update: Update, user: User): String {
        val words = update.message.text.split(" ")

        if (words.size != 2)
            return "Некорректное кол-во аргументов"

        user.login = words[1]
        db.updateUser(user)

        return "Логин успешно сохранен"
    }
}