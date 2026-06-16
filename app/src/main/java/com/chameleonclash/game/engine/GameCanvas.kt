package com.chameleonclash.game.engine

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Main game canvas - renders background, chameleon, and interactions
 */
@Composable
fun GameCanvas(
    bgColor: Color,
    patternColor: Color,
    chameleonColor: Color,
    chameleonX: Float,  // 0-1 normalized
    chameleonY: Float,
    showChameleon: Boolean,
    isHidingPhase: Boolean,
    seekerTap: Offset?,
    hint: String?,
    onTap: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onTap(offset.x, offset.y)
                }
            }
    ) {
        val w = size.width
        val h = size.height

        // Draw background
        drawRect(color = bgColor)

        // Draw pattern (circles/stripes)
        drawBackgroundPattern(patternColor, w, h)

        // Draw chameleon (hidden or visible)
        if (showChameleon) {
            val cx = chameleonX * w
            val cy = chameleonY * h
            val radius = min(w, h) * 0.08f
            drawChameleon(cx, cy, radius, chameleonColor)
        }

        // Draw seeker tap indicator
        seekerTap?.let { tap ->
            // Crosshair
            val lineLen = 30f
            drawLine(Color.White, Offset(tap.x - lineLen, tap.y), Offset(tap.x + lineLen, tap.y), strokeWidth = 3f)
            drawLine(Color.White, Offset(tap.x, tap.y - lineLen), Offset(tap.x, tap.y + lineLen), strokeWidth = 3f)
            drawCircle(Color.Red, radius = 20f, center = tap, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
        }
    }
}

/**
 * Draw background pattern (stripes + circles)
 */
private fun DrawScope.drawBackgroundPattern(color: Color, w: Float, h: Float) {
    // Diagonal stripes
    val stripeWidth = w * 0.06f
    val stripeSpacing = w * 0.12f
    var x = -h
    while (x < w + h) {
        val path = Path().apply {
            moveTo(x, 0f)
            lineTo(x + stripeWidth, 0f)
            lineTo(x + stripeWidth - h, h)
            lineTo(x - h, h)
            close()
        }
        drawPath(path, color.copy(alpha = 0.3f), style = Fill)
        x += stripeSpacing
    }

    // Circles pattern
    val circleR = w * 0.04f
    val spacing = w * 0.15f
    var cy = spacing
    while (cy < h) {
        var cx = spacing
        while (cx < w) {
            drawCircle(color.copy(alpha = 0.2f), radius = circleR, center = Offset(cx, cy))
            cx += spacing
        }
        cy += spacing
    }
}

/**
 * Draw cute chameleon character
 */
private fun DrawScope.drawChameleon(cx: Float, cy: Float, radius: Float, color: Color) {
    // Body (main circle)
    drawCircle(color, radius = radius, center = Offset(cx, cy))

    // Lighter belly
    drawCircle(
        color.copy(alpha = 0.7f),
        radius = radius * 0.7f,
        center = Offset(cx, cy + radius * 0.1f)
    )

    // Eyes (white with black pupils)
    val eyeOffset = radius * 0.35f
    val eyeRadius = radius * 0.22f
    // Left eye
    drawCircle(Color.White, radius = eyeRadius, center = Offset(cx - eyeOffset, cy - radius * 0.2f))
    drawCircle(Color.Black, radius = eyeRadius * 0.5f, center = Offset(cx - eyeOffset + 2f, cy - radius * 0.2f))
    // Right eye
    drawCircle(Color.White, radius = eyeRadius, center = Offset(cx + eyeOffset, cy - radius * 0.2f))
    drawCircle(Color.Black, radius = eyeRadius * 0.5f, center = Offset(cx + eyeOffset + 2f, cy - radius * 0.2f))

    // Eye highlights
    drawCircle(Color.White, radius = eyeRadius * 0.25f, center = Offset(cx - eyeOffset - 1f, cy - radius * 0.25f))
    drawCircle(Color.White, radius = eyeRadius * 0.25f, center = Offset(cx + eyeOffset - 1f, cy - radius * 0.25f))

    // Smile
    val smilePath = Path().apply {
        moveTo(cx - radius * 0.25f, cy + radius * 0.15f)
        quadraticBezierTo(cx, cy + radius * 0.4f, cx + radius * 0.25f, cy + radius * 0.15f)
    }
    drawPath(smilePath, Color.Black.copy(alpha = 0.6f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f))

    // Tail (curly)
    val tailPath = Path().apply {
        moveTo(cx + radius * 0.8f, cy)
        quadraticBezierTo(cx + radius * 1.4f, cy - radius * 0.3f, cx + radius * 1.2f, cy + radius * 0.2f)
        quadraticBezierTo(cx + radius * 1.0f, cy + radius * 0.5f, cx + radius * 1.3f, cy + radius * 0.3f)
    }
    drawPath(tailPath, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.12f))

    // Blush (pink cheeks)
    drawCircle(Color(0xFFFF8A80).copy(alpha = 0.4f), radius = radius * 0.12f, center = Offset(cx - radius * 0.55f, cy + radius * 0.05f))
    drawCircle(Color(0xFFFF8A80).copy(alpha = 0.4f), radius = radius * 0.12f, center = Offset(cx + radius * 0.55f, cy + radius * 0.05f))
}
