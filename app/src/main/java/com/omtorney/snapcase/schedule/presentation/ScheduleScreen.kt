package com.omtorney.snapcase.schedule.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.presentation.components.BottomBar
import com.omtorney.snapcase.common.presentation.components.CaseColumn
import com.omtorney.snapcase.home.presentation.components.Spinner

@Composable
fun ScheduleScreen(
    state: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit,
    accentColor: Color,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit
) {
    val judgeList = state.cases.map { it.judge }.distinct()
    val selectedJudge = state.selectedJudge
    val filteredCases = state.filteredCases

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            /** Judge filter */
            Text(
                text = "Выберите судью для фильтрации дел",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 2.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Max)
            ) {
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
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyLarge
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
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 0.dp, top = 8.dp, bottom = 0.dp)
                        .border(
                            width = 1.dp,
                            color = accentColor,
                            shape = MaterialTheme.shapes.small
                        )
                )
                TextButton(
                    onClick = { onEvent(ScheduleEvent.ResetJudge) },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = accentColor,
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 8.dp, top = 4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_restart_alt),
                            contentDescription = "Reset judges"
                        )
                        Text(
                            text = "Сброс",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            /** Case list */
            CaseColumn(
                items = filteredCases,
                accentColor = accentColor,
                onCardClick = { case ->
                    onEvent(ScheduleEvent.CacheCase(case))
                    onCardClick(case)
                },
                onActTextClick = { onActTextClick(it) }
            )
        }
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
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
