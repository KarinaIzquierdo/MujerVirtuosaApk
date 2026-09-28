package com.mujer_virtuosa.ui.screens.admin

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mujer_virtuosa.R
import com.mujer_virtuosa.ui.components.AdminMenuDrawer
import com.mujer_virtuosa.ui.components.AdminMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.admin.DashboardViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun AdminHomeScreen(
    onLogout: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToSales: () -> Unit = {},
    onNavigateToUsers: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    dashboardViewModel: DashboardViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val stats = dashboardViewModel.stats.value
    val isLoading = dashboardViewModel.isLoading.value
    val error = dashboardViewModel.error.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        dashboardViewModel.loadStats()
        authViewModel.loadProfile()
    }

    AdminMenuDrawer(
        drawerState = drawerState,
        selectedItem = AdminMenuItem.HOME,
        onNavigate = { item ->
            when (item) {
                AdminMenuItem.CATALOG -> onNavigateToCatalog()
                AdminMenuItem.SALES -> onNavigateToSales()
                AdminMenuItem.USERS -> onNavigateToUsers()
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
    ) {
        // Header con imagen de cascada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.android_compact_26),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )

            // Menú hamburguesa
            Box(modifier = Modifier.statusBarsPadding()) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Crema,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Text(
                text = "Panel de\nAdministración",
                fontFamily = FontFamily.Serif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Crema,
                lineHeight = 36.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjetas de estadísticas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                isLoading -> CircularProgressIndicator(
                    color = VerdeOscuro,
                    modifier = Modifier.padding(top = 48.dp)
                )
                error != null -> Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                stats != null -> Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        StatCard(
                            icon = Icons.Default.Paid,
                            iconTint = Color(0xFFB8860B),
                            label = "Ventas totales",
                            value = stats.totalSales.toPriceFormat(),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.ShoppingCart,
                            iconTint = VerdeOscuro,
                            label = "Pedidos",
                            value = stats.totalOrders.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        StatCard(
                            icon = Icons.Default.Inventory2,
                            iconTint = Color(0xFF8B5A2B),
                            label = "Productos",
                            value = stats.totalProducts.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = Icons.Default.Groups,
                            iconTint = Color(0xFF5B7FA6),
                            label = "Clientes",
                            value = stats.totalCustomers.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EFD9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF7A7466)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        }
    }
}

// Formato de precio estilo colombiano: $220.000
private fun Double.toPriceFormat(): String {
    val formatted = String.format(Locale.US, "%,.0f", this)
    return "$" + formatted.replace(',', '.')
}
