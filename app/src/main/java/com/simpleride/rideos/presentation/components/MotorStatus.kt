package com.simpleride.rideos.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Motor ON / OFF pill. `null` means the signal is unknown and is shown as "--". */
@Composable
fun MotorStatusPill(
    motorOn: Boolean?,
    modifier: Modifier = Modifier
) {
    val label = when (motorOn) {
        true -> "ON"
        false -> "OFF"
        null -> "--"
    }
    val color = when (motorOn) {
        true -> HmiColors.Green
        false -> HmiColors.Red
        null -> HmiColors.Muted
    }
    val background = when (motorOn) {
        true -> HmiColors.GreenBg
        false -> HmiColors.RedBg
        null -> HmiColors.ButtonBg
    }
    val shape = RoundedCornerShape(24.dp)

    Row(
        modifier = modifier
            .semantics { contentDescription = "Motor $label" }
            .background(background, shape)
            .border(1.dp, color, shape)
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PowerIcon(color = color)
        Spacer(Modifier.width(6.dp))
        Text(text = label, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PowerIcon(color: Color) {
    Canvas(Modifier.size(14.dp)) {
        val stroke = 2.dp.toPx()
        drawArc(
            color = color,
            startAngle = -60f,
            sweepAngle = 300f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawLine(
            color = color,
            start = Offset(size.width / 2f, 0f),
            end = Offset(size.width / 2f, size.height / 2f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun MotorStatusPillPreview() {
    Row {
        MotorStatusPill(true)
        MotorStatusPill(false)
        MotorStatusPill(null)
    }
}
