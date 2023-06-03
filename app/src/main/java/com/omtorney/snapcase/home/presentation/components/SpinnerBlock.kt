package com.omtorney.snapcase.home.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.presentation.theme.SnapCaseTheme

@Composable
fun SpinnerBlock(
    title: String,
    items: List<String>,
    selectedItem: String,
    accentColor: Color,
    shape: CornerBasedShape,
    onItemSelected: (String) -> Unit
) {
    BlockFrame(accentColor = accentColor) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Spinner(
            dropDownModifier = Modifier.wrapContentSize(),
            items = items,
            selectedItem = selectedItem,
            onItemSelected = onItemSelected,
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
                .fillMaxWidth()
                .background(
                    color = accentColor.copy(alpha = 0.2f),
                    shape = shape
                )
                .border(
                    width = 1.dp,
                    color = accentColor,
                    shape = shape
                )
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SpinnerBlockPreview() {
    SnapCaseTheme {
        Surface {
            SpinnerBlock(
                title = "Choose item",
                items = listOf("item", "item"),
                selectedItem = "item",
                accentColor = Color.Gray,
                shape = MaterialTheme.shapes.extraSmall,
                onItemSelected = {}
            )
        }
    }
}
