package ru.scratty.steam.parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HomePage(page: String, private val today: Boolean) : SteamworksPage<HomePage.HomePageKey>(page) {

    public override fun parse() {
        val doc = Jsoup.parse(html)

        if (today)
            parseToday(doc)
        else
            parseAnyDay(doc)
    }

    private fun parseToday(doc: Document) {
        val elements = doc.getElementsByAttributeValue("align", "right")

        if (elements.size < 40)
            return

        map[HomePageKey.LIFETIME_REVENUE] = elements[0].text()
        map[HomePageKey.LIFETIME_STEAM_UNITS] = elements[1].text()
        map[HomePageKey.LIFETIME_RETAIL_ACTIVATIONS] = elements[2].text()
        map[HomePageKey.LIFETIME_UNITS_TOTAL] = elements[3].text()

        map[HomePageKey.RECENT_REVENUE] = elements[5].text()
        map[HomePageKey.RECENT_TOTAL_UNITS] = elements[25].text()
        map[HomePageKey.RECENT_STEAM_UNITS] = elements[27].text()
        map[HomePageKey.RECENT_RETAIL_ACTIVATIONS] = elements[39].text()
    }

    private fun parseAnyDay(doc: Document) {
        val elements = doc.getElementsByAttributeValue("align", "right")

        if (elements.size < 130)
            return

        map[HomePageKey.LIFETIME_REVENUE] = elements[0].text()
        map[HomePageKey.LIFETIME_STEAM_UNITS] = elements[1].text()
        map[HomePageKey.LIFETIME_RETAIL_ACTIVATIONS] = elements[2].text()
        map[HomePageKey.LIFETIME_UNITS_TOTAL] = elements[3].text()

        map[HomePageKey.RECENT_REVENUE] = elements[10].text()
        map[HomePageKey.RECENT_TOTAL_UNITS] = elements[80].text()
        map[HomePageKey.RECENT_STEAM_UNITS] = elements[87].text()
        map[HomePageKey.RECENT_RETAIL_ACTIVATIONS] = elements[129].text()
    }

    override fun print() {
        println(map)
    }

    override fun get(key: HomePageKey): String {
        return map.getOrDefault(key, "empty field")
    }


    enum class HomePageKey {
        LIFETIME_REVENUE, LIFETIME_STEAM_UNITS, LIFETIME_RETAIL_ACTIVATIONS, LIFETIME_UNITS_TOTAL,
        RECENT_REVENUE, RECENT_TOTAL_UNITS, RECENT_STEAM_UNITS, RECENT_RETAIL_ACTIVATIONS,

    }
}