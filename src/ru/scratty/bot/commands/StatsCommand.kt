package ru.scratty.bot.commands

import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.scratty.db.User
import ru.scratty.steam.IncorrectUserData
import ru.scratty.steam.SteamLoginSecure
import ru.scratty.steam.parser.HomePage
import ru.scratty.steam.parser.SteamworksParser
import java.text.SimpleDateFormat
import java.util.*

abstract class StatsCommand(private val prefix: String, private val contains: Regex): Command() {

    private var successAuth = false

    abstract fun handle(update: Update)

    abstract fun getDateStart(): GregorianCalendar

    abstract fun getDateEnd(): GregorianCalendar

    abstract fun getNextDate(): String

    abstract fun getPrevDate(): String

    override fun checkCommand(update: Update): Boolean {
        if (update.hasMessage() && update.message.hasText()) {
            isUpdate = false

            return update.message.text.toLowerCase().contains(contains)
        } else if (update.hasCallbackQuery()) {
            isUpdate = true

            return update.callbackQuery.data.contains(contains)
        }

        return false
    }

    override fun handleCommand(update: Update, user: User): String {
        handle(update)

        val parser = SteamworksParser(user.steamLoginSecure)

        if (!parser.checkValid()) {
            successAuth = false
            isUpdate = false

            return if (!user.login.isEmpty() && !user.pass.isEmpty()) {
                try {
                    SteamLoginSecure(user.login, user.pass).execute()
                } catch (e: IncorrectUserData) {
                }

                "Срок действия авторизации истек. Введите код двухфакторной авторизации и повторите запрос"
            } else {
                "Срок действия авторизации истек. Авторизуйтесь с помощью команды /auth"
            }
        } else {
            successAuth = true

            val dateStart = getDateStart()
            val dateEnd = getDateEnd()

            val homePage = SteamworksParser(user.steamLoginSecure).parseHomePage(dateStart.time, dateEnd.time)

            val sdf = SimpleDateFormat("dd.MM.YY")

            return "${sdf.format(dateStart.time)} - ${sdf.format(dateEnd.time)}\n" +
                    "Заработано: ${homePage.get(HomePage.HomePageKey.RECENT_REVENUE)}\n" +
                    "Копий: ${homePage.get(HomePage.HomePageKey.RECENT_TOTAL_UNITS)}\n" +
                    "Продано в Steam: ${homePage.get(HomePage.HomePageKey.RECENT_STEAM_UNITS)}\n" +
                    "Активировано: ${homePage.get(HomePage.HomePageKey.RECENT_RETAIL_ACTIVATIONS)}"
        }
    }

    override fun getKeyboard(): ReplyKeyboard {
        return if (successAuth) {
            val keyboardMarkup = InlineKeyboardMarkup()
            val rows = ArrayList<ArrayList<InlineKeyboardButton>>()
            val row = ArrayList<InlineKeyboardButton>()

            row.add(InlineKeyboardButton("<-").setCallbackData("${prefix}_${getPrevDate()}"))
            row.add(InlineKeyboardButton("->").setCallbackData("${prefix}_${getNextDate()}"))
            rows.add(row)

            keyboardMarkup.keyboard = rows as List<MutableList<InlineKeyboardButton>>?

            keyboardMarkup
        } else {
            super.getKeyboard()
        }
    }
}