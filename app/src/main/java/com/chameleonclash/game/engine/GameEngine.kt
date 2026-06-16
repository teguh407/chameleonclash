package com.chameleonclash.game.engine

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

object GameEngine {

    /**
     * Calculate color distance (Euclidean in RGB space)
     * Returns 0-255 (0 = perfect match)
     */
    fun colorDistance(c1: Color, c2: Color): Float {
        val r1 = c1.red * 255; val g1 = c1.green * 255; val b1 = c1.blue * 255
        val r2 = c2.red * 255; val g2 = c2.green * 255; val b2 = c2.blue * 255
        return sqrt(
            (r1 - r2) * (r1 - r2) +
            (g1 - g2) * (g1 - g2) +
            (b1 - b2) * (b1 - b2)
        ).toFloat()
    }

    /**
     * Score color match: 0-100
     * 0 = completely wrong, 100 = perfect match
     */
    fun colorMatchScore(painted: Color, target: Color): Int {
        val dist = colorDistance(painted, target)
        // Max distance in RGB space is ~441 (sqrt(255^2 * 3))
        val maxDist = 441f
        val score = ((1f - dist / maxDist) * 100f).coerceIn(0f, 100f)
        return score.toInt()
    }

    /**
     * Score seeker accuracy: 0-100
     * Based on distance from tap to actual chameleon position
     */
    fun seekerScore(
        tapX: Float, tapY: Float,
        chameleonX: Float, chameleonY: Float,
        screenWidth: Float, screenHeight: Float
    ): Int {
        val dx = (tapX - chameleonX * screenWidth) / screenWidth
        val dy = (tapY - chameleonY * screenHeight) / screenHeight
        val dist = sqrt(dx * dx + dy * dy)
        // If tap is within 5% of screen size = perfect, beyond 50% = 0
        val score = ((1f - dist / 0.5f) * 100f).coerceIn(0f, 100f)
        return score.toInt()
    }

    /**
     * Generate random chameleon position (avoid edges)
     */
    fun randomPosition(): Pair<Float, Float> {
        return Pair(
            Random.nextFloat() * 0.6f + 0.2f,  // 0.2 - 0.8
            Random.nextFloat() * 0.6f + 0.2f
        )
    }

    /**
     * Get the closest palette color to a target color
     */
    fun closestPaletteColor(target: Color, palette: List<Color>): Color {
        return palette.minByOrNull { colorDistance(it, target) } ?: palette[0]
    }

    /**
     * Calculate time bonus: more time left = higher bonus
     */
    fun timeBonus(timeLeft: Int, maxTime: Int): Int {
        return ((timeLeft.toFloat() / maxTime) * 50).toInt()
    }

    /**
     * Get hint level for seeker based on distance
     * Returns "FREEZING", "COLD", "WARM", "HOT", "BLAZING"
     */
    fun getHint(tapX: Float, tapY: Float, targetX: Float, targetY: Float): String {
        val dx = tapX - targetX
        val dy = tapY - targetY
        val dist = sqrt(dx * dx + dy * dy)
        return when {
            dist < 0.05f -> "BLAZING 🔥"
            dist < 0.12f -> "HOT 🌡️"
            dist < 0.25f -> "WARM 😊"
            dist < 0.40f -> "COLD ❄️"
            else -> "FREEZING 🧊"
        }
    }
}
