package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Shopping
    @Query("SELECT * FROM shopping_items ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllShoppingItems(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItem): Long

    @Update
    suspend fun updateShoppingItem(item: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE isCompleted = 1")
    suspend fun clearCompletedShoppingItems()

    @Query("DELETE FROM shopping_items")
    suspend fun clearAllShoppingItems()

    // Prayer
    @Query("SELECT * FROM prayer_items ORDER BY createdAt DESC")
    fun getAllPrayerItems(): Flow<List<PrayerItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerItem(item: PrayerItem): Long

    @Update
    suspend fun updatePrayerItem(item: PrayerItem)

    @Delete
    suspend fun deletePrayerItem(item: PrayerItem)

    // Saved Documents
    @Query("SELECT * FROM saved_documents ORDER BY createdAt DESC")
    fun getAllDocuments(): Flow<List<SavedDocument>>

    @Query("SELECT * FROM saved_documents WHERE id = :id")
    suspend fun getDocumentById(id: Long): SavedDocument?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: SavedDocument): Long
    @Query("UPDATE saved_documents SET fileUri = :fileUri, fileMimeType = :fileMimeType WHERE id = :id")
    suspend fun updateDocumentFile(id: Long, fileUri: String, fileMimeType: String)

    @Delete
    suspend fun deleteDocument(doc: SavedDocument)

    // Study Plan
    @Query("SELECT * FROM study_items ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllStudyItems(): Flow<List<StudyPlanItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyItem(item: StudyPlanItem): Long

    @Update
    suspend fun updateStudyItem(item: StudyPlanItem)

    @Delete
    suspend fun deleteStudyItem(item: StudyPlanItem)

    // Exam Countdowns
    @Query("SELECT * FROM exam_items ORDER BY examDate ASC")
    fun getAllExamItems(): Flow<List<ExamItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamItem(item: ExamItem): Long

    @Delete
    suspend fun deleteExamItem(item: ExamItem)

    // Church Events
    @Query("SELECT * FROM church_events ORDER BY date ASC, time ASC")
    fun getAllChurchEvents(): Flow<List<ChurchEventItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChurchEvent(event: ChurchEventItem): Long

    @Delete
    suspend fun deleteChurchEvent(event: ChurchEventItem)

    // Favorites
    @Query("SELECT * FROM favorite_tools")
    fun getAllFavorites(): Flow<List<FavoriteToolItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(fav: FavoriteToolItem)

    @Query("DELETE FROM favorite_tools WHERE toolId = :toolId")
    suspend fun removeFavorite(toolId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tools WHERE toolId = :toolId)")
    fun isFavorite(toolId: String): Flow<Boolean>

    // Recent Tools
    @Query("SELECT * FROM recent_tools ORDER BY lastUsedTimestamp DESC LIMIT 10")
    fun getRecentTools(): Flow<List<RecentToolItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordRecentTool(item: RecentToolItem)

    @Query("DELETE FROM recent_tools")
    suspend fun clearRecentTools()

    // Clear all user tables
    @Query("DELETE FROM shopping_items")
    suspend fun nukeShopping()

    @Query("DELETE FROM prayer_items")
    suspend fun nukePrayer()

    @Query("DELETE FROM saved_documents")
    suspend fun nukeDocuments()

    @Query("DELETE FROM study_items")
    suspend fun nukeStudy()

    @Query("DELETE FROM exam_items")
    suspend fun nukeExams()

    @Query("DELETE FROM church_events")
    suspend fun nukeChurchEvents()

    @Query("DELETE FROM risitisafe_receipts")
    suspend fun nukeReceipts()

    @Query("DELETE FROM umeme_readings")
    suspend fun nukeUmemeReadings()

    @Query("DELETE FROM saved_forms")
    suspend fun nukeSavedForms()

    @Query("DELETE FROM saved_contracts")
    suspend fun nukeSavedContracts()

    @Query("DELETE FROM kikoba_groups")
    suspend fun nukeKikobaGroups()

    @Query("DELETE FROM biashara_products")
    suspend fun nukeBiasharaProducts()

    @Query("DELETE FROM favorite_tools")
    suspend fun nukeFavorites()

    // RisitiSafe Receipts
    @Query("SELECT * FROM risitisafe_receipts ORDER BY createdAt DESC")
    fun getAllReceipts(): Flow<List<RisitiSafeReceipt>>

    @Query("SELECT COUNT(*) FROM risitisafe_receipts")
    fun getReceiptCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: RisitiSafeReceipt): Long

    @Delete
    suspend fun deleteReceipt(receipt: RisitiSafeReceipt)

    // Umeme Meter Readings
    @Query("SELECT * FROM umeme_readings ORDER BY createdAt DESC")
    fun getAllUmemeReadings(): Flow<List<UmemeMeterReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUmemeReading(reading: UmemeMeterReading): Long

    @Delete
    suspend fun deleteUmemeReading(reading: UmemeMeterReading)

    // Saved Forms
    @Query("SELECT * FROM saved_forms ORDER BY createdAt DESC")
    fun getAllSavedForms(): Flow<List<SavedFormProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedForm(form: SavedFormProfile): Long

    @Delete
    suspend fun deleteSavedForm(form: SavedFormProfile)

    // Saved Contracts
    @Query("SELECT * FROM saved_contracts ORDER BY createdAt DESC")
    fun getAllSavedContracts(): Flow<List<SavedContract>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedContract(contract: SavedContract): Long

    @Delete
    suspend fun deleteSavedContract(contract: SavedContract)

    // Kikoba Groups
    @Query("SELECT * FROM kikoba_groups ORDER BY createdAt DESC")
    fun getAllKikobaGroups(): Flow<List<KikobaGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKikobaGroup(group: KikobaGroup): Long

    @Update
    suspend fun updateKikobaGroup(group: KikobaGroup)

    @Delete
    suspend fun deleteKikobaGroup(group: KikobaGroup)

    // Biashara Products
    @Query("SELECT * FROM biashara_products ORDER BY createdAt DESC")
    fun getAllBiasharaProducts(): Flow<List<BiasharaProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBiasharaProduct(product: BiasharaProduct): Long

    @Delete
    suspend fun deleteBiasharaProduct(product: BiasharaProduct)
}
