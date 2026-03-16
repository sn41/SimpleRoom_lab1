package kz.misal.db

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kz.misal.db.model.NoteViewModel
import kz.misal.db.screens.NoteScreen
import kz.misal.db.ui.theme.SimpleRoomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем ViewModel
        val viewModel by viewModels<NoteViewModel> ()

        enableEdgeToEdge()
        setContent {
            SimpleRoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NoteScreen(viewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }
}
