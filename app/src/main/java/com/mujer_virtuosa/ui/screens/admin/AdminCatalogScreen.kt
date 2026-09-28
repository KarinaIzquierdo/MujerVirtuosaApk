package com.mujer_virtuosa.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mujer_virtuosa.R
import com.mujer_virtuosa.data.model.product.Product
import com.mujer_virtuosa.ui.components.AdminMenuDrawer
import com.mujer_virtuosa.ui.components.AdminMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.product.ProductViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun AdminCatalogScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToSales: () -> Unit = {},
    onNavigateToUsers: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onAddProduct: () -> Unit = {},
    onEditProduct: (Product) -> Unit = {},
    productViewModel: ProductViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val products = productViewModel.products.value
    val isLoading = productViewModel.isLoading.value
    val error = productViewModel.error.value

    var showAvailable by remember { mutableStateOf(true) }
    var showUnavailable by remember { mutableStateOf(true) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        productViewModel.loadProducts()
        authViewModel.loadProfile()
    }

    val filteredProducts = products.filter {
        (it.active && showAvailable) || (!it.active && showUnavailable)
    }

    AdminMenuDrawer(
        drawerState = drawerState,
        selectedItem = AdminMenuItem.CATALOG,
        onNavigate = { item ->
            when (item) {
                AdminMenuItem.HOME -> onNavigateToHome()
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
        AdminCatalogHeader(
            onMenuClick = { scope.launch { drawerState.open() } }
        )

        // Filtros de disponibilidad
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvailabilityFilter(
                label = "Disponible",
                checked = showAvailable,
                onCheckedChange = { showAvailable = it }
            )
            AvailabilityFilter(
                label = "No Disponible",
                checked = showUnavailable,
                onCheckedChange = { showUnavailable = it }
            )
        }

        // Contenido
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator(color = VerdeOscuro)
                error != null -> Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                filteredProducts.isEmpty() -> Text(
                    text = "No hay productos disponibles",
                    color = VerdeOscuro
                )
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredProducts) { product ->
                        AdminProductCard(
                            product = product,
                            onEdit = { onEditProduct(product) }
                        )
                    }
                }
            }
        }

        // Botón agregar producto
        Button(
            onClick = onAddProduct,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeOscuro,
                contentColor = Crema
            ),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        ) {
            Text("+ Agregar Producto", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
    }
}

@Composable
private fun AdminCatalogHeader(
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
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
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = Crema,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Logo + título
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Mujer Virtuosa",
                modifier = Modifier.height(56.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Catálogo de\nProductos",
                fontFamily = FontFamily.Serif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Crema,
                lineHeight = 36.sp
            )
        }
    }
}

@Composable
private fun AvailabilityFilter(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = VerdeOscuro,
                uncheckedColor = VerdeOscuro,
                checkmarkColor = Crema
            )
        )
        Text(label, fontSize = 15.sp, color = VerdeOscuro)
    }
}

@Composable
private fun AdminProductCard(
    product: Product,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEDE4C8))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
        ) {
            AsyncImage(
                model = product.image?.toEmulatorUrl(),
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Button(
                onClick = onEdit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Crema,
                    contentColor = VerdeOscuro
                ),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            ) {
                Text("Editar Info", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(
                text = product.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = VerdeOscuro
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = product.price.toPriceFormat(),
                fontSize = 15.sp,
                color = VerdeOscuro
            )
        }
    }
}

// El backend devuelve URLs con 127.0.0.1; desde el emulador el host es 10.0.2.2
private fun String.toEmulatorUrl(): String {
    return replace("127.0.0.1", "10.0.2.2").replace("localhost", "10.0.2.2")
}

// Formato de precio estilo colombiano: $35.000
private fun Double.toPriceFormat(): String {
    val formatted = String.format(Locale.US, "%,.0f", this)
    return "$" + formatted.replace(',', '.')
}
