package com.omtorney.snapcase.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.theme.SnapCaseTheme
import com.omtorney.snapcase.util.Constants

@Composable
fun CaseColumn(
    modifier: Modifier = Modifier,
    items: List<Case>,
    accentColor: Color,
    onCardClick: (Case) -> Unit,
    onActTextClick: (Case) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        items.map { case ->
            item {
                CaseCard(
                    case = case,
                    isExpanded = false,
                    accentColor = accentColor,
                    onCardClick = { onCardClick(it) },
                    onActTextClick = { onActTextClick(it) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CaseColumnPreview() {
    SnapCaseTheme {
        Surface {
            CaseColumn(
                items = listOf(testCase, testCase),
                accentColor = Color(Constants.INITIAL_COLOR),
                onCardClick = {},
                onActTextClick = {}
            )
        }
    }
}

private val testCase = Case(
    "2-22/2022", "", "", "", "09:00", "Споры о правах на недвижимость",
    "ИСТЕЦ: Иванов Иван Иванович ОТВЕТЧИК: Петров Петр Петрович",
    "Истец И.И, Ответчик О.О.", "Иванова И.И.", "01.01.2022", "01.01.2022", "",
    "01.01.2022", "", "Дмитровский городской", "", mutableListOf(), mutableMapOf()
)
