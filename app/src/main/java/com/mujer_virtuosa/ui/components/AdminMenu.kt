package com.mujer_virtuosa.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
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

enum class AdminMenuItem {
    HOME, CATALOG, SALES, USERS, PROFILE, PROGRESS
}

// Menú lateral del admin: ocupa la mitad de la pantalla
@Composable
fun AdminMenuDrawer(
    drawerState: DrawerState,
    selectedItem: AdminMenuItem? = null,
    onNavigate: (AdminMenuItem) -> Unit = {},
    onLogout: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.55f).dp),
                drawerContainerColor = VerdeOscuro,
                drawerShape = RoundedCornerShape(bottomEnd = 28.dp)
            ) {
                AdminMenuContent(
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
private fun AdminMenuContent(
    selectedItem: AdminMenuItem?,
    onItemClick: (AdminMenuItem) -> Unit,
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

        Spacer(modifier = Modifier.height(32.dp))

        AdminMenuEntry(
            icon = Icons.Default.Home,
            label = "Inicio",
            selected = selectedItem == AdminMenuItem.HOME,
            onClick = { onItemClick(AdminMenuItem.HOME) }
        )
        MenuDivider()
        AdminMenuEntry(
            icon = Icons.Default.Sell,
            label = "Catálogo",
            selected = selectedItem == AdminMenuItem.CATALOG,
            onClick = { onItemClick(AdminMenuItem.CATALOG) }
        )
        MenuDivider()
        AdminMenuEntry(
            icon = Icons.Default.Leaderboard,
            label = "Ventas",
            selected = selectedItem == AdminMenuItem.SALES,
            onClick = { onItemClick(AdminMenuItem.SALES) }
        )
        MenuDivider()
        AdminMenuEntry(
            icon = Icons.Default.Groups,
            label = "Usuarios",
            selected = selectedItem == AdminMenuItem.USERS,
            onClick = { onItemClick(AdminMenuItem.USERS) }
        )
        MenuDivider()
        AdminMenuEntry(
            icon = Icons.Default.Person,
            label = "Perfil",
            selected = selectedItem == AdminMenuItem.PROFILE,
            onClick = { onItemClick(AdminMenuItem.PROFILE) }
        )
        MenuDivider()
        AdminMenuEntry(
            icon = Icons.Default.TrendingUp,
            label = "Progreso",
            selected = selectedItem == AdminMenuItem.PROGRESS,
            onClick = { onItemClick(AdminMenuItem.PROGRESS) }
        )

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
private fun AdminMenuEntry(
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
                if (selected) Color(0xFF2E4A3E) else Color.Transparent
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

@Composable
private fun MenuDivider() {
    HorizontalDivider(
        color = Crema.copy(alpha = 0.12f),
        thickness = 1.dp
    )
}
