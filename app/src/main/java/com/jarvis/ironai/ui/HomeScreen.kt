package com.jarvis.ironai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

private val bg = Color(0xFF050B16)
private val cyan = Color(0xFF00D9FF)
private val panel = Color(0xFF102338)

@Composable
fun JarvisHome(
    status: String,
    transcript: String,
    onListen: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))

            Text(
                "J.A.R.V.I.S.",
                color = cyan,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "PERSONAL AI SYSTEM",
                color = Color.LightGray,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(45.dp))

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .background(panel, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .background(Color(0xFF123D59), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "J",
                        color = cyan,
                        fontSize = 100.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Text(
                status,
                color = cyan,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = onListen,
                colors = ButtonDefaults.buttonColors(
                    containerColor = cyan,
                    contentColor = bg
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ACTIVATE JARVIS")
            }

            Spacer(Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = panel
                )
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("LAST COMMAND", color = cyan)
                    Spacer(Modifier.height(10.dp))
                    Text(transcript, color = Color.White)
                }
            }

            Spacer(Modifier.weight(1f))

            Text(
                "ONLINE • OFFLINE READY • V1.0",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}
