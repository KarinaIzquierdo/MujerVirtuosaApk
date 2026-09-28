package com.mujer_virtuosa.ui.screens.user

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.mujer_virtuosa.data.model.user.Order
import com.mujer_virtuosa.ui.components.UserMenuDrawer
import com.mujer_virtuosa.ui.components.UserMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.user.CartViewModel
import com.mujer_virtuosa.ui.viewmodel.user.OrdersViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun OrdersScreen(
    onLogout: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    ordersViewModel: OrdersViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoading = ordersViewModel.isLoading.value
    val error = ordersViewModel.error.value
    val activeOrders = ordersViewModel.activeOrders
    val pastOrders = ordersViewModel.pastOrders

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var detailOrder by remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(Unit) {
        ordersViewModel.loadOrders()
        authViewModel.loadProfile()
    }

    UserMenuDrawer(
        drawerState = drawerState,
        selectedItem = UserMenuItem.ORDERS,
        onNavigate = { item ->
            when (item) {
                UserMenuItem.CATALOG -> onNavigateToCatalog()
                UserMenuItem.CART -> onNavigateToCart()
                UserMenuItem.PROGRESS -> onNavigateToProgress()
                UserMenuItem.PROFILE -> onNavigateToProfile()
                else -> { /* ya estamos en pedidos o pendiente */ }
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
            OrdersHeader(onMenuClick = { scope.launch { drawerState.open() } })

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
                else -> OrdersList(
                    activeOrders = activeOrders,
                    pastOrders = pastOrders,
                    onTrackClick = { detailOrder = it },
                    onDetailsClick = { detailOrder = it },
                    onBuyAgainClick = { order ->
                        order.items?.forEach { item ->
                            item.product?.let { product ->
                                cartViewModel.addToCart(product.id, item.quantity)
                            }
                        }
                        Toast.makeText(
                            context,
                            "Productos agregados al carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    detailOrder?.let { order ->
        OrderDetailsDialog(
            order = order,
            onDismiss = { detailOrder = null }
        )
    }
}

@Composable
private fun OrdersHeader(onMenuClick: () -> Unit) {
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
        text = "ENVIOS",
        fontFamily = FontFamily.Serif,
        fontSize = 34.sp,
        fontWeight = FontWeight.Bold,
        color = VerdeOscuro,
        modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun OrdersList(
    activeOrders: List<Order>,
    pastOrders: List<Order>,
    onTrackClick: (Order) -> Unit,
    onDetailsClick: (Order) -> Unit,
    onBuyAgainClick: (Order) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 8.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle(text = "Compras activas", showDot = true)
        }

        if (activeOrders.isEmpty()) {
            item {
                Text(
                    text = "No tienes compras activas",
                    color = TextoSuave,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        } else {
            items(activeOrders) { order ->
                ActiveOrderCard(
                    order = order,
                    onTrackClick = { onTrackClick(order) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionTitle(text = "Compras pasadas", showDot = false)
        }

        if (pastOrders.isEmpty()) {
            item {
                Text(
                    text = "No tienes compras pasadas",
                    color = TextoSuave,
                    fontSize = 14.sp
                )
            }
        } else {
            items(pastOrders) { order ->
                PastOrderCard(
                    order = order,
                    onDetailsClick = { onDetailsClick(order) },
                    onBuyAgainClick = { onBuyAgainClick(order) }
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String, showDot: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeOscuro
        )
        if (showDot) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(VerdeOscuro)
            )
        }
    }
}

@Composable
private fun ActiveOrderCard(
    order: Order,
    onTrackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .border(BorderStroke(2.dp, VerdeOscuro), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        OrderField("Número de pedido.", order.orderNumber)
        OrderField("Fecha de compra.", formatDate(order.createdAt))
        OrderField("Total.", formatPrice(order.total))
        OrderField("Estado actual.", statusLabel(order.status))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OrderField(
                label = "Fecha estimada.",
                value = order.estimatedDate ?: "-",
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onTrackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    contentColor = Crema
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text("Seguimiento", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun PastOrderCard(
    order: Order,
    onDetailsClick: () -> Unit,
    onBuyAgainClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .border(BorderStroke(2.dp, VerdeOscuro), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            OrderField("Número de pedido.", order.orderNumber)
            OrderField("Fecha.", formatDate(order.createdAt))
            OrderField("Total.", formatPrice(order.total))
            OrderField("Estado final.", statusLabel(order.status))
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onDetailsClick,
                shape = RoundedCornerShape(50.dp),
                border = BorderStroke(1.dp, VerdeOscuro),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = VerdeOscuro
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text("Ver detalles", fontSize = 13.sp)
            }
            Button(
                onClick = onBuyAgainClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    contentColor = Crema
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text("Comprar de nuevo", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun OrderField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(vertical = 2.dp)) {
        Text(
            text = "$label ",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = VerdeOscuro
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = VerdeOscuro
        )
    }
}

@Composable
private fun OrderDetailsDialog(
    order: Order,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Crema,
        title = {
            Text(
                text = "Pedido ${order.orderNumber}",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        },
        text = {
            Column {
                OrderField("Estado.", statusLabel(order.status))
                OrderField("Fecha de compra.", formatDate(order.createdAt))
                OrderField("Fecha estimada.", order.estimatedDate ?: "-")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Productos",
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                order.items?.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(
                            text = "${item.product?.name ?: "Producto"} x${item.quantity}",
                            fontSize = 14.sp,
                            color = VerdeOscuro,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = formatPrice(item.subtotal),
                            fontSize = 14.sp,
                            color = VerdeOscuro
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OrderField("Total.", formatPrice(order.total))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = VerdeOscuro)
            }
        }
    )
}

private fun statusLabel(status: String?): String {
    return when (status) {
        "pending" -> "Pendiente"
        "processing" -> "En proceso"
        "shipped" -> "Enviado"
        "delivered" -> "Entregado"
        "cancelled" -> "Cancelado"
        else -> status ?: "-"
    }
}

private fun formatDate(dateTime: String?): String {
    return dateTime?.take(10) ?: "-"
}

private fun formatPrice(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    format.maximumFractionDigits = 0
    return format.format(price)
}
