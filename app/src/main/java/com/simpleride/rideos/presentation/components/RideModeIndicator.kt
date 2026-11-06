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
fun RideModeIndicator(
    mode: String = "ECO"
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "SIMPLE RIDE",
            color = Color.White,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .background(
                    Color(0xFF143B2C),
                    RoundedCornerShape(20.dp)
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 5.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mode,
                color = Color(0xFF21E68A),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "RIDE MODE",
                color = Color.Gray,
                fontSize = 9.sp
            )
        }
    }
}