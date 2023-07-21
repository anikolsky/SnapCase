package com.omtorney.snapcase.domain.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CaseConverters {
    @TypeConverter
    fun fromProcessJson(json: String): List<ProcessStep> {
        return Gson().fromJson(json, object : TypeToken<MutableList<ProcessStep>>() {}.type)
    }

    @TypeConverter
    fun toProcessJson(process: List<ProcessStep>): String {
        return Gson().toJson(process)
    }

    @TypeConverter
    fun fromAppealJson(json: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toAppealJson(appeal: Map<String, String>): String {
        return Gson().toJson(appeal)
    }
}
