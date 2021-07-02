package com.marcuseisele.springbootfieldfiltering

import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.ZonedDateTime

@RestController
class UserController(
    val userService: UserService
) {

    @GetMapping("/users/{id}")
    fun getUser(
        @PathVariable("id") id: String,
        @RequestParam(required = false, value = "fields") fields: String?
    ): MappingJacksonValue {


        val fromUser = UserDto.fromUser(userService.getUser(id))

        val mappingJacksonValue = MappingJacksonValue(fromUser)

        if (!fields.isNullOrEmpty()) {
            mappingJacksonValue.filters =
                SimpleFilterProvider().addFilter(
                    FIELDS_FILTER,
                    SimpleBeanPropertyFilter.filterOutAllExcept(fields.split(",").toSet())
                )
        }
        return mappingJacksonValue
    }
}

const val FIELDS_FILTER = "FIELDS_FILTER"

@JsonFilter(FIELDS_FILTER)
data class UserDto(
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val profession: String,
    val createdAt: ZonedDateTime
) {
    companion object {
        fun fromUser(user: User) = UserDto(
            firstName = user.firstName,
            lastName = user.lastName,
            birthday = user.birthday,
            profession = user.profession,
            createdAt = user.createdAt
        )
    }


}
