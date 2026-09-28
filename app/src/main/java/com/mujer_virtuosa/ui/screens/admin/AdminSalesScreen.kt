package com.mujer_virtuosa.ui.screens.admin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mujer_virtuosa.data.model.admin.AdminOrder
import com.mujer_virtuosa.data.model.admin.ChartPoint
import com.mujer_virtuosa.ui.components.AdminMenuDrawer
import com.mujer_virtuosa.ui.components.AdminMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.admin.SalesViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.ceil

private val CardBeige = Color(0xFFEDE4C8)
private val Dorado = Color(0xFFC9A227)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun AdminSalesScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToUsers: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onViewAllOrders: () -> Unit = {},
    salesViewModel: SalesViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val sales = salesViewModel.sales.value
    val isLoading = salesViewModel.isLoading.value
    val error = salesViewModel.error.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        salesViewModel.loadSales()
    }

    AdminMenuDrawer(
        drawerState = drawerState,
        selectedItem = AdminMenuItem.SALES,
        onNavigate = { item ->
            when (item) {
                AdminMenuItem.HOME -> onNavigateToHome()
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
                text = "Ventas",
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
                sales != null -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    SalesStatCard(
                        icon = { Icon(Icons.Default.Paid, null, tint = Crema, modifier = Modifier.size(26.dp)) },
                        label = "Ventas totales",
                        value = sales.totalSales.toPriceFormat()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SalesStatCard(
                        icon = { Icon(Icons.Default.ShoppingBag, null, tint = Crema, modifier = Modifier.size(26.dp)) },
                        label = "Pedidos",
                        value = sales.totalOrders.toString()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SalesChartCard(points = sales.chartData)

                    Spacer(modifier = Modifier.height(16.dp))

                    RecentOrdersCard(orders = sales.recentOrders)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onViewAllOrders,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardBeige,
                            contentColor = VerdeOscuro
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    ) {
                        Text(
                            "Ver todos los pedidos",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SalesStatCard(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(horizontal = 20.dp, vertical = 18.dp),
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
private fun SalesChartCard(points: List<ChartPoint>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(16.dp)
    ) {
        Text(
            "Resumen de ventas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeOscuro
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Leyenda
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(22.dp)
                    .height(3.dp)
                    .background(VerdeOscuro)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Ventas (COP)", fontSize = 11.sp, color = VerdeOscuro)
            Spacer(modifier = Modifier.width(16.dp))
            Canvas(modifier = Modifier.width(22.dp).height(3.dp)) {
                drawLine(
                    color = Dorado,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text("Pedidos", fontSize = 11.sp, color = VerdeOscuro)
        }

        Spacer(modifier = Modifier.height(12.dp))

        SalesChart(points = points, modifier = Modifier.fillMaxWidth().height(200.dp))
    }
}

@Composable
private fun SalesChart(points: List<ChartPoint>, modifier: Modifier = Modifier) {
    if (points.isEmpty()) return

    val labelColor = TextoSuave.toArgb()
    val gridColor = Color(0x33FFFFFF)

    Canvas(modifier = modifier) {
        val leftPad = 34.dp.toPx()
        val rightPad = 34.dp.toPx()
        val topPad = 10.dp.toPx()
        val bottomPad = 26.dp.toPx()
        val chartW = size.width - leftPad - rightPad
        val chartH = size.height - topPad - bottomPad

        val maxSales = (points.maxOf { it.sales }).coerceAtLeast(1.0)
        val maxOrders = (points.maxOf { it.orders }).coerceAtLeast(1)
        val salesTop = ceil(maxSales / 2_000_000.0) * 2_000_000.0
        val ordersTop = (ceil(maxOrders / 20.0) * 20).toInt().coerceAtLeast(20)

        val textPaint = android.graphics.Paint().apply {
            color = labelColor
            textSize = 9.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }
        val leftPaint = android.graphics.Paint(textPaint).apply {
            textAlign = android.graphics.Paint.Align.RIGHT
        }
        val rightPaint = android.graphics.Paint(textPaint).apply {
            textAlign = android.graphics.Paint.Align.LEFT
        }

        // Grid + etiquetas Y (4 filas)
        for (i in 0..4) {
            val y = topPad + chartH * i / 4f
            drawLine(gridColor, Offset(leftPad, y), Offset(size.width - rightPad, y), 1f)

            val salesVal = salesTop * (4 - i) / 4
            val salesLabel = if (salesVal >= 1_000_000) {
                "${(salesVal / 1_000_000).toInt()}M"
            } else {
                "${(salesVal / 1_000).toInt()}K"
            }
            drawContext.canvas.nativeCanvas.drawText(
                salesLabel, leftPad - 6.dp.toPx(), y + 3.dp.toPx(), leftPaint
            )
            drawContext.canvas.nativeCanvas.drawText(
                "${ordersTop * (4 - i) / 4}",
                size.width - rightPad + 6.dp.toPx(), y + 3.dp.toPx(), rightPaint
            )
        }

        fun xAt(index: Int) = leftPad + chartW * index / (points.size - 1).coerceAtLeast(1)
        fun ySales(v: Double) = topPad + chartH * (1 - (v / salesTop)).toFloat()
        fun yOrders(v: Int) = topPad + chartH * (1 - v.toFloat() / ordersTop)

        // Área bajo la línea de ventas
        val fillPath = Path().apply {
            moveTo(xAt(0), topPad + chartH)
            points.forEachIndexed { i, p -> lineTo(xAt(i), ySales(p.sales)) }
            lineTo(xAt(points.size - 1), topPad + chartH)
            close()
        }
        drawPath(
            fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(VerdeOscuro.copy(alpha = 0.25f), Color.Transparent),
                startY = topPad,
                endY = topPad + chartH
            )
        )

        // Línea de ventas (sólida)
        val salesPath = Path().apply {
            points.forEachIndexed { i, p ->
                if (i == 0) moveTo(xAt(i), ySales(p.sales))
                else lineTo(xAt(i), ySales(p.sales))
            }
        }
        drawPath(salesPath, VerdeOscuro, style = Stroke(width = 2.5.dp.toPx()))

        // Línea de pedidos (punteada)
        val ordersPath = Path().apply {
            points.forEachIndexed { i, p ->
                if (i == 0) moveTo(xAt(i), yOrders(p.orders))
                else lineTo(xAt(i), yOrders(p.orders))
            }
        }
        drawPath(
            ordersPath, Dorado,
            style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
            )
        )

        // Etiquetas X cada ~7 puntos
        val step = (points.size / 4).coerceAtLeast(1)
        points.forEachIndexed { i, p ->
            if (i % step == 0 || i == points.size - 1) {
                drawContext.canvas.nativeCanvas.drawText(
                    p.date, xAt(i), size.height - 6.dp.toPx(), textPaint
                )
            }
        }
    }
}

@Composable
private fun RecentOrdersCard(orders: List<AdminOrder>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBeige)
            .padding(16.dp)
    ) {
        Text(
            "Últimos pedidos",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeOscuro
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Column {
                // Encabezados
                Row(modifier = Modifier.padding(vertical = 6.dp)) {
                    OrderCell("Pedido", 90.dp, bold = true)
                    OrderCell("Fecha", 110.dp, bold = true)
                    OrderCell("Cliente", 100.dp, bold = true)
                    OrderCell("Productos", 70.dp, bold = true)
                    OrderCell("Total", 80.dp, bold = true)
                    OrderCell("Estado", 90.dp, bold = true)
                    OrderCell("", 40.dp, bold = true)
                }
                orders.forEach { order ->
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OrderCell("#${order.orderNumber}", 90.dp)
                        OrderCell(order.date, 110.dp)
                        OrderCell(order.customer, 100.dp)
                        OrderCell("${order.productsCount} Productos", 70.dp)
                        OrderCell(order.total.toPriceFormat(), 80.dp)
                        Box(modifier = Modifier.width(90.dp)) {
                            StatusBadge(order.status)
                        }
                        Box(modifier = Modifier.width(40.dp)) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Ver pedido",
                                tint = VerdeOscuro,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCell(text: String, width: androidx.compose.ui.unit.Dp, bold: Boolean = false) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = VerdeOscuro,
        maxLines = 2,
        modifier = Modifier.width(width)
    )
}

@Composable
fun StatusBadge(status: String) {
    val (label, bg, fg) = when (status) {
        "delivered" -> Triple("Completado", Color(0xFFC8E6C9), Color(0xFF2E7D32))
        "shipped" -> Triple("Enviado", Color(0xFFB2DFDB), Color(0xFF00695C))
        "processing" -> Triple("Procesando", Color(0xFFFFF9C4), Color(0xFFF57F17))
        "cancelled" -> Triple("Cancelado", Color(0xFFFFCDD2), Color(0xFFC62828))
        else -> Triple("Pendiente", Color(0xFFE0E0E0), Color(0xFF616161))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(label, fontSize = 10.sp, color = fg, fontWeight = FontWeight.Medium)
    }
}

// Formato de precio estilo colombiano: $35.000
private fun Double.toPriceFormat(): String {
    val formatted = String.format(Locale.US, "%,.0f", this)
    return "$" + formatted.replace(',', '.')
}
