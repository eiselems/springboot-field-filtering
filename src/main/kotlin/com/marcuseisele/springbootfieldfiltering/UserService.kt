package com.marcuseisele.springbootfieldfiltering

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Month
import java.time.ZonedDateTime

@Service
class UserService {

    fun getUser(id: String): User {
        // in reality this comes from a DB
        return User(
            id = id,
            firstName = "John",
            lastName = "Doe",
            birthday = LocalDate.of(1990, Month.DECEMBER, 31),
            profession = "Programmer",
            createdAt = ZonedDateTime.now()
        )
    }
}

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val profession: String,
    val createdAt: ZonedDateTime
)