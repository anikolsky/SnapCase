package com.omtorney.snapcase.schedule.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.presentation.components.BottomBar
import com.omtorney.snapcase.common.presentation.components.CaseColumn
import com.omtorney.snapcase.home.presentation.components.Spinner

@Composable
fun ScheduleScreen(
    navController: NavController,
    state: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit,
    accentColor: Color,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit
) {
    val judgeList = state.cases.map { it.judge }.distinct()
    val selectedJudge = state.selectedJudge
    val filteredCases = state.filteredCases

    Scaffold(bottomBar = { BottomBar(navController = navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = accentColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            if (state.error.isNotBlank()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            /** Judge filter */
            Text(
                text = "Выберите судью для фильтрации дел",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 0.dp)
            )
            Spinner(
                dropDownModifier = Modifier.wrapContentSize(),
                items = judgeList,
                selectedItem = selectedJudge,
                onItemSelected = { judge -> onEvent(ScheduleEvent.SelectJudge(judge)) },
                selectedItemFactory = { modifier, item ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.body1
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_round_arrow_drop_down),
                            contentDescription = "Drop down"
                        )
                    }
                },
                dropdownItemFactory = { item, _ ->
                    Text(
                        text = item,
                        style = MaterialTheme.typography.body1
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
                    .border(
                        width = 1.dp,
                        color = accentColor,
                        shape = MaterialTheme.shapes.small
                    )
            )
            /** Case list */
            CaseColumn(
                items = filteredCases, // state.cases
                accentColor = accentColor,
                onCardClick = { case ->
                    onEvent(ScheduleEvent.CacheCase(case))
                    onCardClick(case)
                },
                onActTextClick = { onActTextClick(it) }
            )
        }
    }
}
