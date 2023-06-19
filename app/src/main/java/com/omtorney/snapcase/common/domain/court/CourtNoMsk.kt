package com.omtorney.snapcase.common.domain.court

import com.omtorney.snapcase.common.domain.model.CaseType
import com.omtorney.snapcase.common.domain.parser.PageType

abstract class CourtNoMsk(
    override val title: String,
    override val baseUrl: String
) : Court(title, PageType.NoMsk, baseUrl) {

    override fun getScheduleQuery(date: String): String {
        return if (date.isEmpty()) "$baseUrl/modules.php?name=sud_delo"
        else "$baseUrl/modules.php?name=sud_delo&srv_num=1&H_date=$date"
    }

    override fun getSearchQuery(caseType: CaseType, sideName: String, caseNumber: String): String {
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
                    "&g1_case__ENTRY_DATE2D=" + getSearchQueryCommonUrlPart(caseType)

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
                    "&P1_CASE__ESSENCE=" + getSearchQueryCommonUrlPart(caseType)
        }
    }

    private fun getSearchQueryCommonUrlPart(caseType: CaseType): String {
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
}
