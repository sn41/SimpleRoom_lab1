package kz.misal.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val content: String,

    // todo 2: добавим поле для даты
    // val date: Long = 0, // Храним время в ms
    // todo 3: добавим поле емодзи
    // val emoji: String = "📝" // Новое поле
)