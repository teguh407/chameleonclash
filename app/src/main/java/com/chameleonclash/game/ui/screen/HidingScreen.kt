package com.chameleonclash.game.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chameleonclash.game.model.PAINT_COLORS

@Composable
fun HidingScreen(
    bgColor: Color,
    selectedColor: Color,
    timeLeft: Int,
    colorMatchScore: Int,
    chameleonX: Float,
    chameleonY: Float,
    onColorSelected: (Color) -> Unit,
    onTimeUp: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Game canvas
        com.chameleonclash.game.engine.GameCanvas(
            bgColor = bgColor,
            patternColor = bgColor.copy(alpha = 0.3f),
            chameleonColor = selectedColor,
            chameleonX = chameleonX,
            chameleonY = chameleonY,
            showChameleon = true,
            isHidingPhase = true,
            seekerTap = null,
            hint = null,
            onTap = { _, _ -> },
            modifier = Modifier.fillMaxSize()
        )

        // Top HUD
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (timeLeft <= 5) Color(0xFFE53935) else Color.Black.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    "⏰ $timeLeft",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            }

            Spacer(Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f))
            ) {
                val pct = colorMatchScore
                val matchColor = when {
                    pct >= 80 -> Color(0xFF4CAF50)
                    pct >= 50 -> Color(0xFFFFB300)
                    else -> Color(0xFFE53935)
                }
                Text(
                    "Match: $pct% ${if (pct >= 80) "🔥" else if (pct >= 50) "😊" else "😅"}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 14.sp, fontWeight = FontWeight.Medium, color = matchColor
                )
            }
        }

        // Bottom color palette
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "🎨 Pilih warna!",
                        fontSize = 14.sp, fontWeight = FontWeight.Medium,
                        color = Color.White, modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Color grid - 5 columns
                    val rows = PAINT_COLORS.chunked(5)
                    rows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { color ->
                                val isSelected = color == selectedColor
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .then(
                                            if (isSelected) Modifier.border(3.dp, Color.White, CircleShape)
                                            else Modifier.border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                        )
                                        .clickable { onColorSelected(color) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Text("✓", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}
