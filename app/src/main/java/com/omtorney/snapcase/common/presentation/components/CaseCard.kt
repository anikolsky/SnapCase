package com.omtorney.snapcase.common.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme

@Composable
fun CaseCard(
    modifier: Modifier = Modifier,
    case: Case,
    isExpanded: Boolean,
    accentColor: Color,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    Card(
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(accentColor.copy(alpha = 0.2f)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(case) }
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 0.dp
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                ) {
                    Text(
                        text = "${case.courtTitle} суд",
                        style = MaterialTheme.typography.bodySmall,
                        color = accentColor.copy(alpha = 0.5f),
                        modifier = Modifier
                            .padding(vertical = 0.dp)
                            .weight(1f)
                    )
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
                Row {
                    TextBlock(
                        title = "Номер дела: ",
                        text = case.number
                    )
                }
                if (case.hearingDateTime.isNotEmpty()) {
                    Row {
                        TextBlock(
                            title = "Время заседания: ",
                            text = case.hearingDateTime,
                            color = accentColor
                        )
                    }
                }
                Column {
                    TextBlock(
                        title = "Участники: ",
                        text = case.participants
                    )
                }
            }
            if (!expanded) {
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            if (expanded) {
                Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)) {
                    Row {
                        TextBlock(
                            title = "Судья: ",
                            text = case.judge
                        )
                    }
                    Row {
                        TextBlock(
                            title = "Категория: ",
                            text = case.category
                        )
                    }
                    if (case.result.isNotEmpty())
                        Column {
                            TextBlock(
                                title = "Решение: ",
                                text = case.result,
                                color = accentColor
                            )
                        }
                    if (case.actDateTime.isNotEmpty())
                        Row {
                            TextBlock(
                                title = "Дата решения: ",
                                text = case.actDateTime
                            )
                        }
                }
            }
            if (case.actTextUrl.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = accentColor.copy(alpha = 0.8f))
                ) {
                    Text(
                        text = stringResource(R.string.show_act).uppercase(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onActTextClick(case.actTextUrl) }
                    )
                }
            }
        }
    }
}

@Composable
fun TextBlock(
    title: String,
    text: String,
    color: Color = LocalContentColor.current,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Text(
        text = title,
        color = color,
        style = style,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = text,
        color = color,
        style = style,
        maxLines = if (title == "Судья: " || title == "Категория: ") 1 else 10,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CaseCardPreview() {
    SnapCaseTheme {
        Surface {
            CaseCard(
                case = testCase,
                isExpanded = true,
                accentColor = Color.Gray,
                onCardClick = {},
                onActTextClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TextBlockPreview() {
    SnapCaseTheme {
        TextBlock(
            title = "Категория",
            text = "Споры о правах на недвижимость"
        )
    }
}

private val testCase = Case(
    "1", "", "", "2-2222/2022", "",
    "ИСТЕЦ: Иванов Иван Иванович ОТВЕТЧИК: Петров Петр Петрович",
    "Судья", "", "", "", "", "", "", mutableListOf(), mutableMapOf(), "Дмитровский городской", ""
)
