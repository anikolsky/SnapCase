package com.omtorney.snapcase.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.domain.model.Case

@Composable
fun CaseColumn(
    items: List<Case>,
    accentColor: Long,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 0.dp)
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

@Preview
@Composable
fun CaseColumnPreview() {
    CaseColumn(items = listOf(testCase, testCase), 0xFF4D4D5A, {}, {})
}

private val testCase = Case(
    "1", "", "", "2-2222/2022", "",
    "ИСТЕЦ: Иванов Иван Иванович ОТВЕТЧИК: Петров Петр Петрович",
    "Судья", "", "", "", "", "", "", ""
)