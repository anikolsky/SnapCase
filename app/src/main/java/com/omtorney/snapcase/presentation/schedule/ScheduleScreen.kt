package com.omtorney.snapcase.presentation.schedule

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.common.BottomBar
import com.omtorney.snapcase.presentation.common.CaseColumn
import com.omtorney.snapcase.presentation.home.components.Spinner

@Composable
fun ScheduleScreen(
    navController: NavController,
    accentColor: Long,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel() // TODO move to NavHost
) {
    val state = viewModel.state.value
    val judgeList = state.cases.map { it.judge }.distinct()
    val selectedJudge by viewModel.selectedJudge.collectAsStateWithLifecycle()
    val filteredCases by viewModel.filteredCases.collectAsStateWithLifecycle()

    Scaffold(bottomBar = { BottomBar(navController = navController) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = Color(accentColor),
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
                onItemSelected = viewModel::onJudgeSelect,
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
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.small
                    )
            )
            /** Case list */
            CaseColumn(
                items = filteredCases, // state.cases
                accentColor = accentColor,
                onCardClick = { case ->
                    viewModel.cacheCase(case)
                    onCardClick(case)
                },
                onActTextClick = { onActTextClick(it) }
            )
        }
    }
}