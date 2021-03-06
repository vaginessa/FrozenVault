package net.frozendevelopment.frozenvault.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.frozendevelopment.frozenvault.data.converters.DateConverter
import net.frozendevelopment.frozenvault.data.converters.ListConverter
import net.frozendevelopment.frozenvault.data.converters.SecurityQuestionConverter
import net.frozendevelopment.frozenvault.data.converters.UnLockEventTypeConverter
import net.frozendevelopment.frozenvault.data.daos.ServicePasswordDao
import net.frozendevelopment.frozenvault.data.daos.UnlockEventDao
import net.frozendevelopment.frozenvault.data.models.ServicePasswordModel
import net.frozendevelopment.frozenvault.data.models.UnlockEventModel

@TypeConverters(
    DateConverter::class,
    ListConverter::class,
    UnLockEventTypeConverter::class,
    SecurityQuestionConverter::class)
@Database(entities = [ServicePasswordModel::class, UnlockEventModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun servicePasswordDao() : ServicePasswordDao
    abstract fun unlockEventDao(): UnlockEventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE != null) {
                return INSTANCE!!
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "frozenpasswords_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
