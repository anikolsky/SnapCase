package com.omtorney.snapcase.presentation.common

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.ui.theme.Shapes
import com.omtorney.snapcase.presentation.ui.theme.SnapCaseTheme
import com.omtorney.snapcase.presentation.ui.theme.Typography

@Composable
fun CaseColumn(
    items: List<Case>,
    modifier: Modifier = Modifier
) {
    Row {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 4.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items.map { item { CaseCard(case = it) } }
        }
    }
}

@Composable
fun CaseCard(case: Case) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable {
                // TODO: navController.navigate("act/$url")
            },
        shape = Shapes.small,
//        backgroundColor = MaterialTheme.colors.background,
        elevation = 6.dp,
//        border = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant)
    ) {
        CardContent(case = case)
    }
}

@Composable
fun CardContent(case: Case) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (case.hearingDateTime.isNotEmpty())
                Row { TextBlock(title = "Время заседания: ", text = case.hearingDateTime) }
            Row { TextBlock(title = "Номер дела: ", text = case.number) }
            Column { TextBlock(title = "Участники: ", text = case.participants) }

            if (expanded) {
                Row { TextBlock(title = "Судья: ", text = case.judge) }
                Row { TextBlock(title = "Категория: ", text = case.category) }
                if (case.result.isNotEmpty())
                    Column { TextBlock(title = "Решение: ", text = case.result) }
                if (case.actDateTime.isNotEmpty())
                    Row { TextBlock(title = "Дата решения: ", text = case.actDateTime) }
                if (case.actTextUrl.isNotEmpty()) {
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {} // TODO show act text (pass url)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_outline_text),
                            contentDescription = "Открыть решение"
                        )
                        Text(text = "Решение")
                    }
                }
            }

            // TODO: delete from fav - button (or slide?)
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painter = if (expanded)
                    painterResource(R.drawable.ic_round_expand_less)
                else
                    painterResource(R.drawable.ic_round_expand_more),
                contentDescription = if (expanded) "Показать" else "Скрыть"
            )
        }
    }
}

@Composable
private fun TextBlock(title: String, text: String) {
    Text(
        text = title,
//        color = MaterialTheme.colors.secondary,
        style = Typography.subtitle1
    )
    Text(
        text = text,
        style = if (title == "Время заседания: ") Typography.subtitle1 else Typography.body1,
        maxLines = if (title == "Судья: " || title == "Категория: ") 1 else 5,
        overflow = TextOverflow.Ellipsis
    )
}

private val testCase = Case(
    1,
    "",
    "",
    "2-2222/2022",
    "",
    "",
    "ИСТЕЦ: Иванов Иван Иванович ОТВЕТЧИК: Петров Петр Петрович",
    "", "", "", "", "", "", "", ""
)


@Preview(showBackground = true)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CardContentPreview() {
    SnapCaseTheme {
        CardContent(testCase)
    }
}

@Preview
@Composable
fun CaseColumnPreview() {
    CaseColumn(items = listOf(testCase, testCase))
}