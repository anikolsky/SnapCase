package com.omtorney.snapcase.presentation.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.domain.court.CaseType
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.presentation.common.BottomBar
import com.omtorney.snapcase.presentation.common.SettingsButton
import com.omtorney.snapcase.presentation.common.TopBar
import com.omtorney.snapcase.presentation.common.TopBarTitle
import com.omtorney.snapcase.presentation.home.components.Spinner
import com.omtorney.snapcase.presentation.ui.theme.Shapes
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navController: NavController,
    onSearchClick: (String, String) -> Unit,
    onScheduleClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val dateDialogState = rememberMaterialDialogState()
    val scrollState = rememberScrollState()
    var caseType by remember { mutableStateOf(CaseType.GPK.title) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd.MM.yyyy").format(pickedDate)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar {
                TopBarTitle(
                    title = R.string.app_title,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton { onSettingsClick() }
            }
        },
        scaffoldState = scaffoldState,
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center
        ) {
            SpinnerBlock(
                title = "Выберите суд",
                availableQuantities = listOf(
                    Courts.Dmitrov.title
                ),
                selectedItem = Courts.Dmitrov.title,
                onItemSelected = {}
            )
            SpinnerBlock(
                title = "Выберите вид производства",
                availableQuantities = listOf(
                    CaseType.GPK.title,
                    CaseType.KAS.title
                ),
                selectedItem = caseType,
                onItemSelected = { caseType = it }
            )
            SearchBlock(onSearchClick = { onSearchClick(caseType, it) })
            ScheduleBlock(
                dateDialogState = dateDialogState,
                date = formattedDate,
                onScheduleClick = { onScheduleClick(it) }
            )
        }
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "Отмена")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Выберите дату"
        ) {
            pickedDate = it
        }
    }
}

@Composable
fun SpinnerBlock(
    title: String,
    availableQuantities: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
//        border = BorderStroke(
//            width = 1.dp,
//            color = MaterialTheme.colors.primary.copy(alpha = 0.4f)
//        ),
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Spinner(
                dropDownModifier = Modifier.wrapContentSize(),
                items = availableQuantities,
                selectedItem = selectedItem,
                onItemSelected = onItemSelected,
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
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primary,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Composable
fun SearchBlock(
    onSearchClick: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
//        border = BorderStroke(
//            width = 1.dp,
//            color = MaterialTheme.colors.primary.copy(alpha = 0.4f)
//        ),
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            OutlinedTextField(
                value = input,
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_participant_or_number),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                    )
                },
                onValueChange = { input = it },
                singleLine = true,
                maxLines = 1,
                shape = Shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = Shapes.small,
                onClick = {
                    if (input.isEmpty()) {
                        Toast.makeText(context, R.string.enter_participant_or_number, Toast.LENGTH_SHORT).show()
                    } else {
                        onSearchClick(input)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.search_cases).uppercase(),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Composable
fun ScheduleBlock(
    dateDialogState: MaterialDialogState,
    date: String,
    onScheduleClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
//        border = BorderStroke(
//            width = 1.dp,
//            color = MaterialTheme.colors.primary.copy(alpha = 0.4f)
//        ),
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { dateDialogState.show() },
                shape = Shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                ),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.body1
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = Shapes.small,
                onClick = { onScheduleClick(date) }
            ) {
                Text(
                    text = stringResource(R.string.show_schedule).uppercase(),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    CircularProgressIndicator(color = MaterialTheme.colors.secondary, strokeWidth = 5.dp)
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
//@Composable
//fun PreviewMainScreen() {
//    val navController = rememberNavController()
//    HomeScreen(navController = navController, {}, {}, {})
//}