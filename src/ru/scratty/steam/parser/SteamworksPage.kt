package ru.scratty.steam.parser

abstract class SteamworksPage<T>(page: String) {

    protected val html = page

    protected val map = HashMap<T, String>()
    
    protected abstract fun parse()

    abstract fun print()

    abstract fun get(keys: T): String
}