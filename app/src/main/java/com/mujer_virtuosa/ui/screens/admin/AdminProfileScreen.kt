package com.mujer_virtuosa.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mujer_virtuosa.data.model.admin.AdminDevice
import com.mujer_virtuosa.data.model.admin.AdminProfile
import com.mujer_virtuosa.ui.components.AdminMenuDrawer
import com.mujer_virtuosa.ui.components.AdminMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.admin.ProfileViewModel
import kotlinx.coroutines.launch

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)
private val VerdeBadge = Color(0xFFD5E8D4)
private val Rojo = Color(0xFFC62828)

@Composable
fun AdminProfileScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToSales: () -> Unit = {},
    onNavigateToUsers: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onViewOrders: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val profile = profileViewModel.profile.value
    val isLoading = profileViewModel.isLoading.value
    val error = profileViewModel.error.value
    val actionLoading = profileViewModel.actionLoading.value
    val actionMessage = profileViewModel.actionMessage.value
    val actionError = profileViewModel.actionError.value
    val devices = profileViewModel.devices.value
    val devicesLoading = profileViewModel.devicesLoading.value
    val accountDeactivated = profileViewModel.accountDeactivated.value

    var showEditDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showDevicesDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    // Si la cuenta fue desactivada, cerrar sesión
    LaunchedEffect(accountDeactivated) {
        if (accountDeactivated) {
            authViewModel.logout()
            onLogout()
        }
    }

    // Al guardar con éxito, cerrar el diálogo y limpiar el estado
    LaunchedEffect(actionMessage) {
        if (actionMessage != null) {
            showEditDialog = false
            showPasswordDialog = false
            profileViewModel.clearActionState()
        }
    }

    AdminMenuDrawer(
        drawerState = drawerState,
        selectedItem = AdminMenuItem.PROFILE,
        onNavigate = { item ->
            when (item) {
                AdminMenuItem.HOME -> onNavigateToHome()
                AdminMenuItem.CATALOG -> onNavigateToCatalog()
                AdminMenuItem.SALES -> onNavigateToSales()
                AdminMenuItem.USERS -> onNavigateToUsers()
                AdminMenuItem.PROGRESS -> onNavigateToProgress()
                else -> { }
            }
        },
        onLogout = {
            authViewModel.logout()
            onLogout()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Crema)
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = VerdeOscuro,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Text(
                text = "Perfil",
                fontFamily = FontFamily.Serif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VerdeOscuro)
                }
                error != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(error, color = Color(0xFFB00020))
                }
                profile != null -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    ProfileCard(profile)

                    Spacer(modifier = Modifier.height(16.dp))

                    SecurityCard(
                        profile = profile,
                        onEditClick = { showEditDialog = true },
                        onViewDevices = {
                            profileViewModel.loadDevices()
                            showDevicesDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    QuickActionsCard(
                        onViewOrders = onViewOrders,
                        onResetPassword = { showPasswordDialog = true },
                        onDeactivate = { showDeactivateDialog = true }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Diálogo: editar perfil
    if (showEditDialog && profile != null) {
        EditProfileDialog(
            profile = profile,
            isLoading = actionLoading,
            error = actionError,
            message = actionMessage,
            onDismiss = {
                showEditDialog = false
                profileViewModel.clearActionState()
            },
            onSave = { name, email, phone ->
                profileViewModel.updateProfile(name, email, phone)
            }
        )
    }

    // Diálogo: cambiar contraseña
    if (showPasswordDialog) {
        ChangePasswordDialog(
            isLoading = actionLoading,
            error = actionError,
            message = actionMessage,
            onDismiss = {
                showPasswordDialog = false
                profileViewModel.clearActionState()
            },
            onSave = { current, new, confirmation ->
                profileViewModel.changePassword(current, new, confirmation)
            }
        )
    }

    // Diálogo: dispositivos activos
    if (showDevicesDialog) {
        DevicesDialog(
            devices = devices,
            isLoading = devicesLoading,
            onDismiss = { showDevicesDialog = false }
        )
    }

    // Diálogo: confirmar desactivación
    if (showDeactivateDialog) {
        DeactivateDialog(
            isLoading = actionLoading,
            error = actionError,
            onDismiss = {
                showDeactivateDialog = false
                profileViewModel.clearActionState()
            },
            onConfirm = { profileViewModel.deactivateAccount() }
        )
    }
}

@Composable
private fun ProfileCard(profile: AdminProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(16.dp)
    ) {
        // Avatar + estado
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = profile.avatar?.toEmulatorUrl(),
                contentDescription = profile.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD8D0BC))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(VerdeBadge)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D32))
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text("Activo", fontSize = 11.sp, color = VerdeOscuro)
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Datos
        Column {
            Text(
                profile.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(VerdeBadge)
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(profile.roleLabel, fontSize = 11.sp, color = VerdeOscuro)
            }
            Spacer(modifier = Modifier.height(10.dp))
            ProfileInfoRow(Icons.Default.Email, profile.email)
            ProfileInfoRow(Icons.Default.Phone, profile.phone ?: "-")
            ProfileInfoRow(Icons.Default.DateRange, "Miembro desde: ${profile.memberSince}")
            ProfileInfoRow(Icons.Default.Info, "ID de usuario: #${profile.userCode}")
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VerdeOscuro,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 11.sp, color = VerdeOscuro)
    }
}

@Composable
private fun SecurityCard(
    profile: AdminProfile,
    onEditClick: () -> Unit,
    onViewDevices: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Lock, null,
                tint = VerdeOscuro, modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Seguridad",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier.weight(1f)
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, VerdeOscuro, RoundedCornerShape(8.dp))
                    .clickable(onClick = onEditClick)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Edit, null,
                    tint = VerdeOscuro, modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Editar", fontSize = 11.sp, color = VerdeOscuro)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Contraseña
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Contraseña", fontSize = 12.sp, color = VerdeOscuro)
            Text(
                "••••••••••••",
                fontSize = 12.sp,
                color = VerdeOscuro,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text("Última actualización", fontSize = 9.sp, color = TextoSuave)
                Text(
                    profile.passwordUpdatedAt ?: "-",
                    fontSize = 11.sp,
                    color = VerdeOscuro
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            color = Color(0x33FFFFFF)
        )

        // 2FA
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Autenticación en dos pasos",
                fontSize = 12.sp,
                color = VerdeOscuro,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.Shield, null,
                tint = VerdeOscuro, modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Activado", fontSize = 11.sp, color = VerdeOscuro)
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            color = Color(0x33FFFFFF)
        )

        // Dispositivos
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Dispositivos activos",
                fontSize = 12.sp,
                color = VerdeOscuro,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.Monitor, null,
                tint = VerdeOscuro, modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "${profile.activeDevices} dispositivos",
                fontSize = 11.sp,
                color = VerdeOscuro
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier.clickable(onClick = onViewDevices),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Ver dispositivos",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = VerdeOscuro
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                    tint = VerdeOscuro, modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickActionsCard(
    onViewOrders: () -> Unit,
    onResetPassword: () -> Unit,
    onDeactivate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Bolt, null,
                tint = VerdeOscuro, modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Acciones rápidas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionButton(
            icon = Icons.Default.Key,
            label = "Restablecer contraseña",
            onClick = onResetPassword
        )
        Spacer(modifier = Modifier.height(10.dp))
        QuickActionButton(
            icon = Icons.Default.PersonRemove,
            label = "Desactivar usuario",
            labelColor = Rojo,
            iconColor = Rojo,
            onClick = onDeactivate
        )
        Spacer(modifier = Modifier.height(10.dp))
        QuickActionButton(
            icon = Icons.Default.Inventory2,
            label = "Ver pedidos del usuario",
            onClick = onViewOrders
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    labelColor: Color = VerdeOscuro,
    iconColor: Color = VerdeOscuro,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, VerdeOscuro.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            label,
            fontSize = 13.sp,
            color = labelColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = VerdeOscuro,
            modifier = Modifier.size(18.dp)
        )
    }
}

// El backend devuelve URLs con 127.0.0.1; desde el emulador el host es 10.0.2.2
private fun String.toEmulatorUrl(): String {
    return replace("127.0.0.1", "10.0.2.2").replace("localhost", "10.0.2.2")
}

@Composable
private fun EditProfileDialog(
    profile: AdminProfile,
    isLoading: Boolean,
    error: String?,
    message: String?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var email by remember { mutableStateOf(profile.email) }
    var phone by remember { mutableStateOf(profile.phone ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar perfil", color = VerdeOscuro, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                DialogField("Nombre", name) { name = it }
                DialogField("Correo electrónico", email) { email = it }
                DialogField("Teléfono", phone) { phone = it }
                if (error != null) {
                    Text(error, color = Rojo, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
                if (message != null) {
                    Text(message, color = VerdeOscuro, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, email, phone) },
                enabled = !isLoading && name.isNotBlank() && email.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = VerdeOscuro)
                } else {
                    Text("Guardar", color = VerdeOscuro)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = TextoSuave)
            }
        },
        containerColor = Crema
    )
}

@Composable
private fun ChangePasswordDialog(
    isLoading: Boolean,
    error: String?,
    message: String?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var current by remember { mutableStateOf("") }
    var new by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Restablecer contraseña", color = VerdeOscuro, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                DialogField("Contraseña actual", current, isPassword = true) { current = it }
                DialogField("Nueva contraseña", new, isPassword = true) { new = it }
                DialogField("Confirmar contraseña", confirmation, isPassword = true) { confirmation = it }
                if (error != null) {
                    Text(error, color = Rojo, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
                if (message != null) {
                    Text(message, color = VerdeOscuro, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(current, new, confirmation) },
                enabled = !isLoading && current.isNotBlank() && new.length >= 8 && new == confirmation
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = VerdeOscuro)
                } else {
                    Text("Guardar", color = VerdeOscuro)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = TextoSuave)
            }
        },
        containerColor = Crema
    )
}

@Composable
private fun DevicesDialog(
    devices: List<AdminDevice>,
    isLoading: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dispositivos activos", color = VerdeOscuro, fontWeight = FontWeight.Bold) },
        text = {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VerdeOscuro)
                }
            } else if (devices.isEmpty()) {
                Text("No hay dispositivos activos.", color = TextoSuave, fontSize = 13.sp)
            } else {
                Column {
                    devices.forEach { device ->
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Monitor, null,
                                tint = VerdeOscuro, modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(device.device, fontSize = 13.sp, color = VerdeOscuro)
                                Text(
                                    "${device.ipAddress ?: "-"} · ${device.lastActivity}",
                                    fontSize = 11.sp,
                                    color = TextoSuave
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = VerdeOscuro)
            }
        },
        containerColor = Crema
    )
}

@Composable
private fun DeactivateDialog(
    isLoading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Desactivar usuario", color = Rojo, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    "¿Estás seguro? Tu cuenta quedará desactivada, se cerrarán todas las sesiones y no podrás volver a iniciar sesión.",
                    fontSize = 13.sp,
                    color = VerdeOscuro
                )
                if (error != null) {
                    Text(error, color = Rojo, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isLoading) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Rojo)
                } else {
                    Text("Desactivar", color = Rojo)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextoSuave)
            }
        },
        containerColor = Crema
    )
}

@Composable
private fun DialogField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdeOscuro,
            unfocusedBorderColor = VerdeOscuro.copy(alpha = 0.4f),
            focusedLabelColor = VerdeOscuro,
            cursorColor = VerdeOscuro
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
