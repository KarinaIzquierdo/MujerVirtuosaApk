package com.mujer_virtuosa.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mujer_virtuosa.data.model.user.Address
import com.mujer_virtuosa.data.model.user.ProfileUser
import com.mujer_virtuosa.ui.components.UserMenuDrawer
import com.mujer_virtuosa.ui.components.UserMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.user.ProfileViewModel
import kotlinx.coroutines.launch

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    profileViewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val user = profileViewModel.user.value
    val addresses = profileViewModel.addresses.value
    val isLoading = profileViewModel.isLoading.value
    val error = profileViewModel.error.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showEditProfile by remember { mutableStateOf(false) }
    var editingAddress by remember { mutableStateOf<Address?>(null) }
    var showAddAddress by remember { mutableStateOf(false) }

    val saveSuccess = profileViewModel.saveSuccess.value
    val saveError = profileViewModel.saveError.value

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        authViewModel.loadProfile()
    }

    LaunchedEffect(saveSuccess, saveError) {
        if (saveSuccess) {
            Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show()
            showEditProfile = false
            editingAddress = null
            showAddAddress = false
            profileViewModel.resetSaveState()
        }
        saveError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            profileViewModel.resetSaveState()
        }
    }

    UserMenuDrawer(
        drawerState = drawerState,
        selectedItem = UserMenuItem.PROFILE,
        onNavigate = { item ->
            when (item) {
                UserMenuItem.CATALOG -> onNavigateToCatalog()
                UserMenuItem.CART -> onNavigateToCart()
                UserMenuItem.ORDERS -> onNavigateToOrders()
                UserMenuItem.PROGRESS -> onNavigateToProgress()
                else -> { /* ya estamos en perfil */ }
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
        ) {
            ProfileHeader(onMenuClick = { scope.launch { drawerState.open() } })

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
                    Text(
                        text = error,
                        color = Color(0xFFB00020),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> ProfileContent(
                    user = user,
                    addresses = addresses,
                    onEditProfile = { showEditProfile = true },
                    onEditAddress = { editingAddress = it },
                    onAddAddress = { showAddAddress = true }
                )
            }
        }
    }

    if (showEditProfile && user != null) {
        EditProfileDialog(
            user = user,
            isSaving = profileViewModel.isSaving.value,
            onDismiss = { showEditProfile = false },
            onSave = { name, email, phone ->
                profileViewModel.updateProfile(name, email, phone)
            }
        )
    }

    editingAddress?.let { address ->
        AddressDialog(
            title = "Editar dirección",
            initialAddress = address.address,
            isSaving = profileViewModel.isSaving.value,
            onDismiss = { editingAddress = null },
            onSave = { newAddress ->
                profileViewModel.updateAddress(address.id, newAddress, address.isDefault)
            }
        )
    }

    if (showAddAddress) {
        AddressDialog(
            title = "Agregar dirección",
            initialAddress = "",
            isSaving = profileViewModel.isSaving.value,
            onDismiss = { showAddAddress = false },
            onSave = { newAddress ->
                profileViewModel.createAddress(newAddress)
            }
        )
    }
}

@Composable
private fun ProfileHeader(onMenuClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = VerdeOscuro,
                modifier = Modifier.size(28.dp)
            )
        }
    }
    Text(
        text = "PERFIL",
        fontFamily = FontFamily.Serif,
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold,
        color = VerdeOscuro,
        modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
    )
}

@Composable
private fun ProfileContent(
    user: ProfileUser?,
    addresses: List<Address>,
    onEditProfile: () -> Unit,
    onEditAddress: (Address) -> Unit,
    onAddAddress: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        // Tarjeta de datos personales + direcciones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBeige)
                .padding(20.dp)
        ) {
            ProfileField("Nombre:", user?.name ?: "-")
            Spacer(modifier = Modifier.height(14.dp))
            ProfileField("Correo electronico:", user?.email ?: "-")
            Spacer(modifier = Modifier.height(14.dp))
            ProfileField("Numero de telefono:", user?.phone ?: "-")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onEditProfile,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeOscuro,
                        contentColor = Crema
                    ),
                    shape = RoundedCornerShape(50.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Text("Editar", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            HorizontalDivider(
                color = VerdeOscuro,
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 14.dp)
            )

            Text(
                text = "Mis direcciones",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (addresses.isEmpty()) {
                Text(
                    text = "No tienes direcciones registradas",
                    color = TextoSuave,
                    fontSize = 13.sp
                )
            } else {
                addresses.forEach { address ->
                    AddressCard(
                        address = address,
                        onEdit = { onEditAddress(address) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onAddAddress) {
                    Text(
                        text = "Agregar dirección",
                        color = VerdeOscuro,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = VerdeOscuro,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta Configuraciones
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardBeige)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Configuraciones",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = VerdeOscuro,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeOscuro
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = VerdeOscuro,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun AddressCard(
    address: Address,
    onEdit: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VerdeOscuro)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = address.address,
            fontSize = 14.sp,
            color = Crema,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onEdit,
            colors = ButtonDefaults.buttonColors(
                containerColor = CardBeige,
                contentColor = VerdeOscuro
            ),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text("Editar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EditProfileDialog(
    user: ProfileUser,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, phone: String?) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Crema,
        title = {
            Text(
                text = "Editar perfil",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileTextField(value = name, onValueChange = { name = it }, label = "Nombre")
                ProfileTextField(value = email, onValueChange = { email = it }, label = "Correo electrónico")
                ProfileTextField(value = phone, onValueChange = { phone = it }, label = "Teléfono")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name.trim(), email.trim(), phone.trim().ifBlank { null })
                },
                enabled = name.isNotBlank() && email.isNotBlank() && !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    contentColor = Crema
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = VerdeOscuro)
            }
        }
    )
}

@Composable
private fun AddressDialog(
    title: String,
    initialAddress: String,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (address: String) -> Unit
) {
    var address by remember { mutableStateOf(initialAddress) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Crema,
        title = {
            Text(
                text = title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        },
        text = {
            ProfileTextField(
                value = address,
                onValueChange = { address = it },
                label = "Dirección"
            )
        },
        confirmButton = {
            Button(
                onClick = { onSave(address.trim()) },
                enabled = address.isNotBlank() && !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    contentColor = Crema
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = VerdeOscuro)
            }
        }
    )
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdeOscuro,
            unfocusedBorderColor = VerdeOscuro,
            focusedLabelColor = VerdeOscuro,
            unfocusedLabelColor = VerdeOscuro,
            focusedTextColor = VerdeOscuro,
            unfocusedTextColor = VerdeOscuro
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
