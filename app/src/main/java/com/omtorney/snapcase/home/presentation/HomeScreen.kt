package com.omtorney.snapcase.home.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.domain.court.CaseType
import com.omtorney.snapcase.common.domain.court.Courts
import com.omtorney.snapcase.common.presentation.components.BottomBar
import com.omtorney.snapcase.common.presentation.components.SettingsButton
import com.omtorney.snapcase.common.presentation.components.TopBar
import com.omtorney.snapcase.common.presentation.components.TopBarTitle
import com.omtorney.snapcase.common.presentation.theme.Shapes
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme
import com.omtorney.snapcase.home.presentation.components.Spinner
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navController: NavController,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    accentColor: Color,
    onSearchClick: (String, String, String) -> Unit,
    onScheduleClick: (String, String) -> Unit,
    onSettingsClick: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val dateDialogState = rememberMaterialDialogState()
    val scrollState = rememberScrollState()
    var caseType by remember { mutableStateOf(CaseType.GPK.title) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedCourt = state.selectedCourt
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd.MM.yyyy").format(pickedDate)
        }
    }

//    private lateinit var multiplePermissionResultLauncher: ActivityResultLauncher<Array<String>>
//    private val viewModel by viewModels<MainViewModel>()
//    private val permissionsToRequest = arrayOf(
//        Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )

//        multiplePermissionResultLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            permissionsToRequest.forEach { permission ->
//                viewModel.onPermissionResult(
//                    permission = permission,
//                    isGranted = permissions[permission] == true
//                )
//            }
//        }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar {
                TopBarTitle(
                    title = R.string.app_title,
                    modifier = Modifier.weight(1f)
                )
                SettingsButton(
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
                onItemSelected = { courtTitle -> onEvent(HomeEvent.SetSelectedCourt(courtTitle)) }
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

    /** Date picker dialog */
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
                dateActiveBackgroundColor = accentColor.copy(alpha = 0.5f),
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
    accentColor: Color,
    onItemSelected: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(6.dp),
        elevation = 0.dp,
        backgroundColor = accentColor.copy(alpha = 0.2f),
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(
                        width = 1.dp,
                        color = accentColor,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Composable
fun SearchBlock(
    accentColor: Color,
    onSearchClick: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(6.dp),
        elevation = 0.dp,
        backgroundColor = accentColor.copy(alpha = 0.2f),
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            OutlinedTextField(
                value = input,
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_participant_or_number),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                    )
                },
                onValueChange = { input = it },
                singleLine = true,
                maxLines = 1,
                shape = Shapes.small,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = accentColor,
                    backgroundColor = accentColor.copy(alpha = 0.2f)
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
                colors = ButtonDefaults.buttonColors(accentColor),
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
    accentColor: Color,
    onScheduleClick: (String, String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(6.dp),
        elevation = 0.dp,
        backgroundColor = accentColor.copy(alpha = 0.2f),
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
                    color = accentColor
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = accentColor.copy(alpha = 0.2f)
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
                colors = ButtonDefaults.buttonColors(accentColor),
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

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchBlockPreview() {
    SnapCaseTheme {
        Surface {
            SearchBlock(
                accentColor = Color.Gray,
                onSearchClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SpinnerBlockPreview() {
    SnapCaseTheme {
        Surface {
            SpinnerBlock(
                title = "Choose item",
                items = listOf("item", "item"),
                selectedItem = "item",
                accentColor = Color.Gray,
                onItemSelected = {}
            )
        }
    }
}
