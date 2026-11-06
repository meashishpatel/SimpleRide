package com.simpleride.rideos.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

private val NeonGreen = Color(0xFF21E68A)
private val CardBackground = Color(0xFF242A30)

@Composable
fun TopConsoleHeader(
    mode: String = "ECO",
    rangeKm: Int = 62,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Hexagonal Green Brand Icon
        HexagonEmblem(
            modifier = Modifier.size(34.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Trapezoidal Console Card
        Box(
            modifier = Modifier
                .width(260.dp)
                .height(48.dp)
                .clip(TrapezoidShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF333B44), CardBackground)
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            // Bright Green Top Line Accent
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(3.dp)
                    .background(NeonGreen, RoundedCornerShape(2.dp))
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ECO Badge
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFF142921),
                            RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, NeonGreen.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = mode,
                        color = NeonGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }

                // Sync / Double Arrow icon
                Text(
                    text = "⇄",
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Range Info
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$rangeKm",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        Text(
                            text = "km",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 10.sp
                        )
                        Text(
                            text = "Range",
                            color = Color.Gray,
                            fontSize = 9.sp,
                            lineHeight = 9.sp
                        )
                    }
                }
            }
        }
    }
}

// Custom Trapezoid Shape for top card
private val TrapezoidShape = GenericShape { size, _ ->
    val inset = size.width * 0.08f
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width - inset, size.height)
    lineTo(inset, size.height)
    close()
}

@Composable
fun HexagonEmblem(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = size.minDimension / 2f * 0.9f

        val hexPath = Path()
        for (i in 0..5) {
            val angle = Math.toRadians((60 * i - 30).toDouble())
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()
            if (i == 0) hexPath.moveTo(x, y) else hexPath.lineTo(x, y)
        }
        hexPath.close()

        drawPath(
            path = hexPath,
            color = Color(0xFF0F261C)
        )
        drawPath(
            path = hexPath,
            color = NeonGreen,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx())
        )

        // Center star/cross
        val starPath = Path().apply {
            moveTo(cx, cy - radius * 0.45f)
            lineTo(cx + radius * 0.15f, cy - radius * 0.15f)
            lineTo(cx + radius * 0.45f, cy)
            lineTo(cx + radius * 0.15f, cy + radius * 0.15f)
            lineTo(cx, cy + radius * 0.45f)
            lineTo(cx - radius * 0.15f, cy + radius * 0.15f)
            lineTo(cx - radius * 0.45f, cy)
            lineTo(cx - radius * 0.15f, cy - radius * 0.15f)
            close()
        }
        drawPath(
            path = starPath,
            color = NeonGreen
        )
    }
}

@Composable
fun RideModeIndicator(
    mode: String = "ECO"
) {
    TopConsoleHeader(mode = mode)
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TopConsoleHeaderPreview() {
    TopConsoleHeader()
}
