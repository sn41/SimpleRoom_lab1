# Задание 3. Выполняем миграцию данных при изменении версии базы данных.

## Миграция

Добавим к нашему приложению возможность выбора эмодзи и поле в базе данных для него, сопроводим новую версию механизмом миграции.

Добавление нового поля в существующую базу данных — это критический момент в разработке. Если просто добавить поле в код и запустить приложение, оно упадет, так как структура в коде перестанет совпадать со структурой файла на диске. Чтобы этого избежать, используется **механизм миграции**.

---

### 1. Обновление модели (Entity)

Добавляем поле `emoji`. Мы задаем ему значение по умолчанию (например, "📝"), чтобы старые записи не остались пустыми.

```kotlin
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val text: String,
    val date: Long,
    val emoji: String = "📝" // Новое поле
)

```

---

### 2. Определение миграции

Миграция — это инструкция для Room, как именно изменить файл базы данных. В нашем случае нужно выполнить SQL-команду `ALTER TABLE`.

Добавим в файл AppDatabase следующие строки:

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Добавляем колонку emoji типа TEXT, которая не может быть null, со значением по умолчанию
        db.execSQL("ALTER TABLE notes ADD COLUMN emoji TEXT NOT NULL DEFAULT '📝'")
    }
}

```

---

### 3. Обновление класса базы данных

Теперь нужно увеличить версию базы данных с `1` до `2` и зарегистрировать нашу миграцию.

```kotlin
@Database(entities = [Note::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_database"
                )
                    // todo 3 - добавим алгоритм миграции
                    .addMigrations(MIGRATION_1_2) // Указываем, как переходить
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

---

### 4. ViewModel и UI для выбора эмодзи

Во ViewModel добавим функцию сохранения с учетом нового поля. В UI добавим простую панель выбора.

```kotlin
// Во ViewModel:
fun addNote(title: String, text: String, emoji: String) {
    viewModelScope.launch {
        dao.insert(Note(title = title, text = text, date = System.currentTimeMillis(), emoji = emoji))
        isAddingNote = false
    }
}

// В UI экрана создания (AddNoteScreen):
@Composable
fun AddNoteScreen(onSave: (String, String, String) -> Unit, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("📝") }
    val emojis = listOf("📝", "💡", "✅", "🔥", "🚀", "📌")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Панель выбора эмодзи
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            emojis.forEach { emoji ->
                FilterChip(
                    selected = selectedEmoji == emoji,
                    onClick = { selectedEmoji = emoji },
                    label = { Text(emoji, fontSize = 24.sp) }
                )
            }
        }
        
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Заголовок") })
        // ... (остальные поля)
        Button(onClick = { if (title.isNotBlank()) onSave(title, text, selectedEmoji) }) {
            Text("Сохранить")
        }
    }
}

```

---

### 5. Отображение в списке

Теперь в `MainListScreen` мы можем использовать эмодзи в качестве иконки заметки.

```kotlin
ListItem(
    leadingContent = { Text(note.emoji, fontSize = 32.sp) }, // Эмодзи слева
    headlineContent = { Text(note.title) },
    supportingContent = { Text(note.text) }
)

```

---

### Как это работает "под капотом"?

1. **Запуск:** Когда вы запускаете новую версию приложения, Room проверяет `identity_hash` внутри файла БД.
2. **Конфликт:** Он видит, что версия в файле `1`, а в коде `2`.
3. **Поиск решения:** Room ищет в списке `addMigrations` путь от 1 до 2.
4. **Выполнение:** Выполняется наш SQL-код `ALTER TABLE`. Теперь файл базы данных на диске соответствует новой модели `Note`.
5. **Готово:** Данные успешно перенесены, и приложение продолжает работу без потери старых заметок.

### Что проверить в Database Inspector?

Откройте инспектор после запуска. Вы увидите, что:

* Появилась новая колонка `emoji`.
* Все ваши старые заметки, созданные до миграции, автоматически получили значение `📝`.
* Новые заметки сохраняются с тем эмодзи, который вы выбрали.

Это фундаментальный навык: теперь вы умеете не только создавать базы данных, но и развивать их структуру на живых устройствах пользователей.