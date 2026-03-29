package kz.misal.db

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import kz.misal.db.model.NoteViewModel
import kz.misal.db.screens.NoteApp
import kz.misal.db.ui.theme.SimpleRoomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<NoteViewModel>()

        enableEdgeToEdge()
        setContent {
            SimpleRoomTheme {
                NoteApp(viewModel)
            }
        }
    }
}
