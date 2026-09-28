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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mujer_virtuosa.R
import com.mujer_virtuosa.ui.components.AuthField
import com.mujer_virtuosa.ui.components.AuthHeader
import com.mujer_virtuosa.ui.components.SocialButton
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.Marron
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirmationVisible by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    val isLoading = authViewModel.isLoading.value
    val error = authViewModel.error.value
    val isLoggedIn = authViewModel.isLoggedIn.value

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
    ) {
        // Hojas decorativas tenues
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .size(150.dp)
                .alpha(0.07f)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 24.dp)
                .size(110.dp)
                .alpha(0.07f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AuthHeader()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Bienvenida a\nMujer Virtuosa",
                    fontFamily = FontFamily.Serif,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Descubre productos naturales para el\ncuidado de tu cabello.",
                    fontSize = 14.sp,
                    color = Color(0xFF7A7466),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthField(
                    label = "Nombre",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Ingresa tu nombre",
                    icon = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthField(
                    label = "Correo electrónico",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Ingresa tu correo electrónico",
                    icon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthField(
                    label = "Contraseña",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Ingresa tu contraseña",
                    icon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) {
                                        R.drawable.ic_visibility_off
                                    } else {
                                        R.drawable.ic_visibility
                                    }
                                ),
                                contentDescription = if (passwordVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                },
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthField(
                    label = "Confirmar contraseña",
                    value = passwordConfirmation,
                    onValueChange = { passwordConfirmation = it },
                    placeholder = "Confirma tu contraseña",
                    icon = Icons.Default.Lock,
                    visualTransformation = if (passwordConfirmationVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordConfirmationVisible = !passwordConfirmationVisible
                        }) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordConfirmationVisible) {
                                        R.drawable.ic_visibility_off
                                    } else {
                                        R.drawable.ic_visibility
                                    }
                                ),
                                contentDescription = if (passwordConfirmationVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                },
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                val displayError = localError ?: error
                if (displayError != null) {
                    Text(
                        text = displayError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Button(
                    onClick = {
                        localError = when {
                            name.isBlank() || email.isBlank() ||
                                password.isBlank() || passwordConfirmation.isBlank() ->
                                "Completa todos los campos"
                            password != passwordConfirmation ->
                                "Las contraseñas no coinciden"
                            password.length < 8 ->
                                "La contraseña debe tener al menos 8 caracteres"
                            else -> null
                        }
                        if (localError == null) {
                            authViewModel.register(name, email, password, passwordConfirmation)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Marron,
                        contentColor = Crema
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Crema,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SocialButton(
                        text = "Google",
                        iconRes = R.drawable.ic_google,
                        onClick = { /* TODO: registro con Google */ },
                        modifier = Modifier.weight(1f)
                    )
                    SocialButton(
                        text = "Facebook",
                        iconRes = R.drawable.ic_facebook,
                        onClick = { /* TODO: registro con Facebook */ },
                        modifier = Modifier.weight(1f)
                    )
                }

                TextButton(onClick = onNavigateToLogin) {
                    Text("¿Ya tienes cuenta? Inicia sesión", color = VerdeOscuro)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
