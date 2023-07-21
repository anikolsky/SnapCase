package com.omtorney.snapcase.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.omtorney.snapcase.R
import com.omtorney.snapcase.domain.court.Courts
import com.omtorney.snapcase.domain.model.CaseType
import com.omtorney.snapcase.presentation.components.BottomBar
import com.omtorney.snapcase.presentation.components.SettingsButton
import com.omtorney.snapcase.presentation.components.TopBar
import com.omtorney.snapcase.presentation.components.TopBarTitle
import com.omtorney.snapcase.presentation.screen.home.components.NetworkStateNotification
import com.omtorney.snapcase.presentation.screen.home.components.ScheduleBlock
import com.omtorney.snapcase.presentation.screen.home.components.SearchBlock
import com.omtorney.snapcase.presentation.screen.home.components.SpinnerBlock
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
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
    var datePickerOpened by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var caseType by rememberSaveable { mutableStateOf(CaseType.GPK.title) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedCourt = state.selectedCourt
    val shape = MaterialTheme.shapes.extraSmall
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
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center
            ) {
                SpinnerBlock(
                    title = "Выберите суд",
                    items = Courts.getCourtList().map { it.title },
                    selectedItem = selectedCourt,
                    accentColor = accentColor,
                    shape = shape,
                    onItemSelected = { courtTitle -> onEvent(HomeEvent.SetSelectedCourt(courtTitle)) }
                )
                ScheduleBlock(
                    date = formattedDate,
                    court = selectedCourt,
                    accentColor = accentColor,
                    shape = shape,
                    onOpenClick = { datePickerOpened = true },
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
                    shape = shape,
                    onItemSelected = { caseType = it }
                )
                SearchBlock(
                    accentColor = accentColor,
                    shape = shape,
                    onSearchClick = { query ->
                        onSearchClick(caseType, selectedCourt, query)
                    }
                )
            }
            NetworkStateNotification(
                state = state,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    if (datePickerOpened) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { datePickerOpened = false },
            confirmButton = {
                TextButton(onClick = {
                    pickedDate = LocalDate.ofEpochDay(
                        datePickerState.selectedDateMillis?.div((24 * 60 * 60 * 1000)) ?: 0
                    )
                    datePickerOpened = false
                }) {
                    Text(text = "Ок")
                }
            },
            dismissButton = {
                TextButton(onClick = { datePickerOpened = false }) {
                    Text(text = "Отмена")
                }
            },
            shape = shape
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}
