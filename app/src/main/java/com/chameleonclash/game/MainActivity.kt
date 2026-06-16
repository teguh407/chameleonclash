package com.chameleonclash.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chameleonclash.game.model.GamePhase
import com.chameleonclash.game.ui.GameViewModel
import com.chameleonclash.game.ui.screen.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChameleonClashApp()
        }
    }
}

@Composable
fun ChameleonClashApp(vm: GameViewModel = viewModel()) {
    val state = vm.state

    when (state.phase) {
        GamePhase.MENU -> {
            MenuScreen(onStart = { p1, p2 -> vm.startGame(p1, p2) })
        }

        GamePhase.ROLE_SWAP -> {
            RoleSwapScreen(
                hiderName = state.currentHider,
                seekerName = state.currentSeeker,
                round = state.round,
                maxRounds = state.maxRounds,
                onReady = { vm.startHiding() }
            )
        }

        GamePhase.HIDING -> {
            HidingScreen(
                bgColor = state.bgColor,
                selectedColor = state.selectedColor,
                timeLeft = state.timeLeft,
                colorMatchScore = state.colorMatchScore,
                chameleonX = state.chameleonX,
                chameleonY = state.chameleonY,
                onColorSelected = { vm.selectColor(it) },
                onTimeUp = { }
            )
        }

        GamePhase.SEEKING -> {
            val config = LocalConfiguration.current
            val density = LocalDensity.current
            val screenW = with(density) { config.screenWidthDp.dp.toPx() }
            val screenH = with(density) { config.screenHeightDp.dp.toPx() }

            SeekingScreen(
                bgColor = state.bgColor,
                chameleonColor = state.selectedColor,
                chameleonX = state.chameleonX,
                chameleonY = state.chameleonY,
                hint = vm.hint,
                seekerTap = vm.seekerTap,
                onTap = { x, y -> vm.seekerTap(x, y, screenW, screenH) },
                onConfirm = { vm.confirmSeek() }
            )
        }

        GamePhase.ROUND_RESULT -> {
            state.lastRoundResult?.let { result ->
                RoundResultScreen(result = result, onNext = { vm.nextRound() })
            }
        }

        GamePhase.GAME_OVER -> {
            GameOverScreen(
                p1Name = state.player1Name, p1Score = state.player1Score,
                p2Name = state.player2Name, p2Score = state.player2Score,
                onMenu = { vm.goToMenu() }
            )
        }
    }
}
