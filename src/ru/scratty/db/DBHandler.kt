package ru.scratty.db

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.updateOne

class DBHandler private constructor() {

    private object Holder {
        val INSTANCE = DBHandler()
    }

    companion object {
        val INSTANCE: DBHandler by lazy { Holder.INSTANCE }

        private var users: MongoCollection<User>? = null
    }

    init {
        val client = KMongo.createClient()
        val usersDB = client.getDatabase("steamworks_sales_bot")

        users = usersDB.getCollection<User>("users")
    }

    fun userIsExists(userId: Long): Boolean {
        return users!!.count() > 0 && getUser(userId)!!._id != 0L
    }

    fun registration(userId: Long, name: String) {
        users!!.insertOne(User(userId, name))
    }

    fun getUser(userId: Long): User? {
        val user = users!!.find(eq("_id", userId))

        return if (user.count() > 0) user.first() else User()
    }

    fun updateUser(user: User) {
        users!!.updateOne(user)
    }
}