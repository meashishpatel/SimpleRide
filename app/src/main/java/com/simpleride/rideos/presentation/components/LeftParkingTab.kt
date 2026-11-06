package com.simpleride.rideos.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LeftParkingTab(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(36.dp)
            .fillMaxHeight(0.6f)
            .background(
                color = Color(0xFF1E2328),
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Top vertical grip lines
            GripLines()

            // Vertical PARKING » text
            Box(
                modifier = Modifier
                    .weight(1f)
                    .rotate(-90f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PARKING »",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            // Bottom vertical grip lines
            GripLines()
        }
    }
}

@Composable
private fun GripLines() {
    Canvas(
        modifier = Modifier
            .width(16.dp)
            .height(24.dp)
    ) {
        val lines = 5
        val step = size.height / lines
        for (i in 0 until lines) {
            val y = i * step
            drawLine(
                color = Color.Gray.copy(alpha = 0.4f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun LeftParkingTabPreview() {
    Box(modifier = Modifier.height(250.dp)) {
        LeftParkingTab()
    }
}
