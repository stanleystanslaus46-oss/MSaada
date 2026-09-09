package com.example.data.repository

import com.example.data.local.AppDao
import com.example.data.local.AppPreferences
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

class AppRepository(
    private val dao: AppDao,
    val preferences: AppPreferences
) {
    // Shopping
    val shoppingItems: Flow<List<ShoppingItem>> = dao.getAllShoppingItems()
    suspend fun addShoppingItem(title: String, quantity: String, category: String) =
        dao.insertShoppingItem(ShoppingItem(title = title, quantity = quantity, category = category))
    suspend fun updateShoppingItem(item: ShoppingItem) = dao.updateShoppingItem(item)
    suspend fun deleteShoppingItem(item: ShoppingItem) = dao.deleteShoppingItem(item)
    suspend fun clearCompletedShopping() = dao.clearCompletedShoppingItems()

    // Prayer
    val prayerItems: Flow<List<PrayerItem>> = dao.getAllPrayerItems()
    suspend fun addPrayerItem(title: String, description: String, date: String) =
        dao.insertPrayerItem(PrayerItem(title = title, description = description, date = date))
    suspend fun updatePrayerItem(item: PrayerItem) = dao.updatePrayerItem(item)
    suspend fun deletePrayerItem(item: PrayerItem) = dao.deletePrayerItem(item)

    // Saved Documents
    val savedDocuments: Flow<List<SavedDocument>> = dao.getAllDocuments()
    suspend fun getDocument(id: Long) = dao.getDocumentById(id)
    suspend fun saveDocument(doc: SavedDocument) = dao.insertDocument(doc)
    suspend fun deleteDocument(doc: SavedDocument) = dao.deleteDocument(doc)

    // Study
    val studyItems: Flow<List<StudyPlanItem>> = dao.getAllStudyItems()
    suspend fun addStudyItem(subject: String, task: String, dueDate: String) =
        dao.insertStudyItem(StudyPlanItem(subject = subject, task = task, dueDate = dueDate))
    suspend fun updateStudyItem(item: StudyPlanItem) = dao.updateStudyItem(item)
    suspend fun deleteStudyItem(item: StudyPlanItem) = dao.deleteStudyItem(item)

    // Exams
    val examItems: Flow<List<ExamItem>> = dao.getAllExamItems()
    suspend fun addExam(name: String, date: String, notes: String) =
        dao.insertExamItem(ExamItem(name = name, examDate = date, notes = notes))
    suspend fun deleteExam(item: ExamItem) = dao.deleteExamItem(item)

    // Church Events
    val churchEvents: Flow<List<ChurchEventItem>> = dao.getAllChurchEvents()
    suspend fun addChurchEvent(title: String, date: String, time: String, location: String, notes: String) =
        dao.insertChurchEvent(ChurchEventItem(title = title, date = date, time = time, location = location, notes = notes))
    suspend fun deleteChurchEvent(item: ChurchEventItem) = dao.deleteChurchEvent(item)

    // Favorites
    val favorites: Flow<List<FavoriteToolItem>> = dao.getAllFavorites()
    fun isFavorite(toolId: String): Flow<Boolean> = dao.isFavorite(toolId)
    suspend fun toggleFavorite(toolId: String, currentFav: Boolean) {
        if (currentFav) {
            dao.removeFavorite(toolId)
        } else {
            dao.addFavorite(FavoriteToolItem(toolId = toolId))
        }
    }

    // Recent Tools
    val recentTools: Flow<List<RecentToolItem>> = dao.getRecentTools()
    suspend fun logToolUsage(toolId: String) {
        dao.recordRecentTool(RecentToolItem(toolId = toolId, lastUsedTimestamp = System.currentTimeMillis()))
    }

    // RisitiSafe Receipts
    val receipts: Flow<List<RisitiSafeReceipt>> = dao.getAllReceipts()
    val receiptCount: Flow<Int> = dao.getReceiptCount()
    suspend fun saveReceipt(receipt: RisitiSafeReceipt) = dao.insertReceipt(receipt)
    suspend fun deleteReceipt(receipt: RisitiSafeReceipt) = dao.deleteReceipt(receipt)

    // Umeme Meter Readings
    val umemeReadings: Flow<List<UmemeMeterReading>> = dao.getAllUmemeReadings()
    suspend fun saveUmemeReading(reading: UmemeMeterReading) = dao.insertUmemeReading(reading)
    suspend fun deleteUmemeReading(reading: UmemeMeterReading) = dao.deleteUmemeReading(reading)

    // Saved Forms
    val savedForms: Flow<List<SavedFormProfile>> = dao.getAllSavedForms()
    suspend fun saveFormProfile(form: SavedFormProfile) = dao.insertSavedForm(form)
    suspend fun deleteFormProfile(form: SavedFormProfile) = dao.deleteSavedForm(form)

    // Saved Contracts
    val savedContracts: Flow<List<SavedContract>> = dao.getAllSavedContracts()
    suspend fun saveContract(contract: SavedContract) = dao.insertSavedContract(contract)
    suspend fun deleteContract(contract: SavedContract) = dao.deleteSavedContract(contract)

    // Kikoba Groups
    val kikobaGroups: Flow<List<KikobaGroup>> = dao.getAllKikobaGroups()
    suspend fun saveKikobaGroup(group: KikobaGroup) = dao.insertKikobaGroup(group)
    suspend fun updateKikobaGroup(group: KikobaGroup) = dao.updateKikobaGroup(group)
    suspend fun deleteKikobaGroup(group: KikobaGroup) = dao.deleteKikobaGroup(group)

    // Biashara Products
    val biasharaProducts: Flow<List<BiasharaProduct>> = dao.getAllBiasharaProducts()
    suspend fun saveBiasharaProduct(product: BiasharaProduct) = dao.insertBiasharaProduct(product)
    suspend fun deleteBiasharaProduct(product: BiasharaProduct) = dao.deleteBiasharaProduct(product)

    // Clear all
    suspend fun clearAllData() {
        dao.nukeShopping()
        dao.nukePrayer()
        dao.nukeDocuments()
        dao.nukeStudy()
        dao.nukeExams()
        dao.nukeChurchEvents()
        dao.nukeReceipts()
        dao.nukeUmemeReadings()
        dao.nukeSavedForms()
        dao.nukeSavedContracts()
        dao.nukeKikobaGroups()
        dao.nukeBiasharaProducts()
        dao.nukeFavorites()
        dao.clearRecentTools()
        preferences.clearAll()
    }
}
