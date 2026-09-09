package com.example.ui.screens.tools.photo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.RotateRight
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.ui.components.MsaadaTopBar
import com.example.ui.components.ProBadge
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaProGold
import com.example.ui.theme.MsaadaTeal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class PassportSizePreset(
    val id: String,
    val name: String,
    val dimensionLabel: String,
    val aspectWidth: Float,
    val aspectHeight: Float,
    val targetWidthPx: Int = 600,
    val targetHeightPx: Int = 600
)

@Composable
fun PassportPhotoScreen(
    isSwahili: Boolean,
    isProUser: Boolean,
    photosUsed: Int,
    onGenerateSuccess: () -> Boolean, // returns true if permitted, false if limit reached
    onOpenPro: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var loadedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }

    val presets = listOf(
        PassportSizePreset(
            id = "tz_passport",
            name = if (isSwahili) "Pasipoti ya Tanzania" else "Tanzania Passport",
            dimensionLabel = "35 x 45 mm",
            aspectWidth = 35f,
            aspectHeight = 45f,
            targetWidthPx = 413,
            targetHeightPx = 531
        ),
        PassportSizePreset(
            id = "std_2x2",
            name = if (isSwahili) "Passport Standard (Visa)" else "Standard Passport (2x2)",
            dimensionLabel = "2 x 2 in / 51x51 mm",
            aspectWidth = 51f,
            aspectHeight = 51f,
            targetWidthPx = 600,
            targetHeightPx = 600
        ),
        PassportSizePreset(
            id = "id_stamp",
            name = if (isSwahili) "Kitambulisho / Stamp Size" else "ID / Stamp Size",
            dimensionLabel = "30 x 40 mm",
            aspectWidth = 30f,
            aspectHeight = 40f,
            targetWidthPx = 354,
            targetHeightPx = 472
        ),
        PassportSizePreset(
            id = "large_2x25",
            name = if (isSwahili) "Ukubwa Maalum (2x2.5)" else "Custom Size (2x2.5)",
            dimensionLabel = "50 x 65 mm",
            aspectWidth = 50f,
            aspectHeight = 65f,
            targetWidthPx = 590,
            targetHeightPx = 767
        )
    )

    var selectedPresetIndex by remember { mutableIntStateOf(0) }
    var sheetCount by remember { mutableIntStateOf(6) } // 4, 6, 8, 12
    var generatedResultBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var showSheetPreview by remember { mutableStateOf(false) }

    // Pick Photo from Gallery (Zero-permission Android Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            scope.launch(Dispatchers.IO) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val bmp = BitmapFactory.decodeStream(stream)
                        withContext(Dispatchers.Main) {
                            loadedBitmap = bmp
                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f
                            rotation = 0f
                            generatedResultBitmap = null
                            showSheetPreview = false
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Imeshindikana kusoma picha", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Camera capture Uri launcher
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && tempCameraUri != null) {
            val uri = tempCameraUri!!
            selectedImageUri = uri
            scope.launch(Dispatchers.IO) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val bmp = BitmapFactory.decodeStream(stream)
                        withContext(Dispatchers.Main) {
                            loadedBitmap = bmp
                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f
                            rotation = 0f
                            generatedResultBitmap = null
                            showSheetPreview = false
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Imeshindikana kusoma picha kutoka kamera", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun launchCamera() {
        try {
            val photoFile = File(context.cacheDir, "camera_capture_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            Toast.makeText(context, "Kamera haipatikani: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val selectedPreset = presets[selectedPresetIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MsaadaTopBar(
            title = if (isSwahili) "Passport Photo Generator" else "Passport Photo Generator",
            onBack = onBack,
            actions = {
                if (!isProUser) {
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(onClick = onOpenPro),
                        color = MsaadaTeal.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "$photosUsed/5 ${if (isSwahili) "Bure" else "Free"}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MsaadaTeal
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Source Buttons if no image yet
            if (loadedBitmap == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(MsaadaTeal.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = null,
                                tint = MsaadaTeal,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isSwahili) "Chagua au Piga Picha Yako" else "Choose or Take Your Photo",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isSwahili)
                                "Piga picha safi ukiangalia mbele kwenye mandhari yenye mwanga wa kutosha (background nyeupe inapendekezwa)."
                            else
                                "Capture a clear portrait facing directly with good lighting (white background recommended).",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { launchCamera() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("passport_take_photo_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(imageVector = Icons.Outlined.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isSwahili) "Piga Picha" else "Camera", fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("passport_pick_gallery_button"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(imageVector = Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isSwahili) "Galari" else "Gallery", fontSize = 13.sp)
                            }
                        }
                    }
                }
            } else {
                // Interactive Editor Viewport
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isSwahili) "Rekebisha Picha & Mkao wa Kichwa" else "Adjust Photo & Head Position",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isSwahili) "Buruta kusogeza, tumia slider kukuza au zungusha picha" else "Drag to position, use slider to zoom or rotate",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Aspect Ratio Box with Oval Guide Overlay
                        val aspect = selectedPreset.aspectWidth / selectedPreset.aspectHeight
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.72f)
                                .aspectRatio(aspect)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE2E8F0))
                                .border(2.dp, MsaadaTeal, RoundedCornerShape(12.dp))
                                .pointerInput(Unit) {
                                    detectTransformGestures { _, pan, zoom, _ ->
                                        scale = (scale * zoom).coerceIn(0.6f, 3.5f)
                                        offsetX += pan.x
                                        offsetY += pan.y
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            // Render user image with scale, offsets, rotation
                            loadedBitmap?.let { bmp ->
                                Image(
                                    bitmap = bmp.asImageBitmap(),
                                    contentDescription = "Passport subject",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer(
                                            scaleX = scale,
                                            scaleY = scale,
                                            translationX = offsetX,
                                            translationY = offsetY,
                                            rotationZ = rotation
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // Visual Guide Overlay: Head oval and eye line
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height
                                val stroke = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                                )

                                // Oval for head
                                drawOval(
                                    color = Color(0xFF008C95),
                                    topLeft = Offset(w * 0.22f, h * 0.12f),
                                    size = Size(w * 0.56f, h * 0.62f),
                                    style = stroke
                                )

                                // Eye line
                                drawLine(
                                    color = Color(0x99008C95),
                                    start = Offset(w * 0.25f, h * 0.40f),
                                    end = Offset(w * 0.75f, h * 0.40f),
                                    strokeWidth = 1.5.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                                )

                                // Chin line
                                drawLine(
                                    color = Color(0x99008C95),
                                    start = Offset(w * 0.35f, h * 0.74f),
                                    end = Offset(w * 0.65f, h * 0.74f),
                                    strokeWidth = 1.5.dp.toPx()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Controls: Rotate, Reset, Zoom
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { rotation = (rotation + 90f) % 360f },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Icon(imageVector = Icons.Outlined.RotateRight, contentDescription = "Rotate", tint = MsaadaNavy)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        scale = 1f
                                        offsetX = 0f
                                        offsetY = 0f
                                        rotation = 0f
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "Reset", tint = MsaadaNavy)
                                }
                            }

                            // Change Photo Button
                            OutlinedButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(text = if (isSwahili) "Badili Picha" else "Change", fontSize = 12.sp)
                            }
                        }

                        // Zoom Slider
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ZoomIn,
                                contentDescription = "Zoom",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Slider(
                                value = scale,
                                onValueChange = { scale = it },
                                valueRange = 0.6f..3.0f,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = MsaadaTeal,
                                    activeTrackColor = MsaadaTeal
                                )
                            )
                        }
                    }
                }

                // 2. Preset Selection
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isSwahili) "Vipimo vya Picha (Standard Sizes)" else "Photo Preset Sizes",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        presets.forEachIndexed { index, preset ->
                            val isSelected = index == selectedPresetIndex
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MsaadaTeal.copy(alpha = 0.12f) else Color.Transparent)
                                    .clickable { selectedPresetIndex = index }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = preset.name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MsaadaTeal else MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = preset.dimensionLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(MsaadaTeal),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Print Sheet Layout Count
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isSwahili) "Idadi ya Picha kwenye Karatasi (Print Sheet)" else "Photos per Printable Sheet",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(4, 6, 8, 12).forEach { count ->
                                val selected = sheetCount == count
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = if (selected) 2.dp else 1.dp,
                                            color = if (selected) MsaadaTeal else MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(if (selected) MsaadaTeal.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface)
                                        .clickable { sheetCount = count }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$count",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) MsaadaTeal else MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Generate Sheet Button
                Button(
                    onClick = {
                        val allowed = onGenerateSuccess()
                        if (!allowed) {
                            onOpenPro()
                        } else {
                            isGenerating = true
                            scope.launch(Dispatchers.Default) {
                                val single = createCroppedPhotoBitmap(
                                    source = loadedBitmap!!,
                                    targetWidth = selectedPreset.targetWidthPx,
                                    targetHeight = selectedPreset.targetHeightPx,
                                    scale = scale,
                                    offsetX = offsetX,
                                    offsetY = offsetY,
                                    rotation = rotation
                                )

                                val sheet = createPrintableSheetBitmap(
                                    singlePhoto = single,
                                    count = sheetCount,
                                    sheetPreset = selectedPreset
                                )

                                withContext(Dispatchers.Main) {
                                    generatedResultBitmap = sheet
                                    isGenerating = false
                                    showSheetPreview = true
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("passport_generate_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaNavy)
                ) {
                    Text(
                        text = if (isSwahili) "Unda Karatasi ya Picha (${sheetCount}x)" else "Generate Photo Sheet (${sheetCount}x)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }

                // Sheet Preview & Export
                if (showSheetPreview && generatedResultBitmap != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isSwahili) "Karatasi Tayari ya Kuchapa" else "Ready Printable Sheet",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = selectedPreset.dimensionLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(color = MsaadaTeal, fontWeight = FontWeight.Bold)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Image(
                                bitmap = generatedResultBitmap!!.asImageBitmap(),
                                contentDescription = "Generated sheet",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.4f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        scope.launch(Dispatchers.IO) {
                                            saveAndShareBitmap(context, generatedResultBitmap!!, isShare = false)
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    if (isSwahili) "Karatasi ya picha imehifadhiwa kwenye simu!" else "Photo sheet saved to device!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("passport_save_button"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MsaadaTeal)
                                ) {
                                    Icon(imageVector = Icons.Outlined.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = if (isSwahili) "Hifadhi" else "Save", fontSize = 13.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        scope.launch(Dispatchers.IO) {
                                            saveAndShareBitmap(context, generatedResultBitmap!!, isShare = true)
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("passport_share_button"),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Icon(imageVector = Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = if (isSwahili) "Shiriki" else "Share", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Disclaimer Card (Mandatory Requirement)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = MsaadaTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isSwahili)
                            "Taarifa: Mahitaji ya picha yanaweza kutofautiana kulingana na taasisi. Hakikisha umeangalia mahitaji ya sehemu unayowasilisha picha (mfano: rangi ya usuli au vipimo)."
                        else
                            "Notice: Photo requirements may vary by institution. Ensure you verify guidelines (e.g. background color or dimensions) with the issuing authority.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}

// Utility: Crop photo bitmap according to transform params
fun createCroppedPhotoBitmap(
    source: Bitmap,
    targetWidth: Int,
    targetHeight: Int,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
    rotation: Float
): Bitmap {
    val result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    canvas.drawColor(android.graphics.Color.WHITE) // default clean white background

    val matrix = Matrix()
    // Center of source
    val srcW = source.width.toFloat()
    val srcH = source.height.toFloat()

    // Translate to center
    matrix.postTranslate(-srcW / 2f, -srcH / 2f)
    matrix.postRotate(rotation)
    matrix.postScale(scale, scale)
    matrix.postTranslate(targetWidth / 2f + offsetX, targetHeight / 2f + offsetY)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    canvas.drawBitmap(source, matrix, paint)

    return result
}

// Utility: Create printable sheet grid with borders & crop marks
fun createPrintableSheetBitmap(
    singlePhoto: Bitmap,
    count: Int,
    sheetPreset: PassportSizePreset
): Bitmap {
    // 4x6 inch standard photographic print paper size at 300 DPI: 1800 x 1200 px
    val sheetW = 1800
    val sheetH = 1200
    val sheet = Bitmap.createBitmap(sheetW, sheetH, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(sheet)
    canvas.drawColor(android.graphics.Color.WHITE)

    val cols = when (count) {
        4 -> 2
        6 -> 3
        8 -> 4
        12 -> 4
        else -> 3
    }
    val rows = count / cols

    val photoW = (singlePhoto.width * 0.9f).toInt()
    val photoH = (singlePhoto.height * 0.9f).toInt()

    val totalPhotosW = cols * photoW
    val totalPhotosH = rows * photoH

    val startX = (sheetW - totalPhotosW) / (cols + 1)
    val startY = (sheetH - totalPhotosH) / (rows + 1)

    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.DKGRAY
        textSize = 28f
    }

    // Watermark/footer header
    canvas.drawText("MSAADA Tanzania - ${sheetPreset.name} (${sheetPreset.dimensionLabel})", 40f, sheetH - 30f, textPaint)

    var current = 0
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (current >= count) break
            val left = startX + c * (photoW + startX)
            val top = startY + r * (photoH + startY)

            val destRect = Rect(left, top, left + photoW, top + photoH)
            canvas.drawBitmap(singlePhoto, null, destRect, null)
            canvas.drawRect(destRect, borderPaint)
            current++
        }
    }

    return sheet
}

// Utility: Save and Share
fun saveAndShareBitmap(context: Context, bitmap: Bitmap, isShare: Boolean) {
    try {
        val imagesFolder = File(context.cacheDir, "images")
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "passport_sheet_${System.currentTimeMillis()}.jpg")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
        stream.flush()
        stream.close()

        val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        if (isShare) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Shiriki Picha ya Pasipoti"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
