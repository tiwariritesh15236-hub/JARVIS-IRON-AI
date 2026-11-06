package com.jarvis.ironai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF050B16)
private val Cyan = Color(0xFF00D9FF)
private val Panel = Color(0xFF101E30)

@Composable
fun JarvisHome(
    status: String,
    transcript: String,
    response: String,
    onActivate: () -> Unit
) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "J.A.R.V.I.S.",
                        color = Cyan,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "IRON ARTIFICIAL INTELLIGENCE",
                        color = Color.LightGray,
                        fontSize = 10.sp,
                        letterSpacing = 2.sp
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .border(3.dp, Cyan, CircleShape)
                            .padding(12.dp)
                            .border(1.dp, Color(0xFF287A9B), CircleShape)
                            .background(Panel, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "J",
                            color = Cyan,
                            fontSize = 112.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = status,
                        color = Cyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoCard("YOUR COMMAND", transcript)
                    InfoCard("JARVIS RESPONSE", response)

                    Button(
                        onClick = onActivate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Cyan,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "ACTIVATE JARVIS",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }

                    Text(
                        text = "VOICE CONTROL • ONLINE ASSISTANT",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                color = Cyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}
