package com.example.android.unscramble.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.*

suspend fun initialLoader(context: Context){
    val wordDao = WordDatabase.getInstance(context = context)?.wordDao()

    allWords.forEach(){
        wordDao!!.insert(Word(word = it))
    }
}

//Nuestra Room Database
@Database(entities = [Word::class], version = 1)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        private const val DATABASE_NAME = "words_database"

        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getInstance(context: Context): WordDatabase? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    DATABASE_NAME
                    )
                    .addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase){
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                initialLoader(context)
                            }

                        }
                    })
                    .build()
            }
            return INSTANCE
        }
    }

}