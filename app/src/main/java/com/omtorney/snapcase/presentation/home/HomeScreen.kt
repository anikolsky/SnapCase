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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.vanpra.composematerialdialogs.datetime.date.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navController: NavController,
    accentColor: Long,
    onSearchClick: (String, String, String) -> Unit,
    onScheduleClick: (String, String) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel() // TODO move to NavHost
) {
    val scaffoldState = rememberScaffoldState()
    val dateDialogState = rememberMaterialDialogState()
    val scrollState = rememberScrollState()
    var caseType by remember { mutableStateOf(CaseType.GPK.title) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedCourt by viewModel.selectedCourt.collectAsState()
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
                    accentColor = accentColor,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton(
                    accentColor = accentColor,
                    onSettingsClick = onSettingsClick
                )
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
                items = Courts.getCourtList().map { it.title },
                selectedItem = selectedCourt,
                accentColor = accentColor,
                onItemSelected = viewModel::setSelectedCourt
            )
            ScheduleBlock(
                dateDialogState = dateDialogState,
                date = formattedDate,
                court = selectedCourt,
                accentColor = accentColor,
                onScheduleClick = { date, court ->
                    onScheduleClick(date, court)
                }
            )
            SpinnerBlock(
                title = "Выберите вид производства",
                items = listOf(
                    CaseType.GPK.title,
                    CaseType.KAS.title
                ),
                selectedItem = caseType,
                accentColor = accentColor,
                onItemSelected = { caseType = it }
            )
            SearchBlock(
                accentColor = accentColor,
                onSearchClick = { query ->
                    onSearchClick(caseType, selectedCourt, query)
                })
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
            title = "Выберите дату",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colors.background,
                headerTextColor = MaterialTheme.colors.onBackground,
                calendarHeaderTextColor = MaterialTheme.colors.onBackground,
                dateActiveBackgroundColor = Color(accentColor).copy(alpha = 0.5f),
                dateInactiveBackgroundColor = MaterialTheme.colors.background.copy(alpha = 0.3f),
                dateActiveTextColor = MaterialTheme.colors.onBackground,
                dateInactiveTextColor = MaterialTheme.colors.onBackground
            )
        ) {
            pickedDate = it
        }
    }
}

@Composable
fun SpinnerBlock(
    title: String,
    items: List<String>,
    selectedItem: String,
    accentColor: Long,
    onItemSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 6.dp)
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
                items = items,
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
                        color = Color(accentColor),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Composable
fun SearchBlock(
    accentColor: Long,
    onSearchClick: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
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
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(accentColor),
                    unfocusedBorderColor = Color(accentColor)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = Shapes.small,
                colors = ButtonDefaults.buttonColors(Color(accentColor)),
                onClick = {
                    if (input.isEmpty()) {
                        Toast.makeText(
                            context,
                            R.string.enter_participant_or_number,
                            Toast.LENGTH_SHORT
                        ).show()
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
    court: String,
    accentColor: Long,
    onScheduleClick: (String, String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
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
                    color = Color(accentColor)
                ),
                modifier = Modifier
                    .height(46.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = date,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body1
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = Shapes.small,
                colors = ButtonDefaults.buttonColors(Color(accentColor)),
                onClick = { onScheduleClick(date, court) }
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