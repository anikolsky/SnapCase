package com.omtorney.snapcase.domain.parser

import com.omtorney.snapcase.domain.repository.Repository
import com.omtorney.snapcase.domain.court.Court
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.domain.model.ProcessStep
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class PageParserNoMsk @Inject constructor(
    private val repository: Repository
) : PageParser {

    override fun extractSchedule(document: Document, court: Court): List<Case> {
        val caseList = mutableListOf<Case>()
        var currentCaseType = ""
        val caseRows = document.select("#tablcont tr").drop(1)
        for (row in caseRows) {
            val rowColumns = row.select("td")
            if (rowColumns.size == 8) {
                // If it's a case row
                var case = Case("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
                val number = getCaseNumber(rowColumns[1].text())
                val info = rowColumns[4].text()
                case = defineBasicData(info, case)
                case = case.copy(
                    number = number,
                    url = court.baseUrl + rowColumns[1].child(0).attr("href"),
                    courtTitle = court.title,
//                    category = getCaseCategory(info),
                    judge = rowColumns[5].text(),
//                    participants = "${getPlaintiff(info)}\n${getDefendant(info)}",
                    hearingDateTime = rowColumns[2].text(),
                    result = rowColumns[6].text(),
                    actTextUrl = getCaseActUrl(rowColumns[7], court)
                )
                caseList.add(case)
            } else {
                // If it's a case type row
                val caseType = rowColumns[0].text().trim()
                currentCaseType = caseType
            }
        }
        return caseList
    }

    override fun extractSearchResult(document: Document, court: Court): List<Case> {
        // TODO: загружается только первый лист результатов, обернуть в цикл, вызвать createPagesUrlsList()
        // TODO: загружать следующие страницы только по запросу
        val caseRows = document.select("#tablcont tr[valign=top]")
        return caseRows.map { element ->
            val rowColumns = element.select("td")
            var case = Case("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
            val number = getCaseNumber(rowColumns[0].text())
            val info = rowColumns[2].text()
            case = defineBasicData(info, case)
            case.copy(
                number = number,
                url = court.baseUrl + rowColumns.first()?.child(0)?.attr("href"),
                courtTitle = court.title,
//                category = getCaseCategory(info),
                judge = rowColumns[3].text(),
//                participants = "${getPlaintiff(info)}\n${getDefendant(info)}",
                receiptDate = rowColumns[1].text(),
                result = rowColumns[5].text(),
                actDateTime = rowColumns[4].text(),
                actDateForce = rowColumns[6].text(),
                actTextUrl = getCaseActUrl(rowColumns[7], court)
            )
        }
    }

    override suspend fun fetchCase(case: Case, isFavorite: Boolean): Case {
        var actualUrl = case.url
        var actualNumber = case.number

        if (isFavorite) {
            val uidCard = repository.getJsoupDocument(case.permanentUrl)
            val uidElement = uidCard?.selectFirst("div#resultTable table#tablcont")
            val lastUrl = uidElement?.select("a")?.last()
            actualUrl = lastUrl?.absUrl("href") ?: case.url
            actualNumber = lastUrl?.text() ?: case.number
        }

        val tabElements = mutableMapOf<String, Element>()
        val tabTitles = listOf(
            Tab.Case.title,
            Tab.Process.title,
            Tab.Participants.title,
            Tab.Appeal.title
        )

        val document = repository.getJsoupDocument(actualUrl)

        tabTitles.forEach { title ->
            val tabElement = document?.selectFirst("th:contains($title)")?.parent()?.parent()
            tabElement?.let { tab -> tabElements[title] = tab }
        }

        val caseElement = tabElements[Tab.Case.title]
        val processElement = tabElements[Tab.Process.title]
        val participantsElement = tabElements[Tab.Participants.title]
        val appealElement = tabElements[Tab.Appeal.title]
        val participants = fetchParticipants(participantsElement)
        val processSteps = fetchProcess(processElement)
        val appeal = fetchAppeal(appealElement)
        case.process.clear()

        return Case(
            number = getCaseNumber(actualNumber),
            uid = caseElement?.selectFirst("a")?.text() ?: "",
            url = actualUrl,
            permanentUrl = case.permanentUrl.ifEmpty { caseElement?.getUrl() ?: "" },
            courtTitle = case.courtTitle,
            type = "",
            category = caseElement?.getContentOfTd("Категория дела") ?: "",
            judge = caseElement?.getContentOfTd("Судья") ?: "",
            participants = participants.sorted().joinToString("\n"),
            receiptDate = caseElement?.getContentOfTd("Дата поступления") ?: "",
            hearingDateTime = case.hearingDateTime,
            result = caseElement?.getContentOfTd("Результат рассмотрения") ?: "",
            actDateTime = caseElement?.getContentOfTd("Дата рассмотрения") ?: "",
            actDateForce = case.actDateForce,
            actTextUrl = case.actTextUrl,
            notes = "",
            process = processSteps,
            appeal = appeal
        )
    }

    override fun fetchProcess(element: Element?): MutableList<ProcessStep> {
        return element?.select("tr")
            ?.drop(2)
            ?.map { row ->
                val tdElements = row.select("td")
                ProcessStep(
                    event = tdElements[0].ownText(),
                    date = tdElements[1].ownText(),
                    time = tdElements[2].ownText(),
                    result = tdElements[4].ownText(),
                    cause = tdElements[5].ownText(),
                    dateOfPublishing = tdElements[7].ownText()
                )
            }?.toMutableList() ?: mutableListOf()
    }

    override fun fetchAppeal(element: Element?): MutableMap<String, String> {
        val appeal = mutableMapOf<String, String>()
        val appealRows = element?.select("tr")?.drop(2)
        appealRows?.forEach { row ->
            val tdElements = row.select("td")
            val fieldName = tdElements[0].text().trim()
            val fieldValue = tdElements.last()?.text()?.trim() ?: ""
            appeal[fieldName] = fieldValue
        }
        return appeal
    }

    override fun fetchParticipants(element: Element?): MutableList<String> {
        val participants = mutableListOf<String>()
        element?.select("tr")
            ?.drop(2)
            ?.forEach { row ->
                val columns = row.select("td")
                val type = columns[0].text().trim()
                val name = columns[1].text().trim()
                if (type.isNotEmpty() && name.isNotEmpty()) {
                    participants.add("$type: $name")
                }
            }
        return participants
    }

    override suspend fun extractActText(url: String): String {
        val act = arrayListOf<String>()
        val page = repository.getJsoupDocument(url)
        val paragraphs = page?.getElementsByTag("p")
        paragraphs?.forEach { act.add(it.text()) }
        return act.joinToString("\n\n")
    }

    private fun defineBasicData(info: String, case: Case): Case {
        return if (info.contains("АДМ. ИСТЕЦ")) {
            getAdministrativeInfo(info, case)
        } else if (info.contains("ИСТЕЦ(ЗАЯВИТЕЛЬ)")) {
            getCivilInfo(info, case)
        } else if (info.contains("КоАП РФ") || info.contains("УК РФ")) {
            getOtherInfo(info, case)
        } else if (!(info.contains("ИСТЕЦ")) || info.contains("ОТВЕТЧИК") && info.contains("КАТЕГОРИЯ")) {
            case.copy(category = info.substringAfter("КАТЕГОРИЯ: "))
        } else {
            case.copy(participants = info)
        }
    }

    override fun getAdministrativeInfo(info: String, case: Case): Case {
        val plaintiff = if (info.contains("АДМ. ИСТЕЦ") && !info.contains("АДМ. ОТВЕТЧИК")) {
            info.substring(info.indexOf("АДМ. ИСТЕЦ"))
        } else if (info.contains("АДМ. ИСТЕЦ")) {
            info.substring(info.indexOf("АДМ. ИСТЕЦ"), info.indexOf("АДМ. ОТВЕТЧИК") - 1)
        } else {
            ""
        }
        val defendant = if (info.contains("АДМ. ОТВЕТЧИК")) {
            info.substring(info.indexOf("АДМ. ОТВЕТЧИК"))
        } else if (info.contains("АДМ. ОТВЕТЧИК")) {
            info.substring(info.indexOf("АДМ. ОТВЕТЧИК"))
        } else {
            ""
        }
        val category = getCategory(info)
        return case.copy(
            participants = "$plaintiff\n$defendant",
            category = category
        )
    }

    override fun getCivilInfo(info: String, case: Case): Case {
        val plaintiff = if (info.contains("ИСТЕЦ") && !info.contains("ОТВЕТЧИК")) {
            info.substring(info.indexOf("ИСТЕЦ"))
        } else if (info.contains("ИСТЕЦ")) {
            info.substring(info.indexOf("ИСТЕЦ"), info.indexOf("ОТВЕТЧИК") - 1)
        } else {
            ""
        }
        val defendant = if (info.contains("ОТВЕТЧИК")) {
            info.substring(info.indexOf("ОТВЕТЧИК"))
        } else {
            ""
        }
        val category = getCategory(info)
        return case.copy(
            participants = "$plaintiff\n$defendant",
            category = category
        )
    }

    override fun getOtherInfo(info: String, case: Case): Case {
        val participants = if (info.contains("ПРАВОНАРУШЕНИЕ")) {
            info.substring(info.indexOf("ПРАВОНАРУШЕНИЕ") + 16, info.indexOf("ст.") - 3)
        } else {
            info.substring(0, info.indexOf("ст.") - 3)
        }
        val category = getCategory(info)
        return case.copy(
            participants = participants,
            category = category
        )
    }

    override fun getCategory(info: String): String {
        return if (info.contains("КАТЕГОРИЯ") && !(info.contains("АДМ. ИСТЕЦ") || info.contains("ИСТЕЦ"))) {
            info.substring(info.indexOf("КАТЕГОРИЯ"))
        } else if (info.contains("КАТЕГОРИЯ") && info.contains("АДМ. ИСТЕЦ")) {
            info.substring(info.indexOf("КАТЕГОРИЯ") + 11, info.indexOf("АДМ. ИСТЕЦ"))
        } else if (info.contains("КАТЕГОРИЯ") && info.contains("ИСТЕЦ")) {
            info.substring(info.indexOf("КАТЕГОРИЯ") + 11, info.indexOf("ИСТЕЦ"))
        } else if (info.contains("КоАП РФ")) {
            "ПРАВОНАРУШЕНИЕ - ${info.substring(info.indexOf("ст."))}"
        } else if (info.contains("УК РФ")) {
            "ПРЕСТУПЛЕНИЕ - ${info.substring(info.indexOf("ст."))}"
        } else {
            ""
        }
    }

    override fun getCaseNumber(number: String): String {
        return if (number.contains("~"))
            number.substring(0, number.indexOf("~") - 1)
        else
            number
    }

    override fun getCaseActUrl(element: Element, court: Court): String {
        val linkToAct = element.selectFirst("a")?.attr("href") ?: ""
        return if (linkToAct.isNotEmpty()) court.baseUrl + linkToAct
        else linkToAct
    }

    override fun createPagesUrlsList(page: Document, searchUrl: String, court: Court)
            : List<String> {
        val pagesUrls: List<String>

        /** Получить предыдущий элемент того же уровня (из-за особенностей верстки сайта) */
        val pageList = page.select("table[id=tablcont]").first()!!.previousElementSibling()
        val ahrefs = pageList!!.getElementsByTag("a")
        /** Если страниц несколько */
        if (ahrefs.size > 0) {
            /** Получить ссылку на последнюю страницу */
            val lastPageUrl = ahrefs[ahrefs.size - 1]?.attr("href")
            val pageVarIndex = lastPageUrl!!.indexOf("page")

            /** Получить общее число страниц */
            val pagesTotal = lastPageUrl
                .substring(pageVarIndex + 5, lastPageUrl.indexOf('&', pageVarIndex))
                .toInt()
            /** Добавить все ссылки в массив */
            pagesUrls = List(pagesTotal) { index ->
                court.baseUrl + lastPageUrl
                    .removeRange(0, 1)
                    .replace("page=$pagesTotal", "page=${index + 1}")
            }
            /** Если страница одна */
        } else pagesUrls = arrayListOf(searchUrl)
        return pagesUrls
    }
}

fun Element?.getContentOfTd(query: String): String {
    return this?.selectFirst("td:contains($query) + td")?.ownText() ?: ""
}

fun Element?.getUrl(): String {
    return this?.selectFirst("a")?.absUrl("href") ?: ""
}

enum class Tab(val title: String) {
    Case("ДЕЛО"),
    Process("ДВИЖЕНИЕ ДЕЛА"),
    Participants("СТОРОНЫ ПО ДЕЛУ (ТРЕТЬИ ЛИЦА)"),
    Appeal("ОБЖАЛОВАНИЕ РЕШЕНИЙ, ОПРЕДЕЛЕНИЙ (ПОСТ.)")
}