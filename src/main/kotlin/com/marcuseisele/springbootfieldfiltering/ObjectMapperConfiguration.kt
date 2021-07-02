package com.marcuseisele.springbootfieldfiltering

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Configuration
class ObjectMapperConfiguration {

    @Primary
    @Bean
    fun configure() =
        ObjectMapper().apply {
            setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
            configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)


            // this filter allows to have a JSON Filter Annotation on top of a DTO and be able to serialize it without extra handling
            setFilterProvider(SimpleFilterProvider().apply {
                this.defaultFilter = SimpleBeanPropertyFilter.serializeAll()
            })

            val javaTimeModule = JavaTimeModule().apply {
                this.addDeserializer(
                    LocalDateTime::class.java,
                    LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME)
                )
            }

            registerModules(javaTimeModule, KotlinModule())
        }
}
