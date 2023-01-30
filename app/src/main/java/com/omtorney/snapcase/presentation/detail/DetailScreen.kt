package com.omtorney.snapcase.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omtorney.snapcase.domain.model.Case
import com.omtorney.snapcase.presentation.common.CaseCard
import com.omtorney.snapcase.presentation.ui.theme.Grey4

@Composable
fun DetailScreen(
    caseParam: Case?,
    viewModel: DetailViewModel = hiltViewModel()
) {
    viewModel.fillCase(caseParam!!)
    val case by viewModel.case.collectAsState()
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Grey4)
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            CaseCard(case)
        }
        LazyColumn {
            case.process.map {
                item {
                    Card {
                        Text(text = it.toString())
                    }
                }
            }
        }
        LazyColumn {
            case.appealToString()
        }
    }
}