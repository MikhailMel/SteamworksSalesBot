package ru.scratty.bot.commands

import org.telegram.telegrambots.api.objects.Update
import java.text.SimpleDateFormat
import java.util.*

class MonthlyStatsCommand: StatsCommand("month", "month|месяц".toRegex()) {

    private var dateStart = GregorianCalendar()

    private var dateEnd = GregorianCalendar()

    private lateinit var nextDate: String

    private lateinit var prevDate: String

    override fun handle(update: Update) {
        val sdf = SimpleDateFormat("dd.MM.yy")

        if (update.hasMessage() && update.message.hasText()
                && update.message.text.split("_").size == 1) {
            dateStart.set(Calendar.DAY_OF_MONTH, 1)
        } else {
            val date = if (update.hasMessage() && update.message.hasText() && update.message.text.split("_").size > 1)
                update.message.text.split("_")[1]
            else
                update.callbackQuery.data.split("_")[1]

            dateStart.time = sdf.parse(date)
            dateStart[Calendar.HOUR_OF_DAY] = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
            dateStart[Calendar.MINUTE] = Calendar.getInstance()[Calendar.MINUTE]
            dateStart[Calendar.SECOND] = Calendar.getInstance()[Calendar.SECOND]
        }

        dateStart.add(Calendar.HOUR_OF_DAY,(3 - 11))

        dateEnd.time = dateStart.time
        dateEnd.set(Calendar.DAY_OF_MONTH, dateEnd.getActualMaximum(Calendar.DAY_OF_MONTH))

        val buff = dateStart.clone() as GregorianCalendar

        buff.add(Calendar.MONTH, 1)
        nextDate = sdf.format(buff.time)

        buff.add(Calendar.MONTH, -2)
        prevDate = sdf.format(buff.time)
    }

    override fun getDateStart() = dateStart

    override fun getDateEnd() = dateEnd

    override fun getNextDate() = nextDate

    override fun getPrevDate() = prevDate
}