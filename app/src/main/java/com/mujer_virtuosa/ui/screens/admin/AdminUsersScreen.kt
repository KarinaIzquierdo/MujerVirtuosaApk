package com.mujer_virtuosa.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mujer_virtuosa.data.model.admin.AdminUser
import com.mujer_virtuosa.data.model.admin.PaginationMeta
import com.mujer_virtuosa.ui.components.AdminMenuDrawer
import com.mujer_virtuosa.ui.components.AdminMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.admin.UsersViewModel
import kotlinx.coroutines.launch

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun AdminUsersScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToSales: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    usersViewModel: UsersViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val data = usersViewModel.data.value
    val isLoading = usersViewModel.isLoading.value
    val error = usersViewModel.error.value
    val search = usersViewModel.search.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        usersViewModel.loadUsers()
    }

    AdminMenuDrawer(
        drawerState = drawerState,
        selectedItem = AdminMenuItem.USERS,
        onNavigate = { item ->
            when (item) {
                AdminMenuItem.HOME -> onNavigateToHome()
                AdminMenuItem.CATALOG -> onNavigateToCatalog()
                AdminMenuItem.SALES -> onNavigateToSales()
                AdminMenuItem.PROFILE -> onNavigateToProfile()
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
                text = "Gestión de Usuarios",
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                // Stats
                UserStatCard(
                    icon = { Icon(Icons.Default.Group, null, tint = Crema, modifier = Modifier.size(26.dp)) },
                    label = "Total de usuarios",
                    value = "${data?.totalUsers ?: 0}"
                )
                Spacer(modifier = Modifier.height(12.dp))
                UserStatCard(
                    icon = { Icon(Icons.Default.CheckCircle, null, tint = Crema, modifier = Modifier.size(26.dp)) },
                    label = "Usuarios activos",
                    value = "${data?.activeUsers ?: 0}"
                )
                Spacer(modifier = Modifier.height(12.dp))
                UserStatCard(
                    icon = { Icon(Icons.Default.PersonAdd, null, tint = Crema, modifier = Modifier.size(26.dp)) },
                    label = "Nuevos usuarios (mes)",
                    value = "${data?.newUsersMonth ?: 0}"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buscador
                OutlinedTextField(
                    value = search,
                    onValueChange = { usersViewModel.onSearchChange(it) },
                    placeholder = { Text("Buscar", color = TextoSuave) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, null, tint = TextoSuave)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBeige,
                        unfocusedContainerColor = CardBeige,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tabla de usuarios
                when {
                    isLoading -> Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VerdeOscuro)
                    }
                    error != null -> Text(error, color = Color(0xFFB00020))
                    data != null -> UsersTable(
                        users = data.users,
                        pagination = data.pagination,
                        onPageChange = { usersViewModel.loadUsers(it) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun UserStatCard(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(VerdeOscuro),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 13.sp, color = TextoSuave)
            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        }
    }
}

@Composable
private fun UsersTable(
    users: List<AdminUser>,
    pagination: PaginationMeta,
    onPageChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Column {
                // Encabezados
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserCell("Usuario", 140.dp, bold = true)
                    UserCell("Correo electrónico", 160.dp, bold = true)
                    UserCell("Teléfono", 110.dp, bold = true)
                    UserCell("Fecha de\nregistro", 90.dp, bold = true)
                    UserCell("Último acceso", 90.dp, bold = true)
                    UserCell("Acciones", 60.dp, bold = true)
                }

                users.forEach { user ->
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar + nombre
                        Row(
                            modifier = Modifier.width(140.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = user.avatar?.toEmulatorUrl(),
                                contentDescription = user.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD8D0BC))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                user.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = VerdeOscuro,
                                maxLines = 2
                            )
                        }
                        UserCell(user.email, 160.dp)
                        UserCell(user.phone ?: "-", 110.dp)
                        UserCell(user.registeredAt, 90.dp)
                        UserCell(user.lastLogin ?: "Nunca", 90.dp)
                        Box(modifier = Modifier.width(60.dp)) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Acciones",
                                tint = VerdeOscuro,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Paginación
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Mostrando ${pagination.from} a ${pagination.to} de ${pagination.total} usuarios",
                fontSize = 11.sp,
                color = TextoSuave,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { onPageChange(pagination.currentPage - 1) },
                enabled = pagination.currentPage > 1,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    null,
                    tint = if (pagination.currentPage > 1) VerdeOscuro else TextoSuave
                )
            }

            // Números de página (ventana de 3 alrededor de la actual)
            val start = (pagination.currentPage - 1).coerceAtLeast(1)
            val end = (start + 2).coerceAtMost(pagination.lastPage)
            for (page in start..end) {
                PageButton(
                    page = page,
                    selected = page == pagination.currentPage,
                    onClick = { onPageChange(page) }
                )
            }

            IconButton(
                onClick = { onPageChange(pagination.currentPage + 1) },
                enabled = pagination.currentPage < pagination.lastPage,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    null,
                    tint = if (pagination.currentPage < pagination.lastPage) VerdeOscuro else TextoSuave
                )
            }
        }
    }
}

@Composable
private fun PageButton(page: Int, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .size(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) VerdeOscuro else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "$page",
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Crema else VerdeOscuro
        )
    }
}

@Composable
private fun UserCell(text: String, width: Dp, bold: Boolean = false) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = VerdeOscuro,
        maxLines = 2,
        modifier = Modifier.width(width)
    )
}

// El backend devuelve URLs con 127.0.0.1; desde el emulador el host es 10.0.2.2
private fun String.toEmulatorUrl(): String {
    return replace("127.0.0.1", "10.0.2.2").replace("localhost", "10.0.2.2")
}
