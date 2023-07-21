package com.omtorney.snapcase.presentation.screen.schedule.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.presentation.screen.home.components.Spinner
import com.omtorney.snapcase.presentation.screen.schedule.ScheduleEvent

@Composable
fun JudgeFilter(
    judgeList: List<String>,
    selectedJudge: String,
    accentColor: Color,
    onEvent: (ScheduleEvent) -> Unit,
) {
    Text(
        text = "По судье",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(IntrinsicSize.Max)
    ) {
        Spinner(
            dropDownModifier = Modifier.wrapContentSize(),
            items = judgeList,
            selectedItem = selectedJudge,
            onItemSelected = { judge -> onEvent(ScheduleEvent.SelectJudge(judge)) },
            selectedItemFactory = { modifier, item ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = item,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_round_arrow_drop_down),
                        contentDescription = "Drop down"
                    )
                }
            },
            dropdownItemFactory = { item, _ ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 0.dp, top = 8.dp, bottom = 0.dp)
                .border(
                    width = 1.dp,
                    color = accentColor,
                    shape = MaterialTheme.shapes.extraSmall
                )
        )
        TextButton(
            onClick = { onEvent(ScheduleEvent.ResetJudge) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 8.dp, top = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_restart_alt),
                    contentDescription = "Reset judges",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Сброс",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
