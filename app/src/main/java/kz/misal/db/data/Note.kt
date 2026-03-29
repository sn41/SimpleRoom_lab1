package kz.misal.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // - Step 3 Удалите поле "title" класса Note, пусть заметка сохраняет только текст
    // val title: String,
    val text: String,
    val date: Long,
    val emoji: String = "📝"
)
