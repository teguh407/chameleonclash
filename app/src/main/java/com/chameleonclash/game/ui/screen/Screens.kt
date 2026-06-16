package com.chameleonclash.game.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(onStart: (String, String) -> Unit) {
    var p1 by remember { mutableStateOf("") }
    var p2 by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF2E7D32)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            ChameleonLogo()
            Spacer(Modifier.height(16.dp))
            Text("CHAMELEON", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text("CLASH!", fontSize = 42.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDD835))
            Text("Cat & Seek!", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                value = p1, onValueChange = { p1 = it },
                label = { Text("Player 1 (Hider)") },
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFDD835), unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFFDD835), unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = p2, onValueChange = { p2 = it },
                label = { Text("Player 2 (Seeker)") },
                singleLine = true, modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFDD835), unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFFDD835), unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                )
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onStart(p1, p2) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD835))
            ) {
                Text("MULAI!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
            Spacer(Modifier.height(16.dp))
            Text("5 ronde | Saling bergantian jadi Hider & Seeker", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ChameleonLogo() {
    val colors = listOf(Color(0xFF4CAF50), Color(0xFFFFB300), Color(0xFFE53935), Color(0xFF1E88E5), Color(0xFF8E24AA))
    var idx by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { while (true) { kotlinx.coroutines.delay(800); idx = (idx + 1) % colors.size } }

    Canvas(modifier = Modifier.size(100.dp)) {
        val cx = size.width / 2; val cy = size.height / 2; val r = size.width / 2 - 8f
        drawCircle(colors[idx], radius = r, center = Offset(cx, cy))
        drawCircle(Color.White, radius = r * 0.25f, center = Offset(cx - r * 0.3f, cy - r * 0.15f))
        drawCircle(Color.Black, radius = r * 0.12f, center = Offset(cx - r * 0.28f, cy - r * 0.15f))
        drawCircle(Color.White, radius = r * 0.25f, center = Offset(cx + r * 0.3f, cy - r * 0.15f))
        drawCircle(Color.Black, radius = r * 0.12f, center = Offset(cx + r * 0.32f, cy - r * 0.15f))
    }
}

@Composable
fun RoleSwapScreen(hiderName: String, seekerName: String, round: Int, maxRounds: Int, onReady: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1565C0)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text("RONDE $round / $maxRounds", fontSize = 18.sp, color = Color.White.copy(alpha = 0.7f))
            Spacer(Modifier.height(24.dp))
            Text("Serahkan HP ke:", fontSize = 20.sp, color = Color.White)
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("\uD83C\uDFAF", fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(seekerName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                    Text("jadi SEEKER!", fontSize = 16.sp, color = Color(0xFF1565C0).copy(alpha = 0.7f))
                    Spacer(Modifier.height(12.dp))
                    Text("Jangan liat layar $hiderName!", fontSize = 14.sp, color = Color(0xFF757575))
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("$hiderName: Siap-siap cat chameleon!", fontSize = 14.sp, color = Color(0xFFFDD835))
            Spacer(Modifier.height(32.dp))
            Button(onClick = onReady, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD835))) {
                Text("SIAP!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
            }
        }
    }
}

@Composable
fun SeekingScreen(bgColor: Color, chameleonColor: Color, chameleonX: Float, chameleonY: Float,
                  hint: String?, seekerTap: Offset?, onTap: (Float, Float) -> Unit, onConfirm: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        com.chameleonclash.game.engine.GameCanvas(
            bgColor = bgColor, patternColor = bgColor.copy(alpha = 0.3f),
            chameleonColor = chameleonColor, chameleonX = chameleonX, chameleonY = chameleonY,
            showChameleon = true, isHidingPhase = false, seekerTap = seekerTap, hint = hint,
            onTap = onTap, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f))) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("CARI CHAMELEON!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Tap di mana kamu lihat chameleon!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                }
            }
            hint?.let {
                Spacer(Modifier.height(8.dp))
                Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))) {
                    Text(it, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        if (seekerTap != null) {
            Button(onClick = onConfirm, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD835))) {
                Text("KONFIRMASI", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
        }
    }
}

@Composable
fun RoundResultScreen(result: com.chameleonclash.game.model.RoundResult, onNext: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.fillMaxWidth().padding(24.dp), shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E))) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("HASIL RONDE", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFDD835))
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${result.hiderName} (Hider)", fontSize = 14.sp, color = Color.White)
                    Text("${result.colorMatchScore}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                }
                Text("Color match", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${result.seekerName} (Seeker)", fontSize = 14.sp, color = Color.White)
                    Text("${result.seekerScore}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E88E5))
                }
                Text("Seeker accuracy", fontSize = 11.sp, color = Color.White.copy(alpha = 0.5f))
                Spacer(Modifier.height(20.dp))
                val w = if (result.colorMatchScore > result.seekerScore) "${result.hiderName} menang!" else "${result.seekerName} menang!"
                Text(w, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFDD835))
                Spacer(Modifier.height(20.dp))
                Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD835))) {
                    Text("LANJUT", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
                }
            }
        }
    }
}

@Composable
fun GameOverScreen(p1Name: String, p1Score: Int, p2Name: String, p2Score: Int, onMenu: () -> Unit) {
    val winner = if (p1Score > p2Score) p1Name else if (p2Score > p1Score) p2Name else "DRAW"
    val wc = if (p1Score > p2Score) Color(0xFF4CAF50) else if (p2Score > p1Score) Color(0xFF1E88E5) else Color(0xFFFDD835)

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A2E)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text("\uD83C\uDFC6", fontSize = 64.sp)
            Spacer(Modifier.height(16.dp))
            Text("GAME OVER!", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDD835))
            Spacer(Modifier.height(24.dp))
            if (winner == "DRAW") Text("SERI!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            else Text("$winner MENANG!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = wc)
            Spacer(Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(p1Name, fontSize = 18.sp, color = Color.White)
                        Text("$p1Score", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.1f))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(p2Name, fontSize = 18.sp, color = Color.White)
                        Text("$p2Score", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E88E5))
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
            Button(onClick = onMenu, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD835))) {
                Text("MENU", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
            }
        }
    }
}
