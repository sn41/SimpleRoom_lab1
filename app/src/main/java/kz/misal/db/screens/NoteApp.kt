//package kz.misal.db.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material3.Button
//import androidx.compose.material3.FilterChip
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.ListItem
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kz.misal.db.model.NoteViewModel
//
//@Composable
//fun NoteApp(model: NoteViewModel) {
//    // Если флаг isAddingNote истинный, показываем экран ввода, иначе - список
//    if (model.isAddingNote) {
//        AddNoteScreen(
//            onSave = model::addNote,
//            onBack = { model.isAddingNote = false })
//    } else {
//        MainListScreen(model)
//    }
//}
//
//@Composable
//fun MainListScreen(model: NoteViewModel) {
//    val notes by model.notes.collectAsState()
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(onClick = { model.isAddingNote = true }) {
//                Icon(Icons.Default.Add, contentDescription = "Добавить")
//            }
//        }) { padding ->
//        LazyColumn(modifier = Modifier.padding(padding)) {
//            item { Text("Мои Заметки", Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
//            items(notes) { note ->
//                val dateString =
//                    java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
//                        .format(java.util.Date(note.date))
//                ListItem(
//                    headlineContent = { Text(note.title) },
//                    supportingContent = { Text(note.content) },
//                    overlineContent = { Text(dateString) })
//            }
//        }
//    }
//}
//
//
//@Composable
//fun AddNoteScreen(onSave: (String, String) -> Unit, onBack: () -> Unit) {
//    var title by remember { mutableStateOf("") }
//    var text by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        OutlinedTextField(
//            value = title,
//            onValueChange = { title = it },
//            label = { Text("Заголовок") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(
//            value = text,
//            onValueChange = { text = it },
//            label = { Text("Текст") },
//            modifier = Modifier.fillMaxWidth(),
//            minLines = 3
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Row {
//            Button(onClick = onBack) { Text("Отмена") }
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(onClick = { if (title.isNotBlank()) onSave(title, text) }) { Text("Сохранить") }
//        }
//    }
//}

