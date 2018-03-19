package ru.scratty.bot.commands

import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow
import ru.scratty.db.DBHandler
import ru.scratty.db.User

abstract class Command {

    protected var db: DBHandler = DBHandler.INSTANCE

    var isUpdate = false

    abstract fun checkCommand(update: Update): Boolean

    abstract fun handleCommand(update: Update, user: User): String

    open fun getKeyboard(): ReplyKeyboard {
        val keyboardMarkup = ReplyKeyboardMarkup()
        val keyboard = ArrayList<KeyboardRow>()

        var row = KeyboardRow()
        row.add("День")
        row.add("Неделя")
        row.add("Месяц")
        keyboard.add(row)


        row = KeyboardRow()
        row.add("Помощь")
        keyboard.add(row)
        keyboardMarkup.keyboard = keyboard
        keyboardMarkup.resizeKeyboard = true

        return keyboardMarkup
    }
}