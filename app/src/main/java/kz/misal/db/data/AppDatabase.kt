package kz.misal.db.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE notes ADD COLUMN emoji TEXT NOT NULL DEFAULT '📝'")
    }
}

// + Step 4 Выполните миграцию на новую версию базы данных
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Создаем новую таблицу без поля title
        db.execSQL("CREATE TABLE notes_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, text TEXT NOT NULL, date INTEGER NOT NULL, emoji TEXT NOT NULL DEFAULT '📝')")
        // Копируем данные из старой таблицы в новую
        db.execSQL("INSERT INTO notes_new (id, text, date, emoji) SELECT id, text, date, emoji FROM notes")
        // Удаляем старую таблицу
        db.execSQL("DROP TABLE notes")
        // Переименовываем новую таблицу
        db.execSQL("ALTER TABLE notes_new RENAME TO notes")
    }
}

// - Step 4 Выполните миграцию на новую версию базы данных
// @Database(entities = [Note::class], version = 2)
// + Step 4 Выполните миграцию на новую версию базы данных
@Database(entities = [Note::class], version = 3)
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
                    // - Step 4 Выполните миграцию на новую версию базы данных
                    // .addMigrations(MIGRATION_1_2)
                    // + Step 4 Выполните миграцию на новую версию базы данных
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
