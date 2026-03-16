## 1. Определение модели (Model)

   Создаем стандартный Kotlin Data Class. 
   Для интеграции с Room мы добавляем аннотации.

```kotlin
//import androidx.room.Entity
//import androidx.room.PrimaryKey

// Обратите внимание на аннотацию @Entity - мы создаём таблицу базы данных с именем "notes"
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String
)
```

## 2. Настройка Room (Data Layer)
   Нам понадобятся DAO (Data Access Object) для определения операций с БД и сам класс базы данных.

```Kotlin
//import androidx.room.*
//import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>> // Flow позволяет Compose автоматически следить за изменениями
    
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertNote(note: Note)
    
        @Delete
        suspend fun deleteNote(note: Note)

}
```

## 3. Нам нужен объект БД (синглетон)

```kotlin
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase

// 3. Класс базы данных
@Database(entities = [Note::class], version = 1)
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

## 4. Архитектурный мостик: ViewModel
   Вместо того чтобы держать mutableStateOf в UI, 
   мы переносим состояние во ViewModel. 
   Теперь UI подписывается на поток данных из базы.
```kotlin
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch

class NoteViewModel() : ViewModel() {

    // Преобразуем Flow из Room в StateFlow для Compose
    val notes: StateFlow<List<Note>> = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            dao.insertNote(Note(title = title, content = content))
        }
    }
}
```

## 5. Получим объект базы данных в модели

```kotlin
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    // Получаем БД через контекст приложения
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.noteDao()

    // Преобразуем Flow из Room в StateFlow для Compose
    val notes: StateFlow<List<Note>> = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            dao.insertNote(Note(title = title, content = content))
        }
    }
}
```


