package com.omtorney.snapcase.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cases")
data class Case(
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uid: String = "",
    @ColumnInfo(name = "url")
    var url: String = "",
    @ColumnInfo(name = "number")
    var number: String = "",
    @ColumnInfo(name = "hearing_date_time")
    var hearingDateTime: String = "",
    @ColumnInfo(name = "category")
    var category: String = "",
    @ColumnInfo(name = "participants")
    var participants: String = "",
    @ColumnInfo(name = "judge")
    var judge: String = "",
    @ColumnInfo(name = "act_date_time")
    var actDateTime: String = "",
    @ColumnInfo(name = "receipt_date")
    var receiptDate: String = "",
    @ColumnInfo(name = "result")
    var result: String = "",
    @ColumnInfo(name = "act_date_force")
    var actDateForce: String = "",
    @ColumnInfo(name = "act_text_url")
    var actTextUrl: String = "",
    @ColumnInfo(name = "last_event")
    var lastEvent: String = "",
    @ColumnInfo(name = "notes")
    var notes: String = ""
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var process = mutableListOf<CaseProcess>()
    @IgnoredOnParcel
    @Ignore
    var appeal = mutableMapOf<String, String>()

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
            ", lastEvent=$lastEvent" +
            ", notes=$notes"

    fun appealToString(): String {
        var output = ""
        appeal.keys.map {
            if (appeal[it]!!.isNotEmpty())
                output += "$it: ${appeal[it]}\n"
        }
        return output
    }
}

data class CaseProcess(
    var event: String = "",
    var date: String = "",
    var time: String = "",
    var result: String = "",
    var cause: String = "",
    var dateOfPublishing: String = "",
) {
    override fun toString(): String {
        var output = "$date в $time\n$event"
        if (result.isNotEmpty()) output += "\nРезультат: $result"
        if (cause.isNotEmpty()) output += " ($cause)"
        if (dateOfPublishing.isNotEmpty()) output += "\nДата размещения: $dateOfPublishing"
        return output
    }
}