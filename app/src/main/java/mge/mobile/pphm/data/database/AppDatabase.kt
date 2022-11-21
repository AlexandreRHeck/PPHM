package mge.mobile.pphm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mge.mobile.pphm.data.dao.SupremeDao
import mge.mobile.pphm.data.model.ConsumerUnit
import mge.mobile.pphm.data.model.Equipment
import mge.mobile.pphm.data.model.Search

/**
 * @author Gilson Graeter
 * @since 31/01/2022
 */
@Database(
    entities = [ConsumerUnit::class, Equipment::class, Search::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun supremeDao(): SupremeDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "m_database").allowMainThreadQueries().build()
            }

            return instance as AppDatabase
        }
    }
}