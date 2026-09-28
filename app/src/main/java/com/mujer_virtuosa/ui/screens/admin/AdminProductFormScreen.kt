package com.mujer_virtuosa.ui.screens.admin

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mujer_virtuosa.ui.components.AuthField
import com.mujer_virtuosa.ui.theme.Crema
import com.mujer_virtuosa.ui.theme.VerdeOscuro
import com.mujer_virtuosa.ui.viewmodel.product.ProductViewModel
import java.io.File

@Composable
fun AdminProductFormScreen(
    onBack: () -> Unit = {},
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current

    // Si hay producto seleccionado, el formulario edita; si no, crea
    val editing = productViewModel.selectedProduct.value

    var name by remember { mutableStateOf(editing?.name ?: "") }
    var price by remember { mutableStateOf(editing?.price?.toLong()?.toString() ?: "") }
    var stock by remember { mutableStateOf(editing?.stock?.toString() ?: "") }
    var category by remember { mutableStateOf(editing?.category ?: "") }
    var description by remember { mutableStateOf(editing?.description ?: "") }
    var active by remember { mutableStateOf(editing?.active ?: true) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var localError by remember { mutableStateOf<String?>(null) }

    val isSaving = productViewModel.isSaving.value
    val saveError = productViewModel.saveError.value
    val saveSuccess = productViewModel.saveSuccess.value

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> imageUri = uri }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            productViewModel.resetSaveState()
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
            .statusBarsPadding()
    ) {
        // Barra superior
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
                text = if (editing != null) "Editar Producto" else "Agregar Producto",
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Selector de imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                val previewModel = imageUri ?: editing?.image?.toEmulatorUrl()
                if (previewModel != null) {
                    AsyncImage(
                        model = previewModel,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = Color(0xFF8A8578),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toca para agregar una foto",
                            fontSize = 14.sp,
                            color = Color(0xFF8A8578)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AuthField(
                label = "Nombre",
                value = name,
                onValueChange = { name = it },
                placeholder = "Shampoo reparador",
                icon = Icons.Default.Sell,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthField(
                label = "Precio",
                value = price,
                onValueChange = { price = it },
                placeholder = "35000",
                icon = Icons.Default.Paid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthField(
                label = "Cantidad",
                value = stock,
                onValueChange = { stock = it },
                placeholder = "10",
                icon = Icons.Default.Inventory,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthField(
                label = "Categoría (opcional)",
                value = category,
                onValueChange = { category = it },
                placeholder = "General",
                icon = Icons.Default.Category,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción (multilínea)
            Text(
                text = "Descripción (opcional)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = VerdeOscuro,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Describe el producto", color = Color(0xFF9A9484)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFF8A8578)
                    )
                },
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = Color(0xFFD8D0BC)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Disponibilidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Disponible",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = VerdeOscuro,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = active,
                    onCheckedChange = { active = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = VerdeOscuro,
                        checkedThumbColor = Crema
                    )
                )
            }

            // Errores
            val errorText = localError ?: saveError
            if (errorText != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorText,
                    color = Color(0xFFB00020),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Botón guardar
        Button(
            onClick = {
                when {
                    name.isBlank() -> localError = "El nombre es obligatorio"
                    price.isBlank() || price.toDoubleOrNull() == null ->
                        localError = "Ingresa un precio válido"
                    stock.isBlank() || stock.toIntOrNull() == null ->
                        localError = "Ingresa una cantidad válida"
                    else -> {
                        localError = null
                        val imageFile = imageUri?.let { uriToFile(context, it) }
                        if (editing != null) {
                            productViewModel.updateProduct(
                                id = editing.id,
                                name = name.trim(),
                                price = price.trim(),
                                stock = stock.trim(),
                                description = description.trim(),
                                category = category.trim(),
                                active = active,
                                imageFile = imageFile
                            )
                        } else {
                            productViewModel.createProduct(
                                name = name.trim(),
                                price = price.trim(),
                                stock = stock.trim(),
                                description = description.trim(),
                                category = category.trim(),
                                imageFile = imageFile
                            )
                        }
                    }
                }
            },
            enabled = !isSaving,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeOscuro,
                contentColor = Crema
            ),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    color = Crema,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (editing != null) "Guardar Cambios" else "Guardar Producto",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// El backend devuelve URLs con 127.0.0.1; desde el emulador el host es 10.0.2.2
private fun String.toEmulatorUrl(): String {
    return replace("127.0.0.1", "10.0.2.2").replace("localhost", "10.0.2.2")
}

// Copia la imagen seleccionada a un archivo temporal para subirla
private fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "product_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { output -> input.copyTo(output) }
        input.close()
        file
    } catch (e: Exception) {
        null
    }
}
