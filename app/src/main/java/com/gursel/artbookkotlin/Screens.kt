package com.gursel.artbookkotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(dbHelper: DBHelper) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ArtListScreen(dbHelper) { artId ->
                if (artId == 0) {
                    navController.navigate("art/new")
                } else {
                    navController.navigate("art/$artId")
                }
            }
        }
        composable("art/new") {
            ArtDetailScreen(dbHelper, info = "new") { navController.popBackStack() }
        }
        composable("art/{artId}", arguments = listOf(navArgument("artId") { type = NavType.IntType })) { backStackEntry ->
            val artId = backStackEntry.arguments?.getInt("artId") ?: 0
            ArtDetailScreen(dbHelper, info = "old", artId = artId) { navController.popBackStack() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtListScreen(dbHelper: DBHelper, onItemClick: (Int) -> Unit) {
    var arts by remember { mutableStateOf(listOf<Art>()) }
    LaunchedEffect(Unit) { arts = dbHelper.getAllArts() }

    Scaffold(
        topBar = {
            // Status badge style top bar: rounded pill with icon + text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // Reduced vertical padding to bring the list closer to the status badge
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "ArtWork Main List",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        },
        bottomBar = {
        Button(
            onClick = { onItemClick(0) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(52.dp)
        ) {
            Text("Add New ArtWork")
        }
    }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(arts) { art ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (art.id != 0) onItemClick(art.id) else onItemClick(-1) }
                    .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(art.name, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtDetailScreen(dbHelper: DBHelper, info: String, artId: Int = -1, onDone: () -> Unit) {
    val context = LocalContext.current
    var artName by remember { mutableStateOf("") }
    var painterName by remember { mutableStateOf("") }
    var yearText by remember { mutableStateOf("") }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(info, artId) {
        if (info == "old" && artId >= 0) {
            val art = dbHelper.getArtById(artId)
            art?.let {
                artName = it.name
                painterName = it.painter ?: ""
                yearText = it.year ?: ""
                imageBytes = it.image
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            try {
                val input = context.contentResolver.openInputStream(it)
                val bytes = input?.readBytes()
                input?.close()
                if (bytes != null) imageBytes = bytes
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun bytesToBitmap(bytes: ByteArray): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(if (info == "new") "Add New Artwork" else "Artwork Details") })
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Image Display
            if (imageBytes != null) {
                val bitmap = bytesToBitmap(imageBytes!!)
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clickable { if (info == "new") launcher.launch("image/*") }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clickable { if (info == "new") launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Failed to load image")
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clickable { if (info == "new") launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tap to Select Image")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Art Name Input
            OutlinedTextField(
                value = artName,
                onValueChange = { artName = it },
                label = { Text("Art Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = info == "new"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Painter Name Input
            OutlinedTextField(
                value = painterName,
                onValueChange = { painterName = it },
                label = { Text("Painter Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = info == "new"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Year Input
            OutlinedTextField(
                value = yearText,
                onValueChange = { yearText = it },
                label = { Text("Created Date (Year)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = info == "new"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons
            if (info == "new") {
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("SELECT IMAGE")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (artName.isNotBlank() && painterName.isNotBlank() && yearText.isNotBlank() && imageBytes != null) {
                            dbHelper.insertArt(artName, painterName, yearText, imageBytes!!)
                            onDone()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = artName.isNotBlank() && painterName.isNotBlank() && yearText.isNotBlank() && imageBytes != null
                ) {
                    Text("SAVE ARTWORK")
                }
            } else {
                Button(
                    onClick = { onDone() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("BACK")
                }
            }
        }
    }
}

