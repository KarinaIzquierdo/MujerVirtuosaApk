package com.mujer_virtuosa.ui.screens.user

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mujer_virtuosa.R
import com.mujer_virtuosa.data.model.product.Product
import com.mujer_virtuosa.ui.components.UserMenuDrawer
import com.mujer_virtuosa.ui.components.UserMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.product.ProductViewModel
import com.mujer_virtuosa.ui.viewmodel.user.CartViewModel
import android.widget.Toast
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)

@Composable
fun CatalogScreen(
    onLogout: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    productViewModel: ProductViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val products = productViewModel.products.value
    val isLoading = productViewModel.isLoading.value
    val error = productViewModel.error.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val context = LocalContext.current
    val addSuccess = cartViewModel.addSuccess.value
    val addError = cartViewModel.addError.value

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
        authViewModel.loadProfile()
    }

    // Feedback al agregar desde la tarjeta (el diálogo maneja su propio feedback)
    LaunchedEffect(addSuccess, addError) {
        if (selectedProduct == null) {
            if (addSuccess) {
                Toast.makeText(context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                cartViewModel.resetAddState()
            }
            addError?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                cartViewModel.resetAddState()
            }
        }
    }

    UserMenuDrawer(
        drawerState = drawerState,
        selectedItem = UserMenuItem.CATALOG,
        onNavigate = { item ->
            when (item) {
                UserMenuItem.CART -> onNavigateToCart()
                UserMenuItem.ORDERS -> onNavigateToOrders()
                UserMenuItem.PROGRESS -> onNavigateToProgress()
                UserMenuItem.PROFILE -> onNavigateToProfile()
                else -> { /* ya estamos en catálogo o pendiente */ }
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
            // Header con imagen de fondo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_inicio),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Oscurece la parte inferior para legibilidad
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.25f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                )
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 8.dp, top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Crema,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Catálogo de\nProductos",
                    fontFamily = FontFamily.Serif,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Crema,
                    lineHeight = 34.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 20.dp)
                )
            }

            // Contenido
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> CircularProgressIndicator(color = VerdeOscuro)
                    error != null -> Text(
                        text = error,
                        color = Color(0xFFB00020),
                        modifier = Modifier.padding(16.dp)
                    )
                    products.isEmpty() -> Text(
                        "No hay productos disponibles",
                        color = TextoSuave
                    )
                    else -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 20.dp,
                            bottom = 24.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onInfoClick = { selectedProduct = product },
                                onAddToCart = { cartViewModel.addToCart(product.id, 1) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de detalle del producto
    selectedProduct?.let { product ->
        ProductInfoDialog(
            product = product,
            cartViewModel = cartViewModel,
            onDismiss = {
                selectedProduct = null
                cartViewModel.resetAddState()
            }
        )
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onInfoClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBeige)
    ) {
        // Imagen con botón Info superpuesto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            AsyncImage(
                model = product.image?.toEmulatorUrl(),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Button(
                onClick = onInfoClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Crema,
                    contentColor = VerdeOscuro
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(horizontal = 22.dp, vertical = 4.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 18.dp)
                    .height(34.dp)
            ) {
                Text("Info", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }

        // Nombre y precio
        Column(
            modifier = Modifier.padding(
                start = 14.dp,
                end = 14.dp,
                top = 26.dp,
                bottom = 14.dp
            )
        ) {
            Text(
                text = product.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = VerdeOscuro,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formatPrice(product.price),
                fontSize = 14.sp,
                color = VerdeOscuro
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onAddToCart,
                enabled = product.stock > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    contentColor = Crema
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (product.stock > 0) "Agregar al carrito" else "Sin stock",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ProductInfoDialog(
    product: Product,
    cartViewModel: CartViewModel,
    onDismiss: () -> Unit
) {
    var quantity by remember(product.id) { mutableStateOf(1) }
    val isAdding = cartViewModel.isAdding.value
    val addError = cartViewModel.addError.value
    val addSuccess = cartViewModel.addSuccess.value

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Crema,
        title = {
            Column {
                AsyncImage(
                    model = product.image?.toEmulatorUrl(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = product.name,
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )
                Text(
                    text = formatPrice(product.price),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = VerdeOscuro
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                product.description?.takeIf { it.isNotBlank() }?.let {
                    InfoSection("Descripción", it)
                }
                product.benefits?.takeIf { it.isNotBlank() }?.let {
                    InfoSection("Beneficios", it)
                }
                product.ingredients?.takeIf { it.isNotBlank() }?.let {
                    InfoSection("Ingredientes", it)
                }
                InfoSection("Cantidad disponible", "${product.stock} unidades")

                // Selector de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        enabled = quantity > 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Disminuir",
                            tint = VerdeOscuro
                        )
                    }
                    Text(
                        text = "$quantity",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    IconButton(
                        onClick = { if (quantity < product.stock) quantity++ },
                        enabled = quantity < product.stock
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar",
                            tint = VerdeOscuro
                        )
                    }
                }

                Button(
                    onClick = { cartViewModel.addToCart(product.id, quantity) },
                    enabled = !isAdding && product.stock > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeOscuro,
                        contentColor = Crema
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isAdding) {
                        CircularProgressIndicator(
                            color = Crema,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            if (product.stock > 0) "Agregar al carrito" else "Sin stock",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (addSuccess) {
                    Text(
                        text = "Producto agregado al carrito",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = VerdeOscuro,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                addError?.let {
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = Color(0xFFB00020),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = VerdeOscuro, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun InfoSection(label: String, text: String) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextoSuave
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = VerdeOscuro
        )
    }
}

private fun formatPrice(price: Double): String {
    val format = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))
    format.maximumFractionDigits = 0
    return "$${format.format(price)}"
}

// El backend devuelve URLs con 127.0.0.1; desde el emulador el host es 10.0.2.2
private fun String.toEmulatorUrl(): String {
    return replace("127.0.0.1", "10.0.2.2").replace("localhost", "10.0.2.2")
}
