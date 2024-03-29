package com.omtorney.snapcase.presentation.screen.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.components.CaseColumn
import com.omtorney.snapcase.presentation.components.ErrorMessage
import com.omtorney.snapcase.presentation.components.FilterButton
import com.omtorney.snapcase.presentation.components.LoadingIndicator
import com.omtorney.snapcase.presentation.components.TopBar
import com.omtorney.snapcase.presentation.components.TopBarTitle
import com.omtorney.snapcase.presentation.screen.schedule.components.JudgeFilter
import com.omtorney.snapcase.presentation.screen.schedule.components.ParticipantFilter

@Composable
fun ScheduleScreen(
    state: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit,
    accentColor: Color,
    onCardClick: (Case) -> Unit,
    onActTextClick: (Case) -> Unit
) {
    val judgeList = state.cases.map { it.judge }.distinct()
    val selectedJudge = state.selectedJudge
    val filteredCases = state.filteredCases
    var participantQuery by remember { mutableStateOf(state.participantQuery) }

    Scaffold(topBar = {
        TopBar {
            TopBarTitle(
                title = R.string.schedule,
                modifier = Modifier.weight(1f)
            )
            FilterButton(
                accentColor = accentColor,
                checked = state.isSearchSectionVisible,
                onClick = { onEvent(ScheduleEvent.ToggleSearchSection) }
            ) {
                Text(text = "Фильтрация")
            }
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = state.isSearchSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    JudgeFilter(
                        judgeList = judgeList,
                        selectedJudge = selectedJudge,
                        accentColor = accentColor,
                        onEvent = onEvent
                    )
                    ParticipantFilter(
                        query = participantQuery,
                        accentColor = accentColor,
                        onQueryChange = {
                            participantQuery = it
                            onEvent(ScheduleEvent.FilterByParticipant(participantQuery))
                        },
                        onResetClick = {
                            onEvent(ScheduleEvent.ResetParticipant)
                            participantQuery = ""
                        }
                    )
                }
            }
            CaseColumn(
                items = filteredCases,
                accentColor = accentColor,
                onCardClick = { onCardClick(it) },
                onActTextClick = { onActTextClick(it) }
            )
        }
        if (state.isLoading) {
            LoadingIndicator(accentColor = accentColor)
        }
        if (state.error.isNotBlank()) {
            ErrorMessage(message = state.error)
        }
    }
}
