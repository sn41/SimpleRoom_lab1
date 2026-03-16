package kz.misal.db.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kz.misal.db.data.AppDatabase
import kz.misal.db.data.Note

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

    // todo 2 Теперь мы будем сохранять и дату заметки
//    fun addNote(title: String, content: String) {
//        viewModelScope.launch {
//            val currentTimeData = System.currentTimeMillis()
//            val newNote = Note(title = title, content = content, date = currentTimeData)
//            dao.insertNote(newNote)
//            isAddingNote = false // Возвращаемся на главный экран
//        }
//    }

    // todo 3 Теперь мы будем сохранять и дату заметки и эмодзи
//    fun addNote(title: String, content: String, emoji: String) {
//        viewModelScope.launch {
//            val currentTimeData = System.currentTimeMillis()
//            val newNote = Note(title = title, content = content, date = currentTimeData, emoji = emoji)
//            dao.insertNote(newNote)
//            isAddingNote = false // Возвращаемся на главный экран
//        }
//    }

    // Добавим состояние для навигации между экранами списка и добавления задач
    // Состояние для навигации: true - экран создания, false - список
    var isAddingNote by mutableStateOf(false)
}