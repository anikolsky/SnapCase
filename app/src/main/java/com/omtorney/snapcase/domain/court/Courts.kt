package com.omtorney.snapcase.domain.court

object Courts {
    private val courtList: List<Court> = listOf(
        object : CourtNoMsk("Московский областной", "http://oblsud.mo.sudrf.ru") {},
        object : CourtNoMsk("Дмитровский городской", "https://dmitrov--mo.sudrf.ru") {},
        object : CourtNoMsk("Долгопрудненский городской", "http://dolgoprudniy.mo.sudrf.ru") {},
        object : CourtNoMsk("Дубненский городской", "http://dubna.mo.sudrf.ru") {},
        object : CourtNoMsk("Красногорский городской", "http://krasnogorsk.mo.sudrf.ru") {},
        object : CourtNoMsk("Лобненский городской", "http://lobnia.mo.sudrf.ru") {},
        object : CourtNoMsk("Мытищинский городской", "http://mitishy.mo.sudrf.ru") {},
        object : CourtNoMsk("Пушкинский городской", "http://pushkino.mo.sudrf.ru") {},
        object : CourtNoMsk("Сергиево-Посадский городской", "http://sergiev-posad.mo.sudrf.ru") {},
        object : CourtNoMsk("Талдомский городской", "http://taldom.mo.sudrf.ru") {},
        object : CourtNoMsk("Химкинский городской", "http://himki.mo.sudrf.ru") {}

//    object : CourtMsk("Дорогомиловский районный", "http://mos-gorsud.ru/rs/dorogomilovskij") {}
    )

    fun getCourtList() = courtList

    fun getCourt(title: String): Court {
        return courtList.find { it.title == title }
            ?: throw NoSuchElementException("Court not found")
    }
}
