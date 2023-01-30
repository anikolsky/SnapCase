package com.omtorney.snapcase.domain.court

import com.omtorney.snapcase.domain.parser.PageType

sealed class Courts {

    // NoMSK courts
    object Dmitrov : Court, Courts() {

        override val type = PageType.NoMsk
        override val basicUrl = "https://dmitrov--mo.sudrf.ru"

        override fun getScheduleQuery(date: String): String {
            return if (date.isEmpty()) "$basicUrl/modules.php?name=sud_delo"
            else "$basicUrl/modules.php?name=sud_delo&srv_num=1&H_date=$date"
        }

        /*
        fun createSearchQuery(sideName: String, caseNumber: String) = mutableMapOf(
            "srv_num" to "1",
            "name_op" to "r",
            "delo_id" to "1540005",
            "case_type" to "0",
            "new" to "0",
            "G1_PARTS__NAMESS=" to sideName,
            "g1_case__CASE_NUMBERSS=" to caseNumber,
            "g1_case__JUDICIAL_UIDSS=" to "",
            "delo_table" to "g1_case",
            "g1_case__ENTRY_DATE1D=" to "",
            "g1_case__ENTRY_DATE2D=" to "",
            "G1_CASE__JUDGE=" to "",
            "g1_case__RESULT_DATE1D=" to "",
            "g1_case__RESULT_DATE2D=" to "",
            "G1_CASE__RESULT=" to "",
            "G1_CASE__BUILDING_ID=" to "",
            "G1_CASE__COURT_STRUCT=" to "",
            "G1_EVENT__EVENT_NAME=" to "",
            "G1_EVENT__EVENT_DATEDD=" to "",
            "G1_PARTS__PARTS_TYPE=" to "",
            "G1_PARTS__INN_STRSS=" to "",
            "G1_PARTS__KPP_STRSS=" to "",
            "G1_PARTS__OGRN_STRSS=" to "",
            "G1_PARTS__OGRNIP_STRSS=" to "",
            "g1_requirement__ACCESSION_DATE1D=" to "",
            "g1_requirement__ACCESSION_DATE2D=" to "",
            "G1_REQUIREMENT__CATEGORY=" to "",
            "g1_requirement__ESSENCESS=" to "",
            "g1_requirement__JOIN_END_DATE1D=" to "",
            "g1_requirement__JOIN_END_DATE2D=" to "",
            "G1_REQUIREMENT__PUBLICATION_ID=" to "",
            "G1_DOCUMENT__PUBL_DATE1D=" to "",
            "G1_DOCUMENT__PUBL_DATE2D=" to "",
            "G1_CASE__VALIDITY_DATE1D=" to "",
            "G1_CASE__VALIDITY_DATE2D=" to "",
            "G1_ORDER_INFO__ORDER_DATE1D=" to "",
            "G1_ORDER_INFO__ORDER_DATE2D=" to "",
            "G1_ORDER_INFO__ORDER_NUMSS=" to "",
            "G1_ORDER_INFO__STATE_ID=" to "",
            "G1_ORDER_INFO__RECIP_ID=" to ""
        )*/

        override fun getGPKSearchQuery(sideName: String, caseNumber: String) = basicUrl +
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
                "&g1_case__ENTRY_DATE2D=" +
                "&G1_CASE__JUDGE=" +
                "&g1_case__RESULT_DATE1D=" +
                "&g1_case__RESULT_DATE2D=" +
                "&G1_CASE__RESULT=" +
                "&G1_CASE__BUILDING_ID=" +
                "&G1_CASE__COURT_STRUCT=" +
                "&G1_EVENT__EVENT_NAME=" +
                "&G1_EVENT__EVENT_DATEDD=" +
                "&G1_PARTS__PARTS_TYPE=" +
                "&G1_PARTS__INN_STRSS=" +
                "&G1_PARTS__KPP_STRSS=" +
                "&G1_PARTS__OGRN_STRSS=" +
                "&G1_PARTS__OGRNIP_STRSS=" +
                "&g1_requirement__ACCESSION_DATE1D=" +
                "&g1_requirement__ACCESSION_DATE2D=" +
                "&G1_REQUIREMENT__CATEGORY=" +
                "&g1_requirement__ESSENCESS=" +
                "&g1_requirement__JOIN_END_DATE1D=" +
                "&g1_requirement__JOIN_END_DATE2D=" +
                "&G1_REQUIREMENT__PUBLICATION_ID=" +
                "&G1_DOCUMENT__PUBL_DATE1D=" +
                "&G1_DOCUMENT__PUBL_DATE2D=" +
                "&G1_CASE__VALIDITY_DATE1D=" +
                "&G1_CASE__VALIDITY_DATE2D=" +
                "&G1_ORDER_INFO__ORDER_DATE1D=" +
                "&G1_ORDER_INFO__ORDER_DATE2D=" +
                "&G1_ORDER_INFO__ORDER_NUMSS=" +
                "&G1_ORDER_INFO__STATE_ID=" +
                "&G1_ORDER_INFO__RECIP_ID="

        override fun getKASSearchQuery(sideName: String, caseNumber: String) = basicUrl +
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
                "&P1_CASE__ESSENCE=" +
                "&P1_CASE__JUDGE=" +
                "&p1_case__RESULT_DATE1D=" +
                "&p1_case__RESULT_DATE2D=" +
                "&P1_CASE__RESULT=" +
                "&P1_CASE__BUILDING_ID=" +
                "&P1_CASE__COURT_STRUCT=" +
                "&P1_EVENT__EVENT_NAME=" +
                "&P1_EVENT__EVENT_DATEDD=" +
                "&P1_PARTS__PARTS_TYPE=" +
                "&P1_PARTS__INN_STRSS=" +
                "&P1_PARTS__KPP_STRSS=" +
                "&P1_PARTS__OGRN_STRSS=" +
                "&P1_PARTS__OGRNIP_STRSS=" +
                "&P1_RKN_ACCESS_RESTRICTION__RKN_REASON=" +
                "&p1_rkn_access_restriction__RKN_RESTRICT_URLSS=" +
                "&p1_requirement__ACCESSION_DATE1D=" +
                "&p1_requirement__ACCESSION_DATE2D=" +
                "&P1_REQUIREMENT__CATEGORY=" +
                "&p1_requirement__ESSENCESS=" +
                "&p1_requirement__JOIN_END_DATE1D=" +
                "&p1_requirement__JOIN_END_DATE2D=" +
                "&P1_REQUIREMENT__PUBLICATION_ID=" +
                "&P1_DOCUMENT__PUBL_DATE1D=" +
                "&P1_DOCUMENT__PUBL_DATE2D=" +
                "&P1_DOCUMENT__VALIDITY_DATE1D=" +
                "&P1_DOCUMENT__VALIDITY_DATE2D=" +
                "&P1_ORDER_INFO__ORDER_DATE1D=" +
                "&P1_ORDER_INFO__ORDER_DATE2D=" +
                "&P1_ORDER_INFO__ORDER_NUMSS=" +
                "&P1_ORDER_INFO__EXTERNALKEYSS=" +
                "&P1_ORDER_INFO__STATE_ID=" +
                "&P1_ORDER_INFO__RECIP_ID="
    }

    // MSK courts
    object Dorogomilovskij : Court, Courts() {

        override val type = PageType.Msk
        override val basicUrl = "https://mos-gorsud.ru/rs/dorogomilovskij"

        override fun getScheduleQuery(date: String): String {
            TODO("Not yet implemented")
        }

        override fun getGPKSearchQuery(sideName: String, caseNumber: String): String {
            TODO("Not yet implemented")
        }

        override fun getKASSearchQuery(sideName: String, caseNumber: String): String {
            TODO("Not yet implemented")
        }
    }
}
