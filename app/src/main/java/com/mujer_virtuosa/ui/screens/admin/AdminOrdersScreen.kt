package com.mujer_virtuosa.ui.screens.admin

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mujer_virtuosa.data.model.admin.AdminOrder
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.admin.OrdersViewModel
import java.util.Locale

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun AdminOrdersScreen(
    onBack: () -> Unit = {},
    ordersViewModel: OrdersViewModel = viewModel()
) {
    val orders = ordersViewModel.orders.value
    val isLoading = ordersViewModel.isLoading.value
    val error = ordersViewModel.error.value

    LaunchedEffect(Unit) {
        ordersViewModel.loadOrders()
    }

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
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = VerdeOscuro
                )
            }
            Text(
                text = "Todos los pedidos",
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        }

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
            orders.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay pedidos", color = TextoSuave)
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    OrderCard(order)
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: AdminOrder) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "#${order.orderNumber}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier.weight(1f)
            )
            StatusBadge(order.status)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text("Cliente", fontSize = 11.sp, color = TextoSuave)
                Text(order.customer, fontSize = 13.sp, color = VerdeOscuro)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Fecha", fontSize = 11.sp, color = TextoSuave)
                Text(order.date, fontSize = 13.sp, color = VerdeOscuro)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text("Productos", fontSize = 11.sp, color = TextoSuave)
                Text("${order.productsCount}", fontSize = 13.sp, color = VerdeOscuro)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Total", fontSize = 11.sp, color = TextoSuave)
                Text(
                    order.total.toPriceFormat(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )
            }
        }
    }
}

// Formato de precio estilo colombiano: $35.000
private fun Double.toPriceFormat(): String {
    val formatted = String.format(Locale.US, "%,.0f", this)
    return "$" + formatted.replace(',', '.')
}
