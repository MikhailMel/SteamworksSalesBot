package ru.scratty.steam

import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair

class SteamLoginSecure(private val login: String, private val pass: String, private val code: String = "") {

    private val URL: String = "http://scratty.ru/steamlogin/index.php"

    fun execute(): String {
        val params = ArrayList<BasicNameValuePair>(3)
        params.add(BasicNameValuePair("login", login))
        params.add(BasicNameValuePair("pass", pass))
        params.add(BasicNameValuePair("otp", code))

        val post = HttpPost(URL)
        post.entity = UrlEncodedFormEntity(params, "UTF-8")

       val httpClient = HttpClients.custom()
               .build()

        val steamLoginSecure = BasicResponseHandler().handleResponse(httpClient.execute(post))

        println(steamLoginSecure)
        if (steamLoginSecure == "Error")
            throw IncorrectUserData()

        return steamLoginSecure
    }
}

class IncorrectUserData: Throwable() {
}