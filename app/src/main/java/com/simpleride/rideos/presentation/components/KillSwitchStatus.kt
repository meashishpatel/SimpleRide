package com.simpleride.rideos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KillSwitchStatus(
    isActive: Boolean
) {
    val statusColor = if (isActive) {
        Color(0xFFFF5252)
    } else {
        Color(0xFF21E68A)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF111719),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "KILL SWITCH",
            color = Color.Gray,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .background(statusColor, RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(9.dp))

            Text(
                text = if (isActive) "ACTIVE" else "INACTIVE",
                color = statusColor,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}