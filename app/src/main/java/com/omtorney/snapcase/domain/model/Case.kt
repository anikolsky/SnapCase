package com.omtorney.snapcase.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cases")
data class Case(
    var number: String,
    @PrimaryKey
    val uid: String,
    var url: String,
    val permanentUrl: String,
    var courtTitle: String,
    val type: String, // KAS, GK etc
    val category: String,
    var judge: String,
    var participants: String,
    @ColumnInfo(name = "receipt_date")
    val receiptDate: String,
    @ColumnInfo(name = "hearing_date_time")
    var hearingDateTime: String,
    var result: String,
    @ColumnInfo(name = "act_date_time")
    val actDateTime: String,
    @ColumnInfo(name = "act_date_force")
    val actDateForce: String,
    @ColumnInfo(name = "act_text_url")
    var actTextUrl: String,
    var notes: String,
    var process: MutableList<ProcessStep> = mutableListOf(),
    var appeal: MutableMap<String, String> = mutableMapOf()
) {
    override fun toString() = "number=$number" +
            ", uid=$uid" +
            ", url=$url" +
            ", permanentUrl=$permanentUrl" +
            ", courtTitle=$courtTitle" +
            ", type=$type" +
            ", category=$category" +
            ", judge=$judge" +
            ", participants=$participants" +
            ", receiptDate=$receiptDate" +
            ", hearingDateTime=$hearingDateTime" +
            ", result=$result" +
            ", actDateTime=$actDateTime" +
            ", actDateForce=$actDateForce" +
            ", actTextUrl=$actTextUrl" +
            ", notes=$notes"

    fun appealToString(): String {
        var output = ""
        appeal.keys.map {
            if (appeal[it]!!.isNotEmpty()) {
                output += "$it: ${appeal[it]}\n"
            }
        }
        return output
    }

    fun doesFieldMatchQuery(query: String, type: FilterType): Boolean {
        return when (type) {
            FilterType.JUDGE -> judge.lowercase().contains(query)
            FilterType.PARTICIPANT -> participants.lowercase().contains(query)
        }
    }

    enum class FilterType {
        JUDGE,
        PARTICIPANT
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

//data class Appeal(
//    val receiptDate: String,
//    val appealType: String,
//    val appealDecisionDate: String,
//    val appealDecision: String,
//    val deadlineDefectElimination: String,
//    val deadlineFilingObjections: String,
//    val upperCourt: String,
//    val upperCourtSendDate: String,
//    val upperCourtHearingDate: String,
//    val upperCourtHearingTime: String
//)
