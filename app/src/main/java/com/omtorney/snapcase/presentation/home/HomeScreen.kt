package com.omtorney.snapcase.presentation.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.omtorney.snapcase.presentation.common.AppName
import com.omtorney.snapcase.presentation.common.BottomBar
import com.omtorney.snapcase.presentation.common.SettingsButton
import com.omtorney.snapcase.presentation.common.TopBar
import com.omtorney.snapcase.presentation.home.components.Spinner
import com.omtorney.snapcase.presentation.ui.theme.Shapes
import com.omtorney.snapcase.presentation.ui.theme.Typography
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.R
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navController: NavController,
    onSearchClick: (String) -> Unit,
    onScheduleClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val dateDialogState = rememberMaterialDialogState()
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd.MM.yyyy").format(pickedDate)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        TopBar {
            AppName(modifier = Modifier.weight(1f))
            SettingsButton { onSettingsClick() }
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            SearchBlock(onSearchClick = { onSearchClick(it) })
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
    availableQuantities: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Spinner(
        modifier = Modifier.wrapContentSize(),
        dropDownModifier = Modifier.wrapContentSize(),
        items = availableQuantities,
        selectedItem = selectedItem,
        onItemSelected = onItemSelected,
        selectedItemFactory = { modifier, item ->
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .wrapContentSize()
            ) {
                Text(item)

                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = ""
                )
            }
        },
        dropdownItemFactory = { item, _ ->
            Text(text = item)
        }
    )
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
        border = BorderStroke(1.dp, color = MaterialTheme.colors.primary.copy(alpha = 0.4f)),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SpinnerBlock(
                listOf(
                    "Дмитровский городской",
//                    "Дорогомиловский районный"
                ),
                "Дмитровский городской"
            ) {}
//            SpinnerBlock(
//                listOf(
//                    "Гражданское судопроизводство",
//                      "Административное судопроизводство"
//                ),
//                "Гражданское судопроизводство"
//            ) {}
            OutlinedTextField(
                value = input,
                textStyle = Typography.body1,
                label = { Text("Введите имя или номер дела") },
                onValueChange = { input = it },
                singleLine = true,
                maxLines = 1,
                shape = Shapes.small,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = Shapes.small,
                onClick = {
                    if (input.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Введите имя участника или номер дела",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        onSearchClick(input)
                    }
                }
            ) { Text(text = "ПОИСК") }
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
        border = BorderStroke(1.dp, color = MaterialTheme.colors.primary.copy(alpha = 0.4f)),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                onClick = { dateDialogState.show() },
                shape = Shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                )
            ) {
                Text(text = date, style = MaterialTheme.typography.subtitle1)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                shape = Shapes.small,
                onClick = { onScheduleClick(date) }
            ) { Text("ПОКАЗАТЬ РАСПИСАНИЕ") }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    CircularProgressIndicator(color = MaterialTheme.colors.secondary, strokeWidth = 5.dp)
//}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController, {}, {}, {})
}