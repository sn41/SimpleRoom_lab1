package kz.misal.db.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kz.misal.db.model.NoteViewModel


@Composable
fun StartNoteScreen(viewModel: NoteViewModel, modifier: Modifier) {
    // Подписка на состояние
    val noteList by viewModel.notes.collectAsState()

    var title by rememberSaveable() { mutableStateOf("") }

    var note by rememberSaveable() { mutableStateOf("") }

    Column(modifier = modifier) {

        OutlinedTextField(
            value = title, { title = it }, label = { Text("title") }
        )
        OutlinedTextField(
            value = note, { note = it }, label = { Text("note") }
        )

        Button(onClick = {
            viewModel.addNote(title, note)
            title = ""; note = ""
        }) {
            Text("Add")
        }

        LazyColumn {
            items(noteList) { note ->
                Text(text = "${note.title}: ${note.content}")
            }
        }
    }
}