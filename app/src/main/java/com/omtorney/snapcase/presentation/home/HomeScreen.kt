package com.omtorney.snapcase.presentation.home

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
import androidx.navigation.compose.rememberNavController
import com.omtorney.snapcase.presentation.Screen
import com.omtorney.snapcase.presentation.common.BottomBar
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
    onScheduleClick: (String) -> Unit
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
                SearchBlock(onSearchClick = { onSearchClick(it) })
                Spacer(modifier = Modifier.height(24.dp))
                ScheduleBlock(
                    dateDialogState = dateDialogState,
                    date = formattedDate,
                    onScheduleClick = { onScheduleClick(it) }
                )
            }
        }
    )
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
fun SearchBlock(
    onSearchClick: (String) -> Unit
) {
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
            .padding(horizontal = 12.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
    SpinnerBlock(
        listOf(
            "Гражданское судопроизводство",
//            "Административное судопроизводство"
        ),
        "Гражданское судопроизводство",
        {})
    Button(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = Shapes.small,
        onClick = {
            if (input.isEmpty()) {
                Toast.makeText(context, "Введите имя участника или номер дела", Toast.LENGTH_SHORT)
                    .show()
            } else {
                onSearchClick(input)
            }
        }
    ) { Text(text = "ПОИСК") }
}

@Composable
fun ScheduleBlock(
    dateDialogState: MaterialDialogState,
    date: String,
    onScheduleClick: (String) -> Unit
) {
    Row {
        OutlinedButton(
            modifier = Modifier.height(40.dp),
            onClick = { dateDialogState.show() },
            shape = Shapes.small,
            border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary)
        ) { Text(text = date) }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            modifier = Modifier.height(40.dp),
            shape = Shapes.small,
            onClick = { onScheduleClick(date) }
        ) { Text("ПОКАЗАТЬ РАСПИСАНИЕ") }
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
    HomeScreen(navController = navController, {}, {})
}