package com.omtorney.snapcase.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.domain.model.Case

@Composable
fun CaseColumn(
    items: List<Case>,
    onCardClick: (Case) -> Unit,
    onActTextClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 0.dp)
        ) {
            items.map { case ->
                item {
                    CaseCard(
                        case = case,
                        isExpanded = false,
                        onCardClick = { onCardClick(it) },
                        onActTextClick = { onActTextClick(it) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CaseColumnPreview() {
    CaseColumn(items = listOf(testCase, testCase), {}, {})
}

private val testCase = Case(
    "1", "", "", "2-2222/2022", "", "",
    "ИСТЕЦ: Иванов Иван Иванович ОТВЕТЧИК: Петров Петр Петрович",
    "", "", "", "", "", "", ""
)