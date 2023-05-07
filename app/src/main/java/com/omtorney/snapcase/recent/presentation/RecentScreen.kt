package com.omtorney.snapcase.recent.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.components.BackButton
import com.omtorney.snapcase.common.presentation.components.BottomBar
import com.omtorney.snapcase.common.presentation.components.CaseColumn
import com.omtorney.snapcase.common.presentation.components.SettingsButton
import com.omtorney.snapcase.common.presentation.components.TopBar
import com.omtorney.snapcase.common.presentation.components.TopBarTitle
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.presentation.Screen

@Composable
fun RecentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    accentColor: Long,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit,
    viewModel: RecentViewModel = hiltViewModel() // TODO move to NavHost
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopBar {
                BackButton(
                    accentColor = accentColor,
                    onBackClick = onBackClick
                )
                TopBarTitle(
                    title = Screen.Recent.title,
                    accentColor = accentColor,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton(
                    accentColor = accentColor,
                    onSettingsClick = onSettingsClick
                )
            }
        },
        bottomBar = { BottomBar(navController = navController) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                CaseColumn(
                    items = state.cases,
                    accentColor = accentColor,
                    onCardClick = { onCardClick(it) },
                    onActTextClick = { onActTextClick(it) },
                    modifier = Modifier.weight(1f)
                )
                if (state.cases.isNotEmpty()) {
                    Button(
                        onClick = { viewModel.onEvent(RecentEvent.Clear) },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(Color(accentColor)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.clear).uppercase())
                    }
                }
            }
            if (state.cases.isEmpty()) {
                Text(
                    text = stringResource(R.string.recent_list_empty),
                    color = Color.Gray,
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
