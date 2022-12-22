package com.omtorney.snapcase.presentation.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.omtorney.snapcase.presentation.*
import com.omtorney.snapcase.ui.theme.Shapes
import com.omtorney.snapcase.ui.theme.Typography
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.R
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(navController: NavController) {
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
        bottomBar = { BottomBar(navController = navController) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SpinnerBlock(
                    listOf("Дмитровский городской", "Дорогомиловский районный"),
                    "Дмитровский городской",
                    {})
                SearchBlock(navController = navController)
                Spacer(modifier = Modifier.height(24.dp))
                ScheduleBlock(
                    navController = navController,
                    dateDialogState = dateDialogState,
                    date = formattedDate
                )
            }
        }
    )
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "Отмена")
        },
//        backgroundColor = MaterialTheme.colors.background
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Выберите дату",
//            colors = DatePickerDefaults.colors(
//                headerBackgroundColor = MaterialTheme.colors.primary,
//                headerTextColor = MaterialTheme.colors.secondary,
//                calendarHeaderTextColor = MaterialTheme.colors.secondary,
//                dateActiveBackgroundColor = MaterialTheme.colors.secondary,
//                dateActiveTextColor = MaterialTheme.colors.background,
//                dateInactiveTextColor = MaterialTheme.colors.secondary
//            )
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
                    contentDescription = "drop down arrow"
                )
            }
        },
        dropdownItemFactory = { item, _ ->
            Text(text = item)
        }
    )
}

@Composable
fun SearchBlock(navController: NavController) {
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current
    OutlinedTextField(
        value = input,
        textStyle = Typography.body1,
        label = { Text("Введите имя или номер дела") },
        onValueChange = { input = it },
        singleLine = true,
        shape = Shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            focusedBorderColor = MaterialTheme.colors.secondary,
//            focusedLabelColor = MaterialTheme.colors.secondary,
//            unfocusedBorderColor = MaterialTheme.colors.onPrimary,
//            unfocusedLabelColor = MaterialTheme.colors.primaryVariant
//        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    SpinnerBlock(
        listOf("Гражданское судопроизводство", "Административное судопроизводство"),
        "Гражданское судопроизводство",
        {})
    Button(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = MaterialTheme.colors.primaryVariant
//        ),
        shape = Shapes.small,
        onClick = {
            if (input.isEmpty()) {
                Toast.makeText(context, "Введите имя участника или номер дела", Toast.LENGTH_SHORT)
                    .show()
            } else {
                navController.navigate("search/$input") {
                    popUpTo("main")
                    launchSingleTop = true
                }
            }
        }
    ) { Text(text = "ПОИСК") }
}

@Composable
fun ScheduleBlock(
    navController: NavController,
    dateDialogState: MaterialDialogState,
    date: String
) {
    Row {
        OutlinedButton(
            modifier = Modifier.height(40.dp),
            onClick = { dateDialogState.show() },
            shape = Shapes.small,
//            colors = ButtonDefaults.outlinedButtonColors(
//                backgroundColor = MaterialTheme.colors.background,
//                contentColor = MaterialTheme.colors.onPrimary
//            ),
            border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary)
        ) { Text(text = date) }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            modifier = Modifier.height(40.dp),
            shape = Shapes.small,
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = MaterialTheme.colors.primaryVariant
//            ),
            onClick = {
                navController.navigate("schedule/$date") {
                    popUpTo("main")
                    launchSingleTop = true
                }
            }
        ) { Text("ПОКАЗАТЬ РАСПИСАНИЕ") }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    CircularProgressIndicator(color = MaterialTheme.colors.secondary, strokeWidth = 5.dp)
//}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}