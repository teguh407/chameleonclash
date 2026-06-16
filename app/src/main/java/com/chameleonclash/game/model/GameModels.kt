package com.chameleonclash.game.model

import androidx.compose.ui.graphics.Color

// Game phases
enum class GamePhase {
    MENU,
    ROLE_SWAP,      // "Pass to Player X"
    HIDING,          // Hider paints chameleon
    SEEKING,         // Seeker taps to find
    ROUND_RESULT,    // Show result of round
    GAME_OVER        // Final scores
}

enum class PlayerRole { HIDER, SEEKER }

data class RoundResult(
    val colorMatchScore: Int,    // 0-100 how well hider matched
    val seekerScore: Int,        // 0-100 how accurately seeker found
    val hiderName: String,
    val seekerName: String,
    val hiddenPosition: Pair<Float, Float>,
    val seekerTap: Pair<Float, Float>?
)

data class GameState(
    val phase: GamePhase = GamePhase.MENU,
    val round: Int = 1,
    val maxRounds: Int = 5,
    val player1Name: String = "Player 1",
    val player2Name: String = "Player 2",
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val currentHider: String = "Player 1",
    val currentSeeker: String = "Player 2",
    val currentHiderIndex: Int = 0,  // 0 = P1 hides, 1 = P2 hides

    // Hiding phase
    val bgColor: Color = Color.Red,
    val bgPattern: Int = 0,       // pattern index
    val chameleonX: Float = 0.5f, // normalized position
    val chameleonY: Float = 0.5f,
    val selectedColor: Color = Color.White,
    val colorMatchScore: Int = 0,
    val timeLeft: Int = 15,

    // Seeking phase
    val seekerTap: Pair<Float, Float>? = null,
    val seekerScore: Int = 0,

    // Result
    val lastRoundResult: RoundResult? = null
)

// Color palette for painting
val PAINT_COLORS = listOf(
    Color(0xFFE53935), // Red
    Color(0xFFD81B60), // Pink
    Color(0xFF8E24AA), // Purple
    Color(0xFF5E35B1), // Deep Purple
    Color(0xFF3949AB), // Indigo
    Color(0xFF1E88E5), // Blue
    Color(0xFF039BE5), // Light Blue
    Color(0xFF00ACC1), // Cyan
    Color(0xFF00897B), // Teal
    Color(0xFF43A047), // Green
    Color(0xFF7CB342), // Light Green
    Color(0xFFFDD835), // Yellow
    Color(0xFFFFB300), // Amber
    Color(0xFFFF8F00), // Orange
    Color(0xFFF4511E), // Deep Orange
    Color(0xFF6D4C41), // Brown
    Color(0xFF757575), // Grey
    Color(0xFF546E7A), // Blue Grey
    Color(0xFFFFFFFF), // White
    Color(0xFF212121), // Black
)

// Background color sets (each round uses one)
data class BgSet(
    val mainColor: Color,
    val patternColor: Color,
    val name: String
)

val BG_SETS = listOf(
    BgSet(Color(0xFFE53935), Color(0xFFC62828), "Merah"),
    BgSet(Color(0xFF1E88E5), Color(0xFF1565C0), "Biru"),
    BgSet(Color(0xFF43A047), Color(0xFF2E7D32), "Hijau"),
    BgSet(Color(0xFFFFB300), Color(0xFFF57F17), "Kuning"),
    BgSet(Color(0xFF8E24AA), Color(0xFF6A1B9A), "Ungu"),
    BgSet(Color(0xFF00897B), Color(0xFF00695C), "Teal"),
    BgSet(Color(0xFFF4511E), Color(0xFFD84315), "Oranye"),
    BgSet(Color(0xFF5E35B1), Color(0xFF4527A0), "Deep Purple"),
    BgSet(Color(0xFF3949AB), Color(0xFF283593), "Indigo"),
    BgSet(Color(0xFF6D4C41), Color(0xFF4E342E), "Coklat"),
)

// Chameleon shapes for variety
enum class ChameleonShape { ROUND, STAR, DIAMOND, TRIANGLE, SQUARE }
