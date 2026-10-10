package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.navigation.DocBotNavGraph
import com.example.ui.theme.DocReqBotTheme
import com.example.viewmodel.DocBotViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DocReqBotTheme {
        val navController = rememberNavController()
        val viewModel: DocBotViewModel = viewModel()

        DocBotNavGraph(
          navController = navController,
          viewModel = viewModel
        )
      }
    }
  }
}
