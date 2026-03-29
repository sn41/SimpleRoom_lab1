package kz.misal.db.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kz.misal.db.data.AppDatabase
import kz.misal.db.data.Note

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.noteDao()

    // + Step 5 Добавьте кнопку сортировки элементов по алфавиту
    private val isSortedAlphabetically = MutableStateFlow(false)

    // - Step 5 Добавьте кнопку сортировки элементов по алфавиту
    // val notes: StateFlow<List<Note>> = dao.getAllNotes()
    //    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // + Step 5 Добавьте кнопку сортировки элементов по алфавиту
    val notes: StateFlow<List<Note>> = isSortedAlphabetically
        .flatMapLatest { sorted ->
            if (sorted) dao.getAllNotesSortedByText() else dao.getAllNotes()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // + Step 5 Добавьте кнопку сортировки элементов по алфавиту
    fun toggleSort() {
        isSortedAlphabetically.value = !isSortedAlphabetically.value
    }

    var isAddingNote by mutableStateOf(false)

    // - Step 3 Удалите поле "title" класса Note, пусть заметка сохраняет только текст
    // fun addNote(title: String, text: String, emoji: String) {
    //    viewModelScope.launch {
    //        val currentTimeData = System.currentTimeMillis()
    //        val newNote = Note(
    //            title = title,
    //            text = text,
    //            date = currentTimeData,
    //            emoji = emoji
    //        )
    //        dao.insertNote(newNote)
    //        isAddingNote = false
    //    }
    // }

    // + Step 3 Удалите поле "title" класса Note, пусть заметка сохраняет только текст
    fun addNote(text: String, emoji: String) {
        viewModelScope.launch {
            val currentTimeData = System.currentTimeMillis()
            val newNote = Note(
                text = text,
                date = currentTimeData,
                emoji = emoji
            )
            dao.insertNote(newNote)
            isAddingNote = false
        }
    }

    // + Step 1 Добавьте кнопку удаления заметки на элемент списка заметок
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note)
        }
    }

    // + Step 2 Добавьте кнопку очистки базы данных - удаления всех заметок
    fun clearAllNotes() {
        viewModelScope.launch {
            dao.deleteAllNotes()
        }
    }
}
