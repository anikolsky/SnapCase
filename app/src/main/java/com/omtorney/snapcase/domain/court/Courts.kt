package com.omtorney.snapcase.domain.court

import com.omtorney.snapcase.domain.parser.PageType

enum class CaseType(val title: String) {
    GPK("Гражданское"),
    KAS("Административное (КАС)")
}

sealed class Courts(val title: String) {

    /** NoMSK courts */
    object Oblsud : Court, Courts("Московский областной") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://oblsud.mo.sudrf.ru"
    }

    object Dmitrov : Court, Courts("Дмитровский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "https://dmitrov--mo.sudrf.ru"
    }

    object Dolgoprudniy : Court, Courts("Долгопрудненский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://dolgoprudniy.mo.sudrf.ru"
    }

    object Dubna : Court, Courts("Дубненский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://dubna.mo.sudrf.ru"
    }

    object Krasnogorsk : Court, Courts("Красногорский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://krasnogorsk.mo.sudrf.ru"
    }

    object Lobnia : Court, Courts("Лобненский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://lobnia.mo.sudrf.ru"
    }

    object Mitishy : Court, Courts("Мытищинский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://mitishy.mo.sudrf.ru"
    }

    object Pushkino : Court, Courts("Пушкинский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://pushkino.mo.sudrf.ru"
    }

    object Sergievposad : Court, Courts("Сергиево-Посадский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://sergiev-posad.mo.sudrf.ru"
    }

    object Taldom : Court, Courts("Талдомский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://taldom.mo.sudrf.ru"
    }

    object Himki : Court, Courts("Химкинский городской") {
        override val type = PageType.NoMsk
        override val baseUrl = "http://himki.mo.sudrf.ru"
    }

    /** MSK courts */
    object Dorogomilovskij : Court, Courts("Дорогомиловский районный") {
        override val type = PageType.Msk
        override val baseUrl = "http://mos-gorsud.ru/rs/dorogomilovskij"
    }

    companion object {
        fun getCourtList(): List<Court> {
            return listOf(
                Oblsud,
                Dmitrov,
                Dolgoprudniy,
                Dubna,
                Krasnogorsk,
                Lobnia,
                Mitishy,
                Pushkino,
                Sergievposad,
                Taldom,
                Himki
            )
        }
    }
}

fun getScheduleQueryNoMsk(baseUrl: String, date: String): String {
    return if (date.isEmpty()) "$baseUrl/modules.php?name=sud_delo"
    else "$baseUrl/modules.php?name=sud_delo&srv_num=1&H_date=$date"
}

fun getSearchQueryNoMsk(
    baseUrl: String,
    caseType: CaseType,
    sideName: String,
    caseNumber: String
): String {
    return when (caseType) {
        CaseType.GPK -> baseUrl +
                "/modules.php?name=sud_delo" +
                "&srv_num=1" +
                "&name_op=r" +
                "&delo_id=1540005" +
                "&case_type=0" +
                "&new=0" +
                "&G1_PARTS__NAMESS=" + sideName +
                "&g1_case__CASE_NUMBERSS=" + caseNumber +
                "&g1_case__JUDICIAL_UIDSS=" +
                "&delo_table=g1_case" +
                "&g1_case__ENTRY_DATE1D=" +
                "&g1_case__ENTRY_DATE2D=" + getSearchQueryCommonUrlPartNoMsk(caseType)
        CaseType.KAS -> baseUrl +
                "/modules.php?name=sud_delo" +
                "&srv_num=1" +
                "&name_op=r" +
                "&delo_id=41" +
                "&case_type=0" +
                "&new=0" +
                "&P1_PARTS__NAMESS=" + sideName +
                "&p1_case__CASE_NUMBERSS=" + caseNumber +
                "&p1_case__JUDICIAL_UIDSS=" +
                "&delo_table=p1_case" +
                "&p1_case__ENTRY_DATE1D=" +
                "&p1_case__ENTRY_DATE2D=" +
                "&P1_CASE__PREV_CASE_NUMBERSS=" +
                "&P1_CASE__MASTER_CASE_NUMBERSS=" +
                "&P1_CASE__ESSENCE=" + getSearchQueryCommonUrlPartNoMsk(caseType)
    }
}

private fun getSearchQueryCommonUrlPartNoMsk(caseType: CaseType): String {
    val key1 = when (caseType) {
        CaseType.GPK -> "G1"
        CaseType.KAS -> "P1"
    }
    val key2 = when (caseType) {
        CaseType.GPK -> "g1"
        CaseType.KAS -> "p1"
    }
    return "&${key1}_CASE__JUDGE=" +
            "&${key2}_case__RESULT_DATE1D=" +
            "&${key2}_case__RESULT_DATE2D=" +
            "&${key1}_CASE__RESULT=" +
            "&${key1}_CASE__BUILDING_ID=" +
            "&${key1}_CASE__COURT_STRUCT=" +
            "&${key1}_EVENT__EVENT_NAME=" +
            "&${key1}_EVENT__EVENT_DATEDD=" +
            "&${key1}_PARTS__PARTS_TYPE=" +
            "&${key1}_PARTS__INN_STRSS=" +
            "&${key1}_PARTS__KPP_STRSS=" +
            "&${key1}_PARTS__OGRN_STRSS=" +
            "&${key1}_PARTS__OGRNIP_STRSS=" +
            "&${key1}_RKN_ACCESS_RESTRICTION__RKN_REASON=" +
            "&${key2}_rkn_access_restriction__RKN_RESTRICT_URLSS=" +
            "&${key2}_requirement__ACCESSION_DATE1D=" +
            "&${key2}_requirement__ACCESSION_DATE2D=" +
            "&${key1}_REQUIREMENT__CATEGORY=" +
            "&${key2}_requirement__ESSENCESS=" +
            "&${key2}_requirement__JOIN_END_DATE1D=" +
            "&${key2}_requirement__JOIN_END_DATE2D=" +
            "&${key1}_REQUIREMENT__PUBLICATION_ID=" +
            "&${key1}_DOCUMENT__PUBL_DATE1D=" +
            "&${key1}_DOCUMENT__PUBL_DATE2D=" +
            "&${key1}_CASE__VALIDITY_DATE1D=" +
            "&${key1}_CASE__VALIDITY_DATE2D=" +
            "&${key1}_ORDER_INFO__ORDER_DATE1D=" +
            "&${key1}_ORDER_INFO__ORDER_DATE2D=" +
            "&${key1}_ORDER_INFO__ORDER_NUMSS=" +
            "&${key1}_ORDER_INFO__EXTERNALKEYSS=" +
            "&${key1}_ORDER_INFO__STATE_ID=" +
            "&${key1}_ORDER_INFO__RECIP_ID="
}
