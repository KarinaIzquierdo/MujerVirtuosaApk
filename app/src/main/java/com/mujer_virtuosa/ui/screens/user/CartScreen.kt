package com.mujer_virtuosa.ui.screens.user

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mujer_virtuosa.data.model.user.CartItem
import com.mujer_virtuosa.ui.components.UserMenuDrawer
import com.mujer_virtuosa.ui.components.UserMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.user.CartViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val CardBeige = Color(0xFFEDE4C8)
private val ContenedorMarron = Color(0xFF7A6B4F)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun CartScreen(
    onLogout: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val cart = cartViewModel.cart.value
    val isLoading = cartViewModel.isLoading.value
    val error = cartViewModel.error.value
    val isCheckingOut = cartViewModel.isCheckingOut.value
    val checkoutError = cartViewModel.checkoutError.value
    val checkoutSuccess = cartViewModel.checkoutSuccess.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        cartViewModel.loadCart()
        authViewModel.loadProfile()
    }

    UserMenuDrawer(
        drawerState = drawerState,
        selectedItem = UserMenuItem.CART,
        onNavigate = { item ->
            when (item) {
                UserMenuItem.CATALOG -> onNavigateToCatalog()
                UserMenuItem.ORDERS -> onNavigateToOrders()
                UserMenuItem.PROGRESS -> onNavigateToProgress()
                UserMenuItem.PROFILE -> onNavigateToProfile()
                else -> { /* pantallas pendientes */ }
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
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
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
                text = "MI CARRITO",
                fontFamily = FontFamily.Serif,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
            )

            // Contenido
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ContenedorMarron)
            ) {
                when {
                    isLoading -> CircularProgressIndicator(
                        color = Crema,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    error != null -> Text(
                        text = error,
                        color = Color(0xFFFFB4B4),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                    cart?.items.isNullOrEmpty() -> Text(
                        "Tu carrito está vacío",
                        color = Crema,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    else -> LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(cart!!.items!!) { item ->
                            CartItemCard(
                                item = item,
                                onToggleSelected = {
                                    cartViewModel.toggleSelected(item.id, item.quantity, !item.selected)
                                },
                                onDecrease = {
                                    cartViewModel.updateQuantity(item.id, item.quantity - 1)
                                },
                                onIncrease = {
                                    cartViewModel.updateQuantity(item.id, item.quantity + 1)
                                }
                            )
                        }
                    }
                }
            }

            // Barra inferior: total + hacer compra
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBeige)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                checkoutSuccess?.let {
                    Text(
                        text = it,
                        color = VerdeOscuro,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                checkoutError?.let {
                    Text(
                        text = it,
                        color = Color(0xFFB00020),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Valor: ${formatPrice(cart?.total ?: 0.0)}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedButton(
                        onClick = { cartViewModel.checkout() },
                        enabled = !isCheckingOut && (cart?.total ?: 0.0) > 0,
                        shape = RoundedCornerShape(50.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, VerdeOscuro),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = VerdeOscuro
                        )
                    ) {
                        if (isCheckingOut) {
                            CircularProgressIndicator(
                                color = VerdeOscuro,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Hacer compra",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VerdeOscuro
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onToggleSelected: () -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(12.dp)
    ) {
        IconButton(onClick = onToggleSelected) {
            Icon(
                imageVector = if (item.selected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = "Seleccionar",
                tint = VerdeOscuro,
                modifier = Modifier.size(26.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product?.name ?: "Producto",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = VerdeOscuro,
                maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Cantidad: ${item.quantity}",
                    fontSize = 12.sp,
                    color = VerdeOscuro
                )
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Disminuir",
                        tint = VerdeOscuro,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Aumentar",
                        tint = VerdeOscuro,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Text(
            text = formatPrice(item.subtotal),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = VerdeOscuro,
            modifier = Modifier.padding(end = 10.dp)
        )

        AsyncImage(
            model = item.product?.image?.replace("127.0.0.1", "10.0.2.2")?.replace("localhost", "10.0.2.2"),
            contentDescription = item.product?.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 56.dp, height = 72.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

private fun formatPrice(price: Double): String {
    val format = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))
    format.maximumFractionDigits = 0
    return "$${format.format(price)}"
}
