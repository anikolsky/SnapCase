package com.omtorney.snapcase.presentation.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

@Composable
fun CaseCard(
    case: Case,
    isExpanded: Boolean,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    Card(
        shape = Shapes.small,
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable { onCardClick(case) }
    ) {
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
                            onClick = { onActTextClick(case.actTextUrl) }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_outline_text),
                                contentDescription = "Открыть решение"
                            )
                            Text(text = "Решение")
                        }
                    }
                }

                // TODO delete from fav - button (or slide?)
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
}

@Composable
fun TextBlock(title: String, text: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.subtitle2
    )
    Text(
        text = text,
        style = if (title == "Время заседания: ")
            MaterialTheme.typography.subtitle1
        else
            MaterialTheme.typography.body1,
        maxLines = if (title == "Судья: " || title == "Категория: ") 1 else 5,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CaseCardPreview() {
    SnapCaseTheme {
        CaseCard(testCase, true, {}, {})
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TextBlockPreview() {
    SnapCaseTheme {
        TextBlock(title = "Категория", text = "Споры о правах на недвижимость")
    }
}

private val testCase = Case(
    "1", "", "", "2-2222/2022", "", "",
    "ИСТЕЦ: Иванов Иван Иванович ОТВЕТЧИК: Петров Петр Петрович",
    "", "", "", "", "", "", ""
)