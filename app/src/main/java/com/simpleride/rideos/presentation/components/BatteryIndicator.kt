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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BatteryIndicator(
    batteryPercentage: Int,
    range: Int
) {
    Row(
        modifier = Modifier
            .background(
                Color(0xFF111719),
                RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BatteryIcon()

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = "$batteryPercentage%",
                color = Color(0xFF21E68A),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "BATTERY",
                color = Color.Gray,
                fontSize = 9.sp
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(
                text = "$range km",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "RANGE",
                color = Color.Gray,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
fun BatteryIcon(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF21E68A)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Main battery body
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(15.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(0xFF111719),
                        RoundedCornerShape(2.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f)
                    .background(
                        color,
                        RoundedCornerShape(2.dp)
                    )
            )
        }

        // Battery terminal
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(7.dp)
                .background(
                    color,
                    RoundedCornerShape(
                        topEnd = 2.dp,
                        bottomEnd = 2.dp
                    )
                )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun BatteryIndicatorPreview() {
    BatteryIndicator(
        batteryPercentage = 85,
        range = 120
    )
}