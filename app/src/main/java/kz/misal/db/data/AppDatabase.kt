package kz.misal.db.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// todo 3 Добавим алгоритм миграции
//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        // Добавляем колонку emoji типа TEXT, которая не может быть null, со значением по умолчанию
//        db.execSQL("ALTER TABLE notes ADD COLUMN emoji TEXT NOT NULL DEFAULT '📝'")
//    }
//}



// 3. Класс базы данных
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
                    //.addMigrations(MIGRATION_1_2) // Указываем, как переходить
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}