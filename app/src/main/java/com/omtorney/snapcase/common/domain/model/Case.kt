package com.omtorney.snapcase.common.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "cases")
data class Case(
    @PrimaryKey
    var number: String = "",
    var uid: String = "",
    var url: String = "",
    @ColumnInfo(name = "hearing_date_time")
    var hearingDateTime: String = "",
    var category: String = "",
    var type: String = "", // KAS, GK etc
    var participants: String = "",
    var judge: String = "",
    @ColumnInfo(name = "act_date_time")
    var actDateTime: String = "",
    @ColumnInfo(name = "receipt_date")
    var receiptDate: String = "",
    var result: String = "",
    @ColumnInfo(name = "act_date_force")
    var actDateForce: String = "",
    @ColumnInfo(name = "act_text_url")
    var actTextUrl: String = "",
    var process: MutableList<ProcessStep> = mutableListOf(),
    var appeal: MutableMap<String, String> = mutableMapOf(),
    var courtTitle: String = "",
    var notes: String = ""
) {
    override fun toString() = "url=$url" +
            ", number=$number" +
            ", receiptDate=$receiptDate" +
            ", hearingDateTime=$hearingDateTime" +
            ", category=$category" +
            ", participants=$participants" +
            ", judge=$judge" +
            ", actDate=$actDateTime" +
            ", result=$result" +
            ", actDateForce=$actDateForce" +
            ", actTextUrl=$actTextUrl" +
            ", notes=$notes"

    fun appealToString(): String {
        var output = ""
        appeal.keys.map {
            if (appeal[it]!!.isNotEmpty())
                output += "$it: ${appeal[it]}\n"
        }
        return output
    }

    fun doesFieldMatchQuery(query: String, field: String): Boolean {
        return when (field) {
            "judge" -> judge.lowercase().contains(query)
            else -> participants.lowercase().contains(query)
        }
    }
}

data class ProcessStep(
    val event: String,
    val date: String,
    val time: String,
    val result: String,
    val cause: String,
    val dateOfPublishing: String
) {
    override fun toString(): String {
        var output = "$date в $time\n$event"
        if (result.isNotEmpty()) output += "\nРезультат: $result"
        if (cause.isNotEmpty()) output += " ($cause)"
        if (dateOfPublishing.isNotEmpty()) output += "\nДата размещения: $dateOfPublishing"
        return output
    }
}

data class Appeal(
    val receiptDate: String,
    val appealType: String,
    val appealDecisionDate: String,
    val appealDecision: String,
    val deadlineDefectElimination: String,
    val deadlineFilingObjections: String,
    val upperCourt: String,
    val upperCourtSendDate: String,
    val upperCourtHearingDate: String,
    val upperCourtHearingTime: String
)

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
