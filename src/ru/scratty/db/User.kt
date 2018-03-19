package ru.scratty.db

import org.bson.codecs.pojo.annotations.BsonId

data class User(@BsonId var _id: Long = 0L,
                var name: String = "",
                var login: String = "",
                var pass: String = "",
                var steamLoginSecure: String = "")