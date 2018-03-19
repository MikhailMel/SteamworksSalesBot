package ru.scratty.steam.parser

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.cookie.BasicClientCookie
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class SteamworksParser(private val steamLoginSecure: String) {

    fun parseHomePage(dateStart: Date, dateEnd: Date): HomePage {
        val cookieStore = BasicCookieStore()

        val cookieSteamLoginSecure = BasicClientCookie("steamLoginSecure", steamLoginSecure)
        cookieSteamLoginSecure.domain = "partner.steampowered.com"
        cookieSteamLoginSecure.path = "/"
        cookieStore.addCookie(cookieSteamLoginSecure)

        val sdf = SimpleDateFormat("YYYY-MM-dd")

        val cookieDateStart = BasicClientCookie("dateStart", sdf.format(dateStart))
        cookieDateStart.domain = "partner.steampowered.com"
        cookieDateStart.path = "/"
        cookieStore.addCookie(cookieDateStart)

        val cookieDateEnd = BasicClientCookie("dateEnd", sdf.format(dateEnd))
        cookieDateEnd.domain = "partner.steampowered.com"
        cookieDateEnd.path = "/"
        cookieStore.addCookie(cookieDateEnd)


        val httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build()

        val httpGet = HttpGet("https://partner.steampowered.com/")
        val response = httpClient.execute(httpGet)

        println(sdf.format(dateStart) + " " + sdf.format(dateEnd))

        val now = GregorianCalendar()
        now.set(GregorianCalendar.HOUR_OF_DAY, now.get(GregorianCalendar.HOUR_OF_DAY) - 11 + 3)

        val today = sdf.format(dateStart) == sdf.format(dateEnd) && sdf.format(dateStart) == sdf.format(now.time)
        val homePage = HomePage(BasicResponseHandler().handleResponse(response), today)
        homePage.parse()
        return homePage
    }

    fun checkValid(): Boolean {
        val cookieStore = BasicCookieStore()

        val cookieSteamLoginSecure = BasicClientCookie("steamLoginSecure", steamLoginSecure)
        cookieSteamLoginSecure.domain = "partner.steampowered.com"
        cookieSteamLoginSecure.path = "/"
        cookieStore.addCookie(cookieSteamLoginSecure)

        val httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build()

        val httpGet = HttpGet("https://partner.steampowered.com/")
        val response = httpClient.execute(httpGet)

        val doc = Jsoup.parse(BasicResponseHandler().handleResponse(response))

        return doc.title().contains("Steam Stats")
    }

}