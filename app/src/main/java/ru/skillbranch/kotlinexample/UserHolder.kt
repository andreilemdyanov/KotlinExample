package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting
import java.lang.IllegalArgumentException

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        return if (map[email.toLowerCase()] != null) throw IllegalArgumentException("A user with this email already exists")
        else User.makeUser(fullName, email = email, password = password)
            .also { user -> map[user.login] = user }
    }

    fun loginUser(login: String, password: String): String? {
        return map[login.trim()]?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        return if (map[rawPhone] != null) throw IllegalArgumentException("A user with this phone already exists")
        else User.makeUser(fullName, phone = rawPhone)
            .also { user ->
                if (user.phone?.length != 12) throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
                map[rawPhone] = user
            }
    }

    fun requestAccessCode(login: String): Unit {
        map[login]?.generateNewAccessCode()
    }

    fun importUsers(list: List<String>): List<User> {
        val users = ArrayList<User>()
        for (userData in list) {
            userData.split(";").apply {
                users.add(User.makeUser(
                    fullName = this[0],
                    email = this[1].isBlankThenNull(),
                    completePassword = this[2].isBlankThenNull(),
                    phone = this[3].isBlankThenNull()
                )
                    .also {
                        map[it.login] = it
                    })
            }
        }
        return users
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}