package com.gursel.artbookkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gursel.artbookkotlin.ui.theme.ArtBookKotlinTheme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dbHelper = DBHelper(this)
        setContent {
            ArtBookKotlinTheme {
                AppNav(dbHelper)
            }
        }
    }
}
