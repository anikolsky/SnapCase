package com.omtorney.snapcase.domain.parser

sealed class PageType {
    object NoMsk : PageType()
    object Msk : PageType()
}
