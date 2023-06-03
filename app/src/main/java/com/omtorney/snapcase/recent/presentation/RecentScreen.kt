package com.omtorney.snapcase.recent.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    navController: NavController,
    state: RecentState,
    onEvent: (RecentEvent) -> Unit,
    accentColor: Color,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar {
                BackButton(
                    onBackClick = onBackClick
                )
                TopBarTitle(
                    title = Screen.Recent.title,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton(
                    onSettingsClick = onSettingsClick
                )
            }
        },
        bottomBar = { BottomBar(navController = navController) },
        modifier = Modifier.fillMaxSize()
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
                        onClick = { onEvent(RecentEvent.Clear) },
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = ButtonDefaults.buttonColors(accentColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
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
