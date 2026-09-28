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
import com.mujer_virtuosa.data.model.admin.AdminProgress
import com.mujer_virtuosa.data.model.admin.PaginationMeta
import com.mujer_virtuosa.ui.components.AdminMenuDrawer
import com.mujer_virtuosa.ui.components.AdminMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.admin.ProgressViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun AdminProgressScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToSales: () -> Unit = {},
    onNavigateToUsers: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    progressViewModel: ProgressViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val data = progressViewModel.data.value
    val isLoading = progressViewModel.isLoading.value
    val error = progressViewModel.error.value
    val search = progressViewModel.search.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        progressViewModel.loadProgress()
    }

    AdminMenuDrawer(
        drawerState = drawerState,
        selectedItem = AdminMenuItem.PROGRESS,
        onNavigate = { item ->
            when (item) {
                AdminMenuItem.HOME -> onNavigateToHome()
                AdminMenuItem.CATALOG -> onNavigateToCatalog()
                AdminMenuItem.SALES -> onNavigateToSales()
                AdminMenuItem.USERS -> onNavigateToUsers()
                AdminMenuItem.PROFILE -> onNavigateToProfile()
                else -> { /* ya estamos en progreso */ }
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
                text = "Progreso de usuarios",
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
                Row(modifier = Modifier.fillMaxWidth()) {
                    ProgressStatCard(
                        icon = { Icon(Icons.Default.Group, null, tint = Crema, modifier = Modifier.size(24.dp)) },
                        label = "Usuarios con progreso",
                        value = formatNumber(data?.usersWithProgress ?: 0),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    ProgressStatCard(
                        icon = { Icon(Icons.Default.CheckCircle, null, tint = Crema, modifier = Modifier.size(24.dp)) },
                        label = "Progresos registrados",
                        value = formatNumber(data?.totalProgresses ?: 0),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buscador
                OutlinedTextField(
                    value = search,
                    onValueChange = { progressViewModel.onSearchChange(it) },
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

                // Tabla de progreso
                when {
                    isLoading -> Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VerdeOscuro)
                    }
                    error != null -> Text(error, color = Color(0xFFB00020))
                    data != null -> ProgressTable(
                        progresses = data.progresses,
                        pagination = data.pagination,
                        onPageChange = { progressViewModel.loadProgress(it) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun formatNumber(value: Int): String {
    return NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO")).format(value)
}

@Composable
private fun ProgressStatCard(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(VerdeOscuro),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(label, fontSize = 10.sp, color = TextoSuave, maxLines = 2)
            Text(
                value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        }
    }
}

@Composable
private fun ProgressTable(
    progresses: List<AdminProgress>,
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
                    ProgressCell("Usuario", 130.dp, bold = true)
                    ProgressCell("Producto", 140.dp, bold = true)
                    ProgressCell("Antes/Después", 110.dp, bold = true)
                    ProgressCell("Fecha", 80.dp, bold = true)
                    ProgressCell("Testimonio", 150.dp, bold = true)
                    ProgressCell("Acciones", 50.dp, bold = true)
                }

                progresses.forEach { progress ->
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar + nombre
                        Row(
                            modifier = Modifier.width(130.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = progress.user.avatar?.toEmulatorUrl(),
                                contentDescription = progress.user.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD8D0BC))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                progress.user.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = VerdeOscuro,
                                maxLines = 2
                            )
                        }

                        // Producto: imagen + nombre
                        Row(
                            modifier = Modifier.width(140.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = progress.product.image?.toEmulatorUrl(),
                                contentDescription = progress.product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFD8D0BC))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                progress.product.name,
                                fontSize = 11.sp,
                                color = VerdeOscuro,
                                maxLines = 2
                            )
                        }

                        // Antes / Después
                        Row(
                            modifier = Modifier.width(110.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = progress.beforeImage?.toEmulatorUrl(),
                                contentDescription = "Antes",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFD8D0BC))
                            )
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                                tint = TextoSuave, modifier = Modifier.size(14.dp)
                            )
                            AsyncImage(
                                model = progress.afterImage?.toEmulatorUrl(),
                                contentDescription = "Después",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFD8D0BC))
                            )
                        }

                        // Fecha
                        Column(modifier = Modifier.width(80.dp)) {
                            Text(progress.date, fontSize = 11.sp, color = VerdeOscuro)
                            Text(progress.time, fontSize = 10.sp, color = TextoSuave)
                        }

                        // Testimonio
                        Text(
                            progress.testimony ?: "--",
                            fontSize = 11.sp,
                            color = VerdeOscuro,
                            maxLines = 3,
                            modifier = Modifier.width(150.dp)
                        )

                        // Acciones
                        Box(modifier = Modifier.width(50.dp)) {
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
                "Mostrando ${pagination.from} a ${pagination.to} de ${pagination.total} progresos",
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
private fun ProgressCell(text: String, width: Dp, bold: Boolean = false) {
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
