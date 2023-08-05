package com.omtorney.snapcase.domain.model

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CaseConverters {
    @TypeConverter
    fun processFormJson(json: String): List<ProcessStep> = Json.decodeFromString(json)

    @TypeConverter
    fun jsonFromProcess(process: List<ProcessStep>): String = Json.encodeToString(process)

    @TypeConverter
    fun appealFromJson(json: String): Map<String, String> = Json.decodeFromString(json)

    @TypeConverter
    fun jsonFromAppeal(appeal: Map<String, String>): String = Json.encodeToString(appeal)
}
