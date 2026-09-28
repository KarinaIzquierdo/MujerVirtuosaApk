package com.mujer_virtuosa.ui.screens.user

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mujer_virtuosa.data.model.product.Product
import com.mujer_virtuosa.data.model.user.Progress
import com.mujer_virtuosa.ui.components.UserMenuDrawer
import com.mujer_virtuosa.ui.components.UserMenuItem
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.auth.AuthViewModel
import com.mujer_virtuosa.ui.viewmodel.product.ProductViewModel
import com.mujer_virtuosa.ui.viewmodel.user.ProgressViewModel
import kotlinx.coroutines.launch

private val CardBeige = Color(0xFFEDE4C8)
private val TextoSuave = Color(0xFF8A8578)
private val Dorado = Color(0xFFC9A84C)

@Composable
fun ProgressScreen(
    onLogout: () -> Unit = {},
    onNavigateToCatalog: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    progressViewModel: ProgressViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoading = progressViewModel.isLoading.value
    val error = progressViewModel.error.value
    val myProgress = progressViewModel.myProgress.value
    val community = progressViewModel.community.value

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }

    val saveSuccess = progressViewModel.saveSuccess.value
    val saveError = progressViewModel.saveError.value

    LaunchedEffect(Unit) {
        progressViewModel.loadProgress()
        productViewModel.loadProducts()
        authViewModel.loadProfile()
    }

    LaunchedEffect(saveSuccess, saveError) {
        if (saveSuccess) {
            Toast.makeText(context, "Progreso añadido correctamente", Toast.LENGTH_SHORT).show()
            showAddDialog = false
            progressViewModel.resetSaveState()
        }
        saveError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            progressViewModel.resetSaveState()
        }
    }

    UserMenuDrawer(
        drawerState = drawerState,
        selectedItem = UserMenuItem.PROGRESS,
        onNavigate = { item ->
            when (item) {
                UserMenuItem.CATALOG -> onNavigateToCatalog()
                UserMenuItem.CART -> onNavigateToCart()
                UserMenuItem.ORDERS -> onNavigateToOrders()
                UserMenuItem.PROFILE -> onNavigateToProfile()
                else -> { /* ya estamos en progreso o pendiente */ }
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
            ProgressHeader(
                onMenuClick = { scope.launch { drawerState.open() } },
                onAddClick = { showAddDialog = true }
            )

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
                else -> ProgressContent(
                    myProgress = myProgress,
                    community = community
                )
            }
        }
    }

    if (showAddDialog) {
        AddProgressDialog(
            products = productViewModel.products.value,
            isSaving = progressViewModel.isSaving.value,
            onDismiss = { showAddDialog = false },
            onSave = { productId, description, beforeUri, afterUri ->
                progressViewModel.createProgress(
                    context, productId, description, beforeUri, afterUri
                )
            }
        )
    }
}

@Composable
private fun ProgressHeader(
    onMenuClick: () -> Unit,
    onAddClick: () -> Unit
) {
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
        Text(
            text = "MI PROGRESO",
            fontFamily = FontFamily.Serif,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeOscuro,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = CardBeige,
                contentColor = VerdeOscuro
            ),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Añadir progreso", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProgressContent(
    myProgress: List<Progress>,
    community: List<Progress>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 24.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Mi progreso destacado (ancho completo)
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column {
                if (myProgress.isEmpty()) {
                    Text(
                        text = "Aún no has registrado tu progreso",
                        color = TextoSuave,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    myProgress.forEach { progress ->
                        ProgressCard(
                            progress = progress,
                            featured = true,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
                HorizontalDivider(
                    color = VerdeOscuro,
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "PROGRESOS DE LA COMUNIDAD",
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        // Progresos de la comunidad (grid 2 columnas)
        items(community) { progress ->
            ProgressCard(progress = progress, featured = false)
        }
    }
}

@Composable
private fun ProgressCard(
    progress: Progress,
    featured: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VerdeOscuro)
            .padding(if (featured) 16.dp else 10.dp)
    ) {
        BeforeAfterImages(
            beforeUrl = progress.beforeImage,
            afterUrl = progress.afterImage,
            height = if (featured) 180.dp else 140.dp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = progress.product?.name ?: "Producto",
            fontSize = if (featured) 18.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            color = Crema,
            textAlign = if (featured) TextAlign.Start else TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Experiencia:",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Crema
        )
        Text(
            text = progress.description ?: "",
            fontSize = 12.sp,
            color = Crema.copy(alpha = 0.9f),
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun BeforeAfterImages(
    beforeUrl: String?,
    afterUrl: String?,
    height: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = beforeUrl?.toEmulatorUrl(),
                contentDescription = "Antes",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(CardBeige)
            )
            AsyncImage(
                model = afterUrl?.toEmulatorUrl(),
                contentDescription = "Después",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(CardBeige)
            )
        }

        // Etiquetas ANTES / DESPUÉS
        Text(
            text = "ANTES",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Crema,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .background(VerdeOscuro.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )
        Text(
            text = "DESPUÉS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Crema,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .background(VerdeOscuro.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        )

        // Divisor central con flecha
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(2.dp)
                .fillMaxSize()
                .background(Dorado)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(28.dp)
                .clip(CircleShape)
                .background(Dorado),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = VerdeOscuro,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddProgressDialog(
    products: List<Product>,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (productId: Int, description: String, beforeUri: Uri, afterUri: Uri) -> Unit
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var beforeUri by remember { mutableStateOf<Uri?>(null) }
    var afterUri by remember { mutableStateOf<Uri?>(null) }

    val beforePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> beforeUri = uri }
    val afterPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> afterUri = uri }

    val canSave = selectedProduct != null &&
            description.isNotBlank() &&
            beforeUri != null &&
            afterUri != null &&
            !isSaving

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Crema,
        title = {
            Text(
                text = "Añadir progreso",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedProduct?.name ?: "Selecciona el producto",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Producto") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VerdeOscuro,
                            unfocusedBorderColor = VerdeOscuro,
                            focusedLabelColor = VerdeOscuro,
                            focusedTextColor = VerdeOscuro,
                            unfocusedTextColor = VerdeOscuro
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        products.forEach { product ->
                            DropdownMenuItem(
                                text = { Text(product.name) },
                                onClick = {
                                    selectedProduct = product
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Experiencia") },
                    placeholder = { Text("Cuenta cómo te fue con el producto") },
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdeOscuro,
                        unfocusedBorderColor = VerdeOscuro,
                        focusedLabelColor = VerdeOscuro,
                        focusedTextColor = VerdeOscuro,
                        unfocusedTextColor = VerdeOscuro
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { beforePicker.launch("image/*") },
                        shape = RoundedCornerShape(50.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VerdeOscuro),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeOscuro),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (beforeUri != null) "Antes ✓" else "Foto antes",
                            fontSize = 12.sp
                        )
                    }
                    OutlinedButton(
                        onClick = { afterPicker.launch("image/*") },
                        shape = RoundedCornerShape(50.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VerdeOscuro),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeOscuro),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (afterUri != null) "Después ✓" else "Foto después",
                            fontSize = 12.sp
                        )
                    }
                }

                if (isSaving) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            color = VerdeOscuro,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardando...", color = VerdeOscuro, fontSize = 13.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        selectedProduct!!.id,
                        description.trim(),
                        beforeUri!!,
                        afterUri!!
                    )
                },
                enabled = canSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    contentColor = Crema
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = VerdeOscuro)
            }
        }
    )
}

// El backend devuelve URLs con 127.0.0.1; desde el emulador el host es 10.0.2.2
private fun String.toEmulatorUrl(): String {
    return replace("127.0.0.1", "10.0.2.2").replace("localhost", "10.0.2.2")
}
