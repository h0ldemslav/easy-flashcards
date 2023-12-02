package cz.mendelu.pef.flashyflashcards.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.pef.flashyflashcards.database.wordcollections.WordCollectionsDao
import cz.mendelu.pef.flashyflashcards.model.WordCollectionEntity

@Database(
    entities = [WordCollectionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FlashyFlashcardsDatabase : RoomDatabase() {

    abstract fun wordCollectionsDao(): WordCollectionsDao

    companion object {

        private var INSTANCE: FlashyFlashcardsDatabase? = null

        fun getDatabase(context: Context): FlashyFlashcardsDatabase {
            if (INSTANCE == null) {
                synchronized(FlashyFlashcardsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            FlashyFlashcardsDatabase::class.java,
                            "flashy_flashcards_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}