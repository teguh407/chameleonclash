package com.chameleonclash.game.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chameleonclash.game.engine.GameEngine
import com.chameleonclash.game.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    var state by mutableStateOf(GameState())
        private set

    var seekerTap by mutableStateOf<Offset?>(null)
        private set

    var hint by mutableStateOf<String?>(null)
        private set

    // Start new game
    fun startGame(p1Name: String, p2Name: String) {
        state = GameState(
            phase = GamePhase.ROLE_SWAP,
            player1Name = p1Name.ifBlank { "Player 1" },
            player2Name = p2Name.ifBlank { "Player 2" },
            currentHider = p1Name.ifBlank { "Player 1" },
            currentSeeker = p2Name.ifBlank { "Player 2" },
            currentHiderIndex = 0
        )
    }

    // Begin hiding phase
    fun startHiding() {
        val bgSet = BG_SETS[state.round - 1]
        val pos = GameEngine.randomPosition()
        state = state.copy(
            phase = GamePhase.HIDING,
            bgColor = bgSet.mainColor,
            bgPattern = state.round,
            chameleonX = pos.first,
            chameleonY = pos.second,
            selectedColor = Color.White,
            colorMatchScore = 0,
            timeLeft = 15,
            seekerTap = null,
        )
        seekerTap = null
        startTimer()
    }

    // Select paint color
    fun selectColor(color: Color) {
        val score = GameEngine.colorMatchScore(color, state.bgColor)
        state = state.copy(selectedColor = color, colorMatchScore = score)
    }

    // Seeker taps
    fun seekerTap(x: Float, y: Float, screenW: Float, screenH: Float) {
        if (state.phase != GamePhase.SEEKING) return
        val tx = x / screenW
        val ty = y / screenH
        seekerTap = Offset(x, y)
        val score = GameEngine.seekerScore(x, y, state.chameleonX, state.chameleonY, screenW, screenH)
        val h = GameEngine.getHint(tx, ty, state.chameleonX, state.chameleonY)
        hint = h
        state = state.copy(seekerTap = Pair(tx, ty), seekerScore = score)
    }

    // Confirm seeker tap
    fun confirmSeek() {
        val result = RoundResult(
            colorMatchScore = state.colorMatchScore,
            seekerScore = state.seekerScore,
            hiderName = state.currentHider,
            seekerName = state.currentSeeker,
            hiddenPosition = Pair(state.chameleonX, state.chameleonY),
            seekerTap = state.seekerTap
        )

        // Calculate points
        val hiderPoints = state.colorMatchScore + GameEngine.timeBonus(state.timeLeft, 15)
        val seekerPoints = state.seekerScore

        val newP1Score = if (state.currentHiderIndex == 0) {
            state.player1Score + hiderPoints
        } else {
            state.player1Score + seekerPoints
        }
        val newP2Score = if (state.currentHiderIndex == 0) {
            state.player2Score + seekerPoints
        } else {
            state.player2Score + hiderPoints
        }

        state = state.copy(
            phase = GamePhase.ROUND_RESULT,
            player1Score = newP1Score,
            player2Score = newP2Score,
            lastRoundResult = result
        )
    }

    // Next round
    fun nextRound() {
        if (state.round >= state.maxRounds) {
            state = state.copy(phase = GamePhase.GAME_OVER)
            return
        }

        val nextHiderIndex = if (state.currentHiderIndex == 0) 1 else 0
        val nextHider = if (nextHiderIndex == 0) state.player1Name else state.player2Name
        val nextSeeker = if (nextHiderIndex == 0) state.player2Name else state.player1Name

        state = state.copy(
            round = state.round + 1,
            currentHiderIndex = nextHiderIndex,
            currentHider = nextHider,
            currentSeeker = nextSeeker,
            phase = GamePhase.ROLE_SWAP,
            seekerTap = null,
        )
        seekerTap = null
    }

    // Back to menu
    fun goToMenu() {
        state = GameState()
        seekerTap = null
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (state.timeLeft > 0 && state.phase == GamePhase.HIDING) {
                delay(1000L)
                state = state.copy(timeLeft = state.timeLeft - 1)
            }
            // Auto-switch to seeking when time runs out
            if (state.phase == GamePhase.HIDING) {
                state = state.copy(phase = GamePhase.SEEKING, timeLeft = 0)
            }
        }
    }
}
