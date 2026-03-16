package kz.misal.db.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // todo 2 Измените порядок строк, по убыванию даты
    //@Query("SELECT * FROM notes ORDER BY date DESC")

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>> // Flow позволяет Compose автоматически следить за изменениями

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}

