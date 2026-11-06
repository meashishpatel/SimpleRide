package com.simpleride.rideos.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Kill switch pill.
 *  - Active: solid red, white text and pulsing, so it cannot be missed.
 *  - Inactive: subdued grey outline.
 *  - Unknown (null): grey "--".
 */
@Composable
fun KillSwitchPill(
    killSwitchActive: Boolean?,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    val active = killSwitchActive == true
    val label = when (killSwitchActive) {
        true -> "KILL SWITCH ACTIVE"
        false -> "KILL SWITCH INACTIVE"
        null -> "KILL SWITCH --"
    }
    val background = if (active) HmiColors.Red else HmiColors.ButtonBg
    val border = if (active) HmiColors.Red else HmiColors.Muted.copy(alpha = 0.4f)
    val textColor = if (active) Color.White else HmiColors.Muted

    val transition = rememberInfiniteTransition(label = "killSwitchPulse")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "killSwitchAlpha"
    )

    Row(
        modifier = modifier
            .semantics { contentDescription = label }
            .graphicsLayer { alpha = if (active) pulse else 1f }
            .background(background, shape)
            .border(1.dp, border, shape)
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "⚠", color = textColor, fontSize = 13.sp)
        Spacer(Modifier.width(6.dp))
        Text(text = label, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun KillSwitchPillPreview() {
    Row {
        KillSwitchPill(true)
        KillSwitchPill(false)
        KillSwitchPill(null)
    }
}