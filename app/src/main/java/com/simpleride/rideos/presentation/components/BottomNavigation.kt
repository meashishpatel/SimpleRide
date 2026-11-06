package com.simpleride.rideos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        NavigationItem(
            symbol = "⌂",
            label = "HOME",
            selected = true
        )

        NavigationItem(
            symbol = "➤",
            label = "NAVIGATION"
        )

        NavigationItem(
            symbol = "♫",
            label = "MUSIC"
        )

        NavigationItem(
            symbol = "⚙",
            label = "SETTINGS"
        )
    }
}

@Composable
private fun NavigationItem(
    symbol: String,
    label: String,
    selected: Boolean = false
) {
    val itemColor = if (selected) {
        Color(0xFF21E68A)
    } else {
        Color.Gray
    }

    Row(
        modifier = Modifier
            .background(
                Color(0xFF111719),
                CircleShape
            )
            .padding(
                horizontal = 18.dp,
                vertical = 10.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = symbol,
            color = itemColor,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            color = itemColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigation(){
    BottomNavigation()
}