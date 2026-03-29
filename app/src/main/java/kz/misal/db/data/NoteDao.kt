package kz.misal.db.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    // + Step 2 Добавьте кнопку очистки базы данных - удаления всех заметок
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    // + Step 5 Добавьте кнопку сортировки элементов по алфавиту
    @Query("SELECT * FROM notes ORDER BY text ASC")
    fun getAllNotesSortedByText(): Flow<List<Note>>
}
