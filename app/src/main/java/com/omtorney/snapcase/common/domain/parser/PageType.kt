package com.omtorney.snapcase.common.domain.parser

sealed class PageType {
    object NoMsk : PageType()
    object Msk : PageType()
}
