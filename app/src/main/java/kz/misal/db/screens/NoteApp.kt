package kz.misal.db.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
// + Step 1 Добавьте кнопку удаления заметки на элемент списка заметок
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.misal.db.model.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NoteApp(model: NoteViewModel) {
    if (model.isAddingNote) {
        AddNoteScreen(
            onSave = model::addNote,
            onBack = { model.isAddingNote = false })
    } else {
        MainListScreen(model)
    }
}

@Composable
fun MainListScreen(model: NoteViewModel) {
    val notes by model.notes.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { model.isAddingNote = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Text(
                    text = "Мои Заметки",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            items(notes) { note ->
                val dateString = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                    .format(Date(note.date))
                // - Step 1 Добавьте кнопку удаления заметки на элемент списка заметок
                // ListItem(
                //    leadingContent = { Text(note.emoji, fontSize = 32.sp) },
                //    headlineContent = { Text(note.title) },
                //    supportingContent = { Text(note.text) },
                //    overlineContent = { Text(dateString) }
                // )
                // + Step 1 Добавьте кнопку удаления заметки на элемент списка заметок
                ListItem(
                    leadingContent = { Text(note.emoji, fontSize = 32.sp) },
                    headlineContent = { Text(note.title) },
                    supportingContent = { Text(note.text) },
                    overlineContent = { Text(dateString) },
                    trailingContent = {
                        IconButton(onClick = { model.deleteNote(note) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AddNoteScreen(onSave: (String, String, String) -> Unit, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📝") }
    val emojis = listOf("📝", "💡", "✅", "🔥", "🚀", "📌")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            emojis.forEach { emoji ->
                FilterChip(
                    selected = selectedEmoji == emoji,
                    onClick = { selectedEmoji = emoji },
                    label = { Text(emoji, fontSize = 24.sp) }
                )
            }
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Текст") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onBack) { Text("Отмена") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { if (title.isNotBlank()) onSave(title, text, selectedEmoji) }) {
                Text("Сохранить")
            }
        }
    }
}
