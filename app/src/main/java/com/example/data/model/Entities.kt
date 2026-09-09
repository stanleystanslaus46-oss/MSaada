package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val quantity: String = "1",
    val category: String = "Jumla",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "prayer_items")
data class PrayerItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val date: String = "",
    val isAnswered: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_documents")
data class SavedDocument(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val docType: String, // INVOICE, QUOTATION, RECEIPT, EXPENSE, REPORT
    val title: String,
    val customerOrClient: String = "",
    val totalAmount: Double = 0.0,
    val contentJson: String = "",
    val isProTemplate: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_items")
data class StudyPlanItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val task: String,
    val dueDate: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exam_items")
data class ExamItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val examDate: String,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "church_events")
data class ChurchEventItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: String,
    val time: String = "",
    val location: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "recent_tools")
data class RecentToolItem(
    @PrimaryKey val toolId: String,
    val lastUsedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_tools")
data class FavoriteToolItem(
    @PrimaryKey val toolId: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "risitisafe_receipts")
data class RisitiSafeReceipt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val receiptNumber: String,
    val businessName: String,
    val businessPhone: String,
    val businessAddress: String,
    val customerName: String,
    val customerPhone: String,
    val date: String,
    val itemsJson: String,
    val subtotal: Double,
    val total: Double,
    val amountPaid: Double,
    val balance: Double,
    val paymentMethod: String,
    val notes: String = "",
    val template: String = "modern",
    val isPro: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "umeme_readings")
data class UmemeMeterReading(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val previousReading: Double,
    val currentReading: Double,
    val unitsUsed: Double,
    val pricePerUnit: Double,
    val estimatedCost: Double,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_forms")
data class SavedFormProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: String,
    val templateTitle: String,
    val category: String,
    val profileName: String,
    val fieldsJson: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_contracts")
data class SavedContract(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contractType: String,
    val title: String,
    val partyA: String,
    val partyB: String,
    val fieldsJson: String,
    val fullText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "kikoba_groups")
data class KikobaGroup(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val groupName: String,
    val memberCount: Int,
    val contributionAmount: Double,
    val frequency: String,
    val roundsCount: Int,
    val totalPool: Double,
    val payoutPerMember: Double,
    val membersJson: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "biashara_products")
data class BiasharaProduct(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productName: String,
    val buyingPrice: Double,
    val transportCost: Double,
    val packagingCost: Double,
    val otherCost: Double,
    val sellingPrice: Double,
    val quantity: Double,
    val totalCostPerUnit: Double,
    val totalInvestment: Double,
    val expectedRevenue: Double,
    val profitPerUnit: Double,
    val totalProfit: Double,
    val markupPercentage: Double,
    val profitMarginPercentage: Double,
    val breakEvenPrice: Double,
    val fixedCosts: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
