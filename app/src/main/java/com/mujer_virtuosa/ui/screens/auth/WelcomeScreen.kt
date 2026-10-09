package com.mujer_virtuosa.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mujer_virtuosa.R
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.CremaTranslucido
import com.mujer_virtuosa.ui.theme.VerdeOscuro

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToCatalog: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.shampoomujervirtuosa),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Barra superior
            WelcomeTopBar(
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToRegister = onNavigateToRegister
            )

            // Contenido centrado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 80.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                WelcomeCard(onNavigateToCatalog = onNavigateToCatalog)
            }
        }
    }
}

@Composable
private fun WelcomeTopBar(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeOscuro.copy(alpha = 0.95f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Mujer Virtuosa",
            modifier = Modifier.height(48.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 30.dp)
        ) {
            WelcomeNavButton(text = "Registrarse", onClick = onNavigateToRegister)
            WelcomeNavButton(text = "Iniciar sesión", onClick = onNavigateToLogin)
        }
    }
}

@Composable
private fun WelcomeNavButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Crema,
            contentColor = VerdeOscuro
        ),
        shape = RoundedCornerShape(32.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 14.dp,
            vertical = 8.dp
        )
    ) {
        Text(text = text, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun WelcomeCard(onNavigateToCatalog: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp, bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(CremaTranslucido)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "La naturaleza tiene la respuesta que tu cabello estaba buscando.",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeOscuro,
            textAlign = TextAlign.Center,
            lineHeight = 27.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNavigateToCatalog,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeOscuro,
                contentColor = Crema
            ),
            shape = RoundedCornerShape(50.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 36.dp,
                vertical = 14.dp
            )
        ) {
            Text(
                text = "Descubrir productos",
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
