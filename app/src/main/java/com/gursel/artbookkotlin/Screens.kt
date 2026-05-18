package com.gursel.artbookkotlin

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
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                navController.navigate("art/$artId")
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
    val context = LocalContext.current
    var arts by remember { mutableStateOf(listOf<Art>()) }
    LaunchedEffect(Unit) { arts = dbHelper.getAllArts() }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { /* navigate to new */ onItemClick(-1) }) {
            Text("+")
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
            val input = context.contentResolver.openInputStream(it)
            val bytes = input?.readBytes()
            input?.close()
            if (bytes != null) imageBytes = bytes
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text(if (info=="new") "Add Art" else "Art") }) }) { padding ->
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            if (imageBytes != null) {
                Image(painter = rememberAsyncImagePainter(imageBytes), contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp))
            } else {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp), contentAlignment = Alignment.Center) {
                    Text("Select Image")
                }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = artName, onValueChange = { artName = it }, label = { Text("Art Name") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = painterName, onValueChange = { painterName = it }, label = { Text("Painter Name") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = yearText, onValueChange = { yearText = it }, label = { Text("Created Date") })

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { launcher.launch("image/*") }) { Text("Select Image") }
                if (info == "new") {
                    Button(onClick = {
                        imageBytes?.let { bytes ->
                            dbHelper.insertArt(artName, painterName, yearText, bytes)
                            onDone()
                        }
                    }) { Text("Save") }
                }
            }
        }
    }
}

