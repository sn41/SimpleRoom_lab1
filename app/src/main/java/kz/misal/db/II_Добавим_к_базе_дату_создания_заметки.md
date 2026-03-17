# Задание 2. Модифицируем базу данных, добавляем поле таблицы.

## Изменим класс Note

```kotlin
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    // todo 1: добавим поле для даты
    val date: Long // Храним время в ms
)
```

---

## Добавим порядок сортировки заметок

```kotlin
interface NoteDao {
    // Измените порядок строк, по убыванию даты
    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAllNotes(): Flow<List<Note>> 

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}
```
---

## Изменим функцию добавления записи во ViewModel

```kotlin
    // Теперь мы будем сохранять и дату заметки
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            val currentTimeData = System.currentTimeMillis()
            val newNote = Note(title = title, content = content, date = currentTimeData)
            dao.insertNote(newNote)
            isAddingNote = false // Возвращаемся на главный экран
        }
    }
```

---

## Изменим архитектуру проекта, теперь у нас будут два экрана

Создайте файл NoteApp.kt
```kotlin
@Composable
fun NoteApp(model: NoteViewModel) {
    // Если флаг isAddingNote истинный, показываем экран ввода, иначе - список
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
            item { Text("Мои Заметки", Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
            items(notes) { note ->
                val dateString =
                    java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
                        .format(java.util.Date(note.date))
                ListItem(
                    headlineContent = { Text(note.title) },
                    supportingContent = { Text(note.content) },
                    overlineContent = { Text(dateString) })
            }
        }
    }
}


@Composable
fun AddNoteScreen(onSave: (String, String) -> Unit, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
            Button(onClick = { if (title.isNotBlank()) onSave(title, text) }) { Text("Сохранить") }
        }
    }
}
```

---

## Изменим MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем ViewModel
        val viewModel by viewModels<NoteViewModel>()

        enableEdgeToEdge()
        setContent {
            SimpleRoomTheme {
                //NoteScreen(viewModel, Modifier.padding(16.dp))
                NoteApp(viewModel)
            }
        }
    }
}
```

---

## Миграция базы данных. 

Структура нашей базы данных изменилась. Если вы, пользователь, теперь установите и запустите приложение, оно у вас упадёт.

Удалите старое, и установите новое. Согласовывать старые данные и новые мы будем в следующий раз
