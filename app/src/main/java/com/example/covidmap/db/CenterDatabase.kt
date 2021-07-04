package com.example.covidmap.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.covidmap.COVIDMapApplication
import com.example.covidmap.constant.COVIDConstants.CENTER_DB
import com.example.covidmap.model.Center

/**
 * CenterDatabase
 */
@Database(
    entities = [
        Center::class,
    ],
    version = 1
)
abstract class CenterDatabase : RoomDatabase() {
    abstract fun getDao(): CenterDao

    companion object {
        private var DB_INSTANCE: CenterDatabase? = null

        fun getDatabase(): CenterDatabase {
            val tempInstance = DB_INSTANCE
            tempInstance?.let {
                return it
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    COVIDMapApplication.getApplicationContext(),
                    CenterDatabase::class.java,
                    CENTER_DB
                ).build()

                DB_INSTANCE = instance
                return instance
            }
        }
    }
}