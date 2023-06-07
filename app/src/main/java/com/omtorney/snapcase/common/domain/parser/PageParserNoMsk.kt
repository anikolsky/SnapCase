package com.omtorney.snapcase.common.domain.parser

import com.omtorney.snapcase.common.domain.Repository
import com.omtorney.snapcase.common.domain.court.Court
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.domain.model.ProcessStep
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
                val info = rowColumns[4].text()
                val case = Case(
                    url = court.baseUrl + rowColumns[1].child(0).attr("href"),
                    number = getCaseNumber(rowColumns[1].text()),
                    hearingDateTime = rowColumns[2].text(),
                    category = getCaseCategory(info),
                    participants = "${getPlaintiff(info)}\n${getDefendant(info)}",
                    judge = rowColumns[5].text(),
                    result = rowColumns[6].text(),
                    actTextUrl = getCaseActUrl(rowColumns[7], court),
                    courtTitle = court.title
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
            val info = rowColumns[2].text()
            Case(
                url = court.baseUrl + rowColumns.first()?.child(0)?.attr("href"),
                number = getCaseNumber(rowColumns[0].text()),
                receiptDate = rowColumns[1].text(),
                category = getCaseCategory(info),
                participants = "${getPlaintiff(info)}\n${getDefendant(info)}",
                judge = rowColumns[3].text(),
                actDateTime = rowColumns[4].text(),
                result = rowColumns[5].text(),
                actDateForce = rowColumns[6].text(),
                actTextUrl = getCaseActUrl(rowColumns[7], court),
                courtTitle = court.title
            )
        }
    }

    override suspend fun fetchCase(case: Case): Case {
        val document = repository.getJsoupDocument(case.url)
        val participants = mutableListOf<String>()
        val appeal = mutableMapOf<String, String>()

        val caseElement = document?.selectFirst("div#cont1 table#tablcont")
        val processElement = document?.selectFirst("div#cont2 table#tablcont")
        val participantsElement = document?.selectFirst("div#cont3 table#tablcont")
        val appealElement = document?.selectFirst("div#cont4 table#tablcont")

        participantsElement?.select("tr")
            ?.drop(2)
            ?.forEach { row ->
                val columns = row.select("td")
                val type = columns[0].text().trim()
                val name = columns[1].text().trim()
                if (type.isNotEmpty() && name.isNotEmpty()) {
                    participants.add("$type: $name")
                }
            }

        val processSteps = processElement?.select("tr")
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

        case.process.clear()

        val appealRows = appealElement?.select("tr")?.drop(2)
        appealRows?.forEach { row ->
            val tdElements = row.select("td")
            val fieldName = tdElements[0].text().trim()
            val fieldValue = tdElements.last()?.text()?.trim() ?: ""
            appeal[fieldName] = fieldValue
        }

        return Case(
            url = case.url, // caseElement?.getUrl()  ?: "",
            uid = caseElement?.getContent("a") ?: "",
            number = case.number,
            hearingDateTime = case.hearingDateTime,
            category = caseElement?.getContentOfTd("Категория дела") ?: "",
            participants = participants.sorted().joinToString("\n"),
            judge = caseElement?.getContentOfTd("Судья") ?: "",
            receiptDate = caseElement?.getContentOfTd("Дата поступления") ?: "",
            result = caseElement?.getContentOfTd("Результат рассмотрения") ?: "",
            actDateTime = caseElement?.getContentOfTd("Дата рассмотрения") ?: "",
            actDateForce = case.actDateForce,
            actTextUrl = case.actTextUrl,
            process = processSteps,
            appeal = appeal,
            courtTitle = case.courtTitle
        )
    }

    override suspend fun extractActText(url: String): String {
        val act = arrayListOf<String>()
        val page = repository.getJsoupDocument(url)
        val mainSection = page?.getElementById("modSdpContent")
        val paragraph = mainSection?.getElementsByTag("p")
        paragraph?.forEach { act.add(it.text()) }
        return act.joinToString("\n\n")
    }

    override fun getPlaintiff(info: String): String {
        return if (info.contains("АДМ. ИСТЕЦ") && !info.contains("ОТВЕТЧИК"))
            info.substring(info.indexOf("АДМ. ИСТЕЦ"))
        else if (info.contains("АДМ. ИСТЕЦ"))
            info.substring(info.indexOf("АДМ. ИСТЕЦ"), info.indexOf("АДМ. ОТВЕТЧИК") - 1)
        else if (info.contains("ИСТЕЦ") && !info.contains("ОТВЕТЧИК"))
            info.substring(info.indexOf("ИСТЕЦ"))
        else if (info.contains("ИСТЕЦ"))
            info.substring(info.indexOf("ИСТЕЦ"), info.indexOf("ОТВЕТЧИК") - 1)
        else ""
    }

    override fun getDefendant(info: String): String {
        return if (info.contains("АДМ. ОТВЕТЧИК")) {
            info.substring(info.indexOf("АДМ. ОТВЕТЧИК"))
        } else if (info.contains("ОТВЕТЧИК")) {
            info.substring(info.indexOf("ОТВЕТЧИК"))
        } else if (info.contains("ПРАВОНАРУШЕНИЕ")) {
            if (info.contains("-")) {
                info.substring(info.indexOf(":") + 1, info.indexOfLast { it == '-' } - 1)
            } else info.substring(info.indexOf(":") + 1)
        } else info
    }

    override fun getCaseCategory(info: String): String {
        return if (info.contains("КАТЕГОРИЯ") && !info.contains("ИСТЕЦ"))
            info.substring(info.indexOf("КАТЕГОРИЯ"))
        else if (info.contains("КАТЕГОРИЯ"))
            info.substring(info.indexOf("КАТЕГОРИЯ") + 11, info.indexOf("ИСТЕЦ"))
        else if (info.contains("ПРАВОНАРУШЕНИЕ"))
            info.substring(info.indexOf("ПРАВОНАРУШЕНИЕ"), info.indexOf(":")) +
                    ": " + info.substring(info.indexOfLast { it == '-' } + 1)
        else ""
    }

    override fun getCaseNumber(numberString: String): String {
        return if (numberString.contains("~"))
            numberString.substring(0, numberString.indexOf("~") - 1)
        else
            numberString
    }

    override fun getCaseActUrl(element: Element, court: Court): String {
        val linkToAct = element.selectFirst("a")?.attr("href") ?: ""
        return if (linkToAct.isNotEmpty())
            court.baseUrl + linkToAct
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

fun Element?.getContent(cssQuery: String): String {
    return this?.selectFirst(cssQuery)?.ownText() ?: ""
}

fun Element?.getContentOfTd(query: String): String {
    return this?.selectFirst("td:contains($query) + td")?.ownText() ?: ""
}

//fun Element?.getUrl(): String {
//    return this?.selectFirst("a")?.absUrl("href") ?: ""
//}
