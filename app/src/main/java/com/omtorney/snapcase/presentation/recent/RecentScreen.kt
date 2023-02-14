package com.omtorney.snapcase.presentation.recent

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import com.omtorney.snapcase.presentation.Screen
import com.omtorney.snapcase.presentation.common.*

@Composable
fun RecentScreen(
    navController: NavController,
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RecentViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.value

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column {
                TopBar {
                    BackButton { onBackClick() }
                    TopBarTitle(
                        title = Screen.Recent.title,
                        modifier = Modifier.weight(1f)
                    )
                    SettingsButton { onSettingsClick() }
                }
                CaseColumn(
                    items = state.cases,
                    onCardClick = {},
                    onActTextClick = {},
                    modifier = Modifier.weight(1f)
                )
                if (state.cases.isNotEmpty()) {
                    Button(
                        onClick = { viewModel.onEvent(RecentEvent.Clear) },
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.clear))
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