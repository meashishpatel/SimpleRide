package com.simpleride.rideos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MotorStatus(
    isMotorOn: Boolean
) {
    val statusColor = if (isMotorOn) {
        Color(0xFF21E68A)
    } else {
        Color(0xFFFF5252)
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
            text = "MOTOR STATUS",
            color = Color.Gray,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .background(statusColor, RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.width(9.dp))

            Text(
                text = if (isMotorOn) "ON" else "OFF",
                color = statusColor,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}