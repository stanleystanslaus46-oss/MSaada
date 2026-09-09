package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.local.AppPreferences
import com.example.data.local.MsaadaDatabase
import com.example.data.model.ChurchEventItem
import com.example.data.model.ExamItem
import com.example.data.model.PrayerItem
import com.example.data.model.SavedDocument
import com.example.data.model.ShoppingItem
import com.example.data.model.StudyPlanItem
import com.example.data.model.RisitiSafeReceipt
import com.example.data.model.UmemeMeterReading
import com.example.data.model.SavedFormProfile
import com.example.data.model.SavedContract
import com.example.data.model.KikobaGroup
import com.example.data.model.BiasharaProduct
import com.example.data.repository.AppRepository
import com.example.domain.MsaadaTool
import com.example.domain.ToolRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MsaadaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = MsaadaDatabase.getDatabase(application)
    private val preferences = AppPreferences(application)
    val repository = AppRepository(database.appDao(), preferences)

    // User Preferences State
    val onboardingCompleted: StateFlow<Boolean> = preferences.onboardingCompleted
    val language: StateFlow<String> = preferences.language
    val themeMode: StateFlow<String> = preferences.themeMode
    val notificationsEnabled: StateFlow<Boolean> = preferences.notificationsEnabled
    val passportPhotosUsed: StateFlow<Int> = preferences.passportPhotosUsed
    val isProUser: StateFlow<Boolean> = preferences.isProUser

    val isSwahili: StateFlow<Boolean> = language.map { it == "sw" }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<MsaadaTool>> = _searchQuery.map { query ->
        ToolRegistry.searchTools(query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Favorites
    val favoriteToolIds: StateFlow<Set<String>> = repository.favorites.map { list ->
        list.map { it.toolId }.toSet()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    // Recent Tools
    val recentTools: StateFlow<List<MsaadaTool>> = repository.recentTools.map { recents ->
        recents.mapNotNull { recent -> ToolRegistry.getToolById(recent.toolId) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Shopping Items
    val shoppingItems: StateFlow<List<ShoppingItem>> = repository.shoppingItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Prayer Items
    val prayerItems: StateFlow<List<PrayerItem>> = repository.prayerItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Documents
    val savedDocuments: StateFlow<List<SavedDocument>> = repository.savedDocuments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Study
    val studyItems: StateFlow<List<StudyPlanItem>> = repository.studyItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Exams
    val examItems: StateFlow<List<ExamItem>> = repository.examItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Church Events
    val churchEvents: StateFlow<List<ChurchEventItem>> = repository.churchEvents.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // RisitiSafe Receipts
    val receipts: StateFlow<List<RisitiSafeReceipt>> = repository.receipts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val receiptCount: StateFlow<Int> = repository.receiptCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // Umeme Meter Readings
    val umemeReadings: StateFlow<List<UmemeMeterReading>> = repository.umemeReadings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Saved Forms
    val savedForms: StateFlow<List<SavedFormProfile>> = repository.savedForms.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Saved Contracts
    val savedContracts: StateFlow<List<SavedContract>> = repository.savedContracts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Kikoba Groups
    val kikobaGroups: StateFlow<List<KikobaGroup>> = repository.kikobaGroups.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Biashara Products
    val biasharaProducts: StateFlow<List<BiasharaProduct>> = repository.biasharaProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveReceipt(receipt: RisitiSafeReceipt, onSaved: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.saveReceipt(receipt)
            onSaved(id)
        }
    }

    fun deleteReceipt(receipt: RisitiSafeReceipt) {
        viewModelScope.launch {
            repository.deleteReceipt(receipt)
        }
    }

    fun saveUmemeReading(reading: UmemeMeterReading) {
        viewModelScope.launch {
            repository.saveUmemeReading(reading)
        }
    }

    fun deleteUmemeReading(reading: UmemeMeterReading) {
        viewModelScope.launch {
            repository.deleteUmemeReading(reading)
        }
    }

    fun saveFormProfile(form: SavedFormProfile) {
        viewModelScope.launch {
            repository.saveFormProfile(form)
        }
    }

    fun deleteFormProfile(form: SavedFormProfile) {
        viewModelScope.launch {
            repository.deleteFormProfile(form)
        }
    }

    fun saveContract(contract: SavedContract) {
        viewModelScope.launch {
            repository.saveContract(contract)
        }
    }

    fun deleteContract(contract: SavedContract) {
        viewModelScope.launch {
            repository.deleteContract(contract)
        }
    }

    fun saveKikobaGroup(group: KikobaGroup) {
        viewModelScope.launch {
            repository.saveKikobaGroup(group)
        }
    }

    fun updateKikobaGroup(group: KikobaGroup) {
        viewModelScope.launch {
            repository.updateKikobaGroup(group)
        }
    }

    fun deleteKikobaGroup(group: KikobaGroup) {
        viewModelScope.launch {
            repository.deleteKikobaGroup(group)
        }
    }

    fun saveBiasharaProduct(product: BiasharaProduct) {
        viewModelScope.launch {
            repository.saveBiasharaProduct(product)
        }
    }

    fun deleteBiasharaProduct(product: BiasharaProduct) {
        viewModelScope.launch {
            repository.deleteBiasharaProduct(product)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun completeOnboarding() {
        preferences.setOnboardingCompleted(true)
    }

    fun setLanguage(lang: String) {
        preferences.setLanguage(lang)
    }

    fun setThemeMode(mode: String) {
        preferences.setThemeMode(mode)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        preferences.setNotificationsEnabled(enabled)
    }

    fun toggleFavorite(toolId: String) {
        viewModelScope.launch {
            val isFav = favoriteToolIds.value.contains(toolId)
            repository.toggleFavorite(toolId, isFav)
        }
    }

    fun logToolUse(toolId: String) {
        viewModelScope.launch {
            repository.logToolUsage(toolId)
        }
    }

    /**
     * Developer-only entitlement toggle. Production builds must never mutate
     * the PRO entitlement through this path; real billing should own it.
     */
    fun toggleProUser(isPro: Boolean) {
        if (BuildConfig.DEBUG) {
            preferences.setIsProUser(isPro)
        }
    }

    fun applyVerifiedProEntitlement(isPro: Boolean, expiryTimeMillis: Long = 0L) {
        preferences.setVerifiedProEntitlement(isPro, expiryTimeMillis)
    }

    fun activateProFromCode(expiryTimeMillis: Long) {
        if (expiryTimeMillis <= System.currentTimeMillis()) return
        preferences.setVerifiedProEntitlement(true, expiryTimeMillis)
    }

    fun recordPassportGeneration(): Boolean {
        // returns true if allowed, false if limit reached and not pro
        if (isProUser.value) {
            // PRO users are unlimited; do not inflate the free-use counter.
            return true
        }
        val currentUsed = passportPhotosUsed.value
        if (currentUsed < 5) {
            preferences.incrementPassportPhotosUsed()
            return true
        }
        return false
    }

    // Shopping List Operations
    fun addShoppingItem(title: String, quantity: String, category: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addShoppingItem(title.trim(), quantity.trim(), category.trim())
        }
    }

    fun toggleShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.updateShoppingItem(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
        }
    }

    fun clearCompletedShopping() {
        viewModelScope.launch {
            repository.clearCompletedShopping()
        }
    }

    // Prayer Operations
    fun addPrayerItem(title: String, description: String, date: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addPrayerItem(title.trim(), description.trim(), date.trim())
        }
    }

    fun togglePrayerAnswered(item: PrayerItem) {
        viewModelScope.launch {
            repository.updatePrayerItem(item.copy(isAnswered = !item.isAnswered))
        }
    }

    fun deletePrayerItem(item: PrayerItem) {
        viewModelScope.launch {
            repository.deletePrayerItem(item)
        }
    }

    // Document Operations
    fun saveDocument(doc: SavedDocument, onSaved: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.saveDocument(doc)
            onSaved(id)
        }
    }

    fun deleteDocument(doc: SavedDocument) {
        viewModelScope.launch {
            repository.deleteDocument(doc)
        }
    }

    // Study Plan
    fun addStudyItem(subject: String, task: String, dueDate: String) {
        if (subject.isBlank() || task.isBlank()) return
        viewModelScope.launch {
            repository.addStudyItem(subject.trim(), task.trim(), dueDate.trim())
        }
    }

    fun toggleStudyCompleted(item: StudyPlanItem) {
        viewModelScope.launch {
            repository.updateStudyItem(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun deleteStudyItem(item: StudyPlanItem) {
        viewModelScope.launch {
            repository.deleteStudyItem(item)
        }
    }

    // Exam Countdown
    fun addExam(name: String, date: String, notes: String) {
        if (name.isBlank() || date.isBlank()) return
        viewModelScope.launch {
            repository.addExam(name.trim(), date.trim(), notes.trim())
        }
    }

    fun deleteExam(item: ExamItem) {
        viewModelScope.launch {
            repository.deleteExam(item)
        }
    }

    // Church Events
    fun addChurchEvent(title: String, date: String, time: String, location: String, notes: String) {
        if (title.isBlank() || date.isBlank()) return
        viewModelScope.launch {
            repository.addChurchEvent(title.trim(), date.trim(), time.trim(), location.trim(), notes.trim())
        }
    }

    fun deleteChurchEvent(item: ChurchEventItem) {
        viewModelScope.launch {
            repository.deleteChurchEvent(item)
        }
    }

    // Document Preview
    data class PreviewDocData(
        val docType: String,
        val title: String,
        val customer: String,
        val totalAmount: Double,
        val jsonContent: String
    )

    private val _previewDocData = MutableStateFlow<PreviewDocData?>(null)
    val previewDocData: StateFlow<PreviewDocData?> = _previewDocData.asStateFlow()

    fun setPreviewDoc(docType: String, title: String, customer: String, totalAmount: Double, jsonContent: String) {
        _previewDocData.value = PreviewDocData(docType, title, customer, totalAmount, jsonContent)
    }

    // Clear all local data
    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}
