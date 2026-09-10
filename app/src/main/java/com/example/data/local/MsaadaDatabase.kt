package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ChurchEventItem
import com.example.data.model.ExamItem
import com.example.data.model.FavoriteToolItem
import com.example.data.model.PrayerItem
import com.example.data.model.RecentToolItem
import com.example.data.model.SavedDocument
import com.example.data.model.ShoppingItem
import com.example.data.model.StudyPlanItem
import com.example.data.model.RisitiSafeReceipt
import com.example.data.model.UmemeMeterReading
import com.example.data.model.SavedFormProfile
import com.example.data.model.SavedContract
import com.example.data.model.KikobaGroup
import com.example.data.model.BiasharaProduct

@Database(
    entities = [
        ShoppingItem::class,
        PrayerItem::class,
        SavedDocument::class,
        StudyPlanItem::class,
        ExamItem::class,
        ChurchEventItem::class,
        RecentToolItem::class,
        FavoriteToolItem::class,
        RisitiSafeReceipt::class,
        UmemeMeterReading::class,
        SavedFormProfile::class,
        SavedContract::class,
        KikobaGroup::class,
        BiasharaProduct::class
    ],
    version = 4,
    exportSchema = false
)
abstract class MsaadaDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE saved_documents ADD COLUMN fileUri TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE saved_documents ADD COLUMN fileMimeType TEXT NOT NULL DEFAULT 'application/pdf'")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE biashara_products ADD COLUMN fixedCosts REAL NOT NULL DEFAULT 0.0")
            }
        }

        @Volatile
        private var INSTANCE: MsaadaDatabase? = null

        fun getDatabase(context: Context): MsaadaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MsaadaDatabase::class.java,
                    "msaada_database.db"
                )
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
