package ru.scratty.bot.commands

import org.telegram.telegrambots.api.objects.Update
import ru.scratty.db.User
import ru.scratty.steam.IncorrectUserData
import ru.scratty.steam.SteamLoginSecure

class AuthorizationCommand: Command() {

    override fun checkCommand(update: Update): Boolean {
        if (update.hasMessage() && update.message.hasText()) {
            if (update.message.text.toLowerCase().contains("/вход|/auth".toRegex())) {
                return true
            } else {
                val user = db.getUser(update.message.chatId)!!

                if (update.message.text.length == 5 && !user.login.isEmpty() && !user.pass.isEmpty())
                    return true
            }
        }

        return false
    }

    override fun handleCommand(update: Update, user: User): String {
        val words = update.message.text.split(" ")

        println(user)

        var login = user.login
        var pass = user.pass
        val code: String

        when(words.size) {
            4 -> {
                login = words[1]
                pass = words[2]
                code = words[3]
            }
            3 -> {
                if (login.isEmpty())
                    return "Ваш логин не сохранен на сервере, используйте команду /вход <логин> <пароль> <код>"

                pass = words[1]
                code = words[2]
            }
            2 -> {
                if (pass.isEmpty())
                    return "Ваш пароль не сохранен на сервере, используйте команду /вход <пароль> <код>"

                code = words[1]
            }
            1 -> {
                if (words[0].contains("/вход|/auth".toRegex()))
                    return "Некорректное ко-во аргументов. Используйте:" +
                            "\n/вход <логин> <пароль> <код>" +
                            "\n/вход <пароль> <код>, если сохранен логин" +
                            "\n/вход <код>, если сохранены логин и пароль"
                code = words[0]
            }
            else -> return "Некорректное ко-во аргументов. Используйте:" +
                    "\n/вход <логин> <пароль> <код>" +
                    "\n/вход <пароль> <код>, если сохранен логин" +
                    "\n/вход <код>, если сохранены логин и пароль"
        }

        val steamLoginSecure = auth(login, pass, code)

        if (steamLoginSecure == "")
            return "Ошибка авторизации. Проверьте данные и повторите попытку."

        user.steamLoginSecure = steamLoginSecure
        db.updateUser(user)

        return "Авторизация успешна"
    }

    private fun auth(login: String, pass: String, code: String): String {
        val sls = SteamLoginSecure(login, pass, code)

        return try {
            sls.execute()
        } catch (e: IncorrectUserData) {
            return ""
        }
    }
}