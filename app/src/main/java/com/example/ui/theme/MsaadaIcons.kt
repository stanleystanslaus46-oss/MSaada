package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Agriculture
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Church
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FolderCopy
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Grading
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.Portrait
import androidx.compose.material.icons.outlined.PriceCheck
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.RequestQuote
import androidx.compose.material.icons.outlined.RotateRight
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material.icons.outlined.TextSnippet
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material.icons.outlined.Yard
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized MSAADA Icon System
 *
 * Unified rounded-outline, modern, welcoming, consistent icon language
 * adhering to Navy (#001848) and Teal (#008C95) brand identity
 * with subtle tinted containers and theme adaptability.
 */
object MsaadaIcons {
    // ==========================================
    // 1. CATEGORY ICONS
    // ==========================================
    /** Fedha: Modern wallet + coin */
    val Finance: ImageVector = Icons.Outlined.AccountBalanceWallet

    /** Biashara: Modern storefront / business briefcase */
    val Business: ImageVector = Icons.Outlined.Storefront

    /** Picha & Nyaraka: Camera + document / portrait ID */
    val Documents: ImageVector = Icons.Outlined.DocumentScanner

    /** Elimu: Open book + academic detail */
    val Education: ImageVector = Icons.Outlined.AutoStories

    /** Kanisa: Simple welcoming church/building */
    val Church: ImageVector = Icons.Outlined.Church

    /** Nyumbani: Rounded home */
    val Home: ImageVector = Icons.Outlined.Home

    /** Usafiri: Modern car */
    val Transport: ImageVector = Icons.Outlined.DirectionsCar

    /** Afya: Medical shield / health symbol */
    val Health: ImageVector = Icons.Outlined.HealthAndSafety

    /** Kilimo: Leaf / plant with agricultural detail */
    val Agriculture: ImageVector = Icons.Outlined.Eco

    /** Teknolojia: Laptop / device + code detail */
    val Technology: ImageVector = Icons.Outlined.Devices

    // ==========================================
    // 2. QUICK TOOLS
    // ==========================================
    /** Bajeti: Wallet + budget/coin detail */
    val Budget: ImageVector = Icons.Outlined.AccountBalanceWallet

    /** Calculator: Modern rounded calculator */
    val Calculator: ImageVector = Icons.Outlined.Calculate

    /** Faida: Growth chart / upward trend */
    val Profit: ImageVector = Icons.Outlined.TrendingUp

    /** QR Code: Modern rounded QR icon */
    val QR: ImageVector = Icons.Outlined.QrCodeScanner

    /** Passport Photo: Camera + ID/document photo */
    val PassportPhoto: ImageVector = Icons.Outlined.Portrait

    // ==========================================
    // 3. NEW TOOLS
    // ==========================================
    /** RisitiSafe: Receipt/document + check */
    val ReceiptSafe: ImageVector = Icons.Outlined.ReceiptLong

    /** Umeme Calculator: Electric meter / lightning + calculation */
    val Electricity: ImageVector = Icons.Outlined.ElectricBolt

    /** Msaidizi wa Fomu: Document + pencil/check */
    val FormAssistant: ImageVector = Icons.Outlined.Assignment

    /** Mkataba Rahisi: Contract/document + pen */
    val SimpleContract: ImageVector = Icons.Outlined.HistoryEdu

    /** Kikoba Calculator: Group/people + coins/savings */
    val Kikoba: ImageVector = Icons.Outlined.Groups

    /** Biashara Calculator: Calculator + growth chart */
    val BusinessCalculator: ImageVector = Icons.Outlined.PointOfSale

    /** PRO: Refined premium badge/crown/spark icon (NO emoji stars) */
    val Pro: ImageVector = Icons.Outlined.WorkspacePremium

    // ==========================================
    // 4. EXISTING TOOLS MAPPINGS
    // ==========================================
    val Percentage: ImageVector = Icons.Outlined.Percent
    val Savings: ImageVector = Icons.Outlined.Savings
    val Loan: ImageVector = Icons.Outlined.CreditCard
    val Discount: ImageVector = Icons.Outlined.LocalOffer
    val Markup: ImageVector = Icons.Outlined.ShowChart
    val Invoice: ImageVector = Icons.Outlined.ReceiptLong
    val Quotation: ImageVector = Icons.Outlined.RequestQuote
    val Receipt: ImageVector = Icons.Outlined.Receipt
    val ExpenseReport: ImageVector = Icons.Outlined.Assessment
    val BusinessReport: ImageVector = Icons.Outlined.InsertChart
    val TemplatesHub: ImageVector = Icons.Outlined.FolderCopy
    val Grade: ImageVector = Icons.Outlined.Grading
    val Timer: ImageVector = Icons.Outlined.Timer
    val ExamCountdown: ImageVector = Icons.Outlined.CalendarToday
    val UnitConverter: ImageVector = Icons.Outlined.Straighten
    val StudyPlanner: ImageVector = Icons.Outlined.MenuBook
    val Tithe: ImageVector = Icons.Outlined.VolunteerActivism
    val PrayerList: ImageVector = Icons.Outlined.FavoriteBorder
    val ChurchPlanner: ImageVector = Icons.Outlined.Church
    val BiblePlanner: ImageVector = Icons.Outlined.AutoStories
    val ShoppingList: ImageVector = Icons.Outlined.ShoppingCart
    val HomeExpenses: ImageVector = Icons.Outlined.Home
    val Fuel: ImageVector = Icons.Outlined.LocalGasStation
    val TripSplit: ImageVector = Icons.Outlined.Group
    val TravelChecklist: ImageVector = Icons.Outlined.Luggage
    val Medication: ImageVector = Icons.Outlined.Medication
    val EmergencyContacts: ImageVector = Icons.Outlined.Emergency
    val FirstAid: ImageVector = Icons.Outlined.MedicalServices
    val FarmProfit: ImageVector = Icons.Outlined.MonetizationOn
    val FarmTasks: ImageVector = Icons.Outlined.Agriculture
    val PlantingPlanner: ImageVector = Icons.Outlined.Yard
    val PasswordGen: ImageVector = Icons.Outlined.Lock
    val TextCounter: ImageVector = Icons.Outlined.TextSnippet
    val TechUtilities: ImageVector = Icons.Outlined.Build
    val QrGenerator: ImageVector = Icons.Outlined.QrCode2

    // ==========================================
    // 5. NAVIGATION ICONS
    // ==========================================
    val NavHome: ImageVector = Icons.Outlined.Home
    val NavCategories: ImageVector = Icons.Outlined.GridView
    val NavFavorites: ImageVector = Icons.Outlined.FavoriteBorder
    val NavFavoritesActive: ImageVector = Icons.Outlined.Favorite
    val NavSettings: ImageVector = Icons.Outlined.Settings

    // ==========================================
    // 6. COMMON ACTION ICONS (STANDARDIZED)
    // ==========================================
    val Back: ImageVector = Icons.AutoMirrored.Outlined.ArrowBack
    val Search: ImageVector = Icons.Outlined.Search
    val SearchOff: ImageVector = Icons.Outlined.SearchOff
    val Favorites: ImageVector = Icons.Outlined.Favorite
    val FavoritesBorder: ImageVector = Icons.Outlined.FavoriteBorder
    val Settings: ImageVector = Icons.Outlined.Settings
    val Add: ImageVector = Icons.Outlined.Add
    val Close: ImageVector = Icons.Outlined.Close
    val Save: ImageVector = Icons.Outlined.Save
    val SaveFile: ImageVector = Icons.Outlined.Save
    val Bookmark: ImageVector = Icons.Outlined.Bookmark
    val Edit: ImageVector = Icons.Outlined.Edit
    val Delete: ImageVector = Icons.Outlined.Delete
    val Share: ImageVector = Icons.Outlined.Share
    val Download: ImageVector = Icons.Outlined.Download
    val Upload: ImageVector = Icons.Outlined.Upload
    val Refresh: ImageVector = Icons.Outlined.Refresh
    val Check: ImageVector = Icons.Outlined.Check
    val CheckCircle: ImageVector = Icons.Outlined.CheckCircle
    val Warning: ImageVector = Icons.Outlined.Warning
    val Info: ImageVector = Icons.Outlined.Info
    val Calendar: ImageVector = Icons.Outlined.CalendarToday
    val Clock: ImageVector = Icons.Outlined.Schedule
    val More: ImageVector = Icons.Outlined.MoreVert
    val Menu: ImageVector = Icons.Outlined.Menu
    val Copy: ImageVector = Icons.Outlined.ContentCopy
    val Visibility: ImageVector = Icons.Outlined.Visibility
    val ChevronRight: ImageVector = Icons.AutoMirrored.Outlined.NavigateNext
    val Pdf: ImageVector = Icons.Outlined.PictureAsPdf
    val Camera: ImageVector = Icons.Outlined.PhotoCamera
    val Crop: ImageVector = Icons.Outlined.Crop
    val Print: ImageVector = Icons.Outlined.Print
    val Inbox: ImageVector = Icons.Outlined.Inbox
    val HistoryIcon: ImageVector = Icons.Outlined.History
    val History: ImageVector = Icons.Outlined.History
    val PhotoLibrary: ImageVector = Icons.Outlined.PhotoLibrary
    val RotateRight: ImageVector = Icons.Outlined.RotateRight
    val ZoomIn: ImageVector = Icons.Outlined.ZoomIn
    val RadioUnchecked: ImageVector = Icons.Outlined.RadioButtonUnchecked
    val Gavel: ImageVector = Icons.Outlined.Gavel
    val Person: ImageVector = Icons.Outlined.Person
    val Work: ImageVector = Icons.Outlined.Work
    val PriceCheck: ImageVector = Icons.Outlined.PriceCheck
    val Speed: ImageVector = Icons.Outlined.Speed
    val EventNote: ImageVector = Icons.Outlined.EventNote
    val Summarize: ImageVector = Icons.Outlined.Summarize

    // Settings
    val Theme: ImageVector = Icons.Outlined.DarkMode
    val ThemeLight: ImageVector = Icons.Outlined.LightMode
    val DarkMode: ImageVector = Icons.Outlined.DarkMode
    val Language: ImageVector = Icons.Outlined.Language
    val Notifications: ImageVector = Icons.Outlined.Notifications
    val Storage: ImageVector = Icons.Outlined.Storage
    val Privacy: ImageVector = Icons.Outlined.Security
    val ClearData: ImageVector = Icons.Outlined.DeleteSweep

    // Onboarding
    val OnboardingApps: ImageVector = Icons.Outlined.GridView
    val OnboardingTools: ImageVector = Icons.Outlined.Handyman
    val OnboardingDocs: ImageVector = Icons.Outlined.Description
    val AllTools: ImageVector = Icons.Outlined.GridView
    val Tools: ImageVector = Icons.Outlined.Handyman
}

/**
 * Standardized MSAADA Icon Container
 *
 * Renders a welcoming, consistent icon container with subtle tinting,
 * rounded corners, proper optical margins, and theme-adaptive colors.
 */
@Composable
fun MsaadaIconBox(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    containerSize: Dp = 44.dp,
    tint: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MsaadaTeal.copy(alpha = 0.08f),
    shape: Shape = RoundedCornerShape(10.dp)
) {
    Box(
        modifier = modifier
            .size(containerSize)
            .clip(shape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}
