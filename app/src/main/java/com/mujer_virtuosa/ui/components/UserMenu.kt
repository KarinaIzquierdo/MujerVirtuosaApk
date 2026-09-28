package com.mujer_virtuosa.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mujer_virtuosa.R
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import kotlinx.coroutines.launch

enum class UserMenuItem {
    CATALOG, CART, ORDERS, PROGRESS, PROFILE
}

private val VerdeFila = Color(0xFF2E4A3E)

// Menú lateral del usuario: ocupa la mitad de la pantalla
@Composable
fun UserMenuDrawer(
    drawerState: DrawerState,
    selectedItem: UserMenuItem? = null,
    onNavigate: (UserMenuItem) -> Unit = {},
    onLogout: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.5f).dp),
                drawerContainerColor = VerdeOscuro,
                drawerShape = RoundedCornerShape(bottomEnd = 28.dp)
            ) {
                UserMenuContent(
                    selectedItem = selectedItem,
                    onItemClick = { item ->
                        scope.launch { drawerState.close() }
                        onNavigate(item)
                    },
                    onLogout = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    }
                )
            }
        },
        content = content
    )
}

@Composable
private fun UserMenuContent(
    selectedItem: UserMenuItem?,
    onItemClick: (UserMenuItem) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(56.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Mujer Virtuosa",
            modifier = Modifier.height(80.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            UserMenuEntry(
                icon = Icons.Default.Sell,
                label = "Catálogo",
                selected = selectedItem == UserMenuItem.CATALOG,
                onClick = { onItemClick(UserMenuItem.CATALOG) }
            )
            UserMenuEntry(
                icon = Icons.Default.ShoppingCart,
                label = "Carrito",
                selected = selectedItem == UserMenuItem.CART,
                onClick = { onItemClick(UserMenuItem.CART) }
            )
            UserMenuEntry(
                icon = Icons.AutoMirrored.Filled.List,
                label = "Mis pedidos",
                selected = selectedItem == UserMenuItem.ORDERS,
                onClick = { onItemClick(UserMenuItem.ORDERS) }
            )
            UserMenuEntry(
                icon = Icons.Default.TrendingUp,
                label = "Progreso",
                selected = selectedItem == UserMenuItem.PROGRESS,
                onClick = { onItemClick(UserMenuItem.PROGRESS) }
            )
            UserMenuEntry(
                icon = Icons.Default.Person,
                label = "Perfil",
                selected = selectedItem == UserMenuItem.PROFILE,
                onClick = { onItemClick(UserMenuItem.PROFILE) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Crema,
                contentColor = VerdeOscuro
            ),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar sesión", fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun UserMenuEntry(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) VerdeFila.copy(alpha = 1.35f) else VerdeFila
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Crema,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Crema,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Crema,
            modifier = Modifier.size(22.dp)
        )
    }
}
