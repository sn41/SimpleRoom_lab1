package kz.misal.db

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.misal.db.model.NoteViewModel
//import kz.misal.db.screens.NoteApp
import kz.misal.db.screens.StartNoteScreen
import kz.misal.db.ui.theme.SimpleRoomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем ViewModel
        val viewModel by viewModels<NoteViewModel>()

        enableEdgeToEdge()
        setContent {
            SimpleRoomTheme {
                StartNoteScreen(viewModel, Modifier.padding(16.dp))
                // todo 2 - теперь приложение будет иметь два экрана, и NoteApp будет их переключать
//                 NoteApp(viewModel)
            }
        }
    }
}
