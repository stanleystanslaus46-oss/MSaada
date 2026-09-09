package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.domain.MsaadaTool
import com.example.domain.StringsManager
import com.example.domain.ToolRegistry
import com.example.ui.MsaadaViewModel
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CategoryDetailScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MsaadaProScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.tools.agriculture.LandAreaCalculatorScreen
import com.example.ui.screens.tools.agriculture.SeedPlantingCalculatorScreen
import com.example.ui.screens.tools.business.BiasharaCalculatorScreen
import com.example.ui.screens.tools.business.DiscountCalculatorScreen
import com.example.ui.screens.tools.business.DocumentEditorScreen
import com.example.ui.screens.tools.business.DocumentPreviewScreen
import com.example.ui.screens.tools.business.MarkupCalculatorScreen
import com.example.ui.screens.tools.business.ProfitCalculatorScreen
import com.example.ui.screens.tools.business.RisitiSafeScreen
import com.example.ui.screens.tools.business.TemplatesScreen
import com.example.ui.screens.tools.documents.MkatabaRahisiScreen
import com.example.ui.screens.tools.documents.MsaidiziWaFomuScreen
import com.example.ui.screens.tools.finance.KikobaCalculatorScreen
import com.example.ui.screens.tools.home.UmemeCalculatorScreen
import com.example.ui.screens.tools.church.BibleReaderScreen
import com.example.ui.screens.tools.church.ChurchAnnouncementsScreen
import com.example.ui.screens.tools.church.HymnsScreen
import com.example.ui.screens.tools.church.PrayerRequestsScreen
import com.example.ui.screens.tools.education.ExamCountdownScreen
import com.example.ui.screens.tools.education.GpaCalculatorScreen
import com.example.ui.screens.tools.education.StudyPlannerScreen
import com.example.ui.screens.tools.finance.BudgetCalculatorScreen
import com.example.ui.screens.tools.finance.CalculatorScreen
import com.example.ui.screens.tools.finance.LoanCalculatorScreen
import com.example.ui.screens.tools.finance.PercentageCalculatorScreen
import com.example.ui.screens.tools.finance.SavingsCalculatorScreen
import com.example.ui.screens.tools.finance.TitheCalculatorScreen
import com.example.ui.screens.tools.health.BmiCalculatorScreen
import com.example.ui.screens.tools.health.WaterIntakeScreen
import com.example.ui.screens.tools.home.LukuCalculatorScreen
import com.example.ui.screens.tools.home.ShoppingListScreen
import com.example.ui.screens.tools.photo.PassportPhotoScreen
import com.example.ui.screens.tools.tech.DataUsageCalculatorScreen
import com.example.ui.screens.tools.tech.QrGeneratorScannerScreen
import com.example.ui.screens.tools.tech.UnitConverterScreen
import com.example.ui.screens.tools.transport.DistanceCalculatorScreen
import com.example.ui.screens.tools.transport.FuelCostCalculatorScreen
import com.example.ui.screens.tools.HomeExpenseTrackerScreen
import com.example.ui.screens.tools.StudyTimerScreen
import com.example.ui.screens.tools.TravelChecklistScreen
import com.example.ui.screens.tools.TripCostSplitterScreen
import com.example.ui.screens.tools.EmergencyContactsScreen
import com.example.ui.screens.tools.FirstAidScreen
import com.example.ui.screens.tools.MedicineReminderScreen
import com.example.ui.screens.tools.FarmTaskPlannerScreen
import com.example.ui.screens.tools.FarmProfitCalculatorScreen
import com.example.ui.screens.tools.PlantingPlannerScreen
import com.example.ui.screens.tools.PasswordGeneratorScreen
import com.example.ui.screens.tools.TextCounterScreen
import com.example.ui.screens.tools.TechnologyUtilitiesScreen
import com.example.ui.theme.MsaadaIcons
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MsaadaViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val isDark = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            MyApplicationTheme(darkTheme = isDark) {
                MsaadaApp(viewModel = viewModel)
            }
        }
    }

}

@Composable
fun MsaadaApp(viewModel: MsaadaViewModel) {
    val navController = rememberNavController()

    val isSwahili by viewModel.isSwahili.collectAsStateWithLifecycle()
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()
    val isProUser by viewModel.isProUser.collectAsStateWithLifecycle()
    val passportPhotosUsed by viewModel.passportPhotosUsed.collectAsStateWithLifecycle()
    val favoriteToolIds by viewModel.favoriteToolIds.collectAsStateWithLifecycle()
    val recentTools by viewModel.recentTools.collectAsStateWithLifecycle()

    // Persistent entities state
    val shoppingItems by viewModel.shoppingItems.collectAsStateWithLifecycle()
    val prayerItems by viewModel.prayerItems.collectAsStateWithLifecycle()
    val studyItems by viewModel.studyItems.collectAsStateWithLifecycle()
    val examItems by viewModel.examItems.collectAsStateWithLifecycle()
    val churchEvents by viewModel.churchEvents.collectAsStateWithLifecycle()
    val previewDocData by viewModel.previewDocData.collectAsStateWithLifecycle()

    val onToolNavigate: (MsaadaTool) -> Unit = { tool ->
        viewModel.logToolUse(tool.id)
        navController.navigate(tool.route)
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. SPLASH SCREEN
        composable("splash") {
            SplashScreen(
                onNavigateNext = { destination ->
                    val target = if (destination == "main_home") "main_container" else "onboarding"
                    navController.navigate(target) {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onboardingCompleted = onboardingCompleted
            )
        }

        // 2. ONBOARDING SCREEN
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    viewModel.completeOnboarding()
                    navController.navigate("main_container") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                isSwahili = isSwahili
            )
        }

        // 3. MAIN CONTAINER (BOTTOM NAV: HOME, CATEGORIES, FAVORITES, SETTINGS)
        composable("main_container") {
            MainContainerScreen(
                viewModel = viewModel,
                navController = navController,
                isSwahili = isSwahili,
                favoriteToolIds = favoriteToolIds,
                recentTools = recentTools,
                language = language,
                themeMode = themeMode,
                notificationsEnabled = notificationsEnabled,
                isProUser = isProUser,
                onToolClick = onToolNavigate
            )
        }

        // 4. CATEGORY DETAIL SCREEN
        composable(
            route = "category_detail/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "fedha"
            CategoryDetailScreen(
                categoryId = categoryId,
                isSwahili = isSwahili,
                favoriteToolIds = favoriteToolIds,
                onToolClick = onToolNavigate,
                onFavoriteToggle = { toolId -> viewModel.toggleFavorite(toolId) },
                onBack = { navController.popBackStack() }
            )
        }

        // 5. SEARCH SCREEN
        composable("search") {
            SearchScreen(
                isSwahili = isSwahili,
                favoriteToolIds = favoriteToolIds,
                onToolClick = onToolNavigate,
                onFavoriteToggle = { toolId -> viewModel.toggleFavorite(toolId) },
                onBack = { navController.popBackStack() }
            )
        }

        // 6. PRO UPGRADE SCREEN
        composable("pro") {
            MsaadaProScreen(
                isSwahili = isSwahili,
                isProUser = isProUser,
                photosUsed = passportPhotosUsed,
                onTogglePro = { proState -> viewModel.toggleProUser(proState) },
                onActivatePro = { expiryTimeMillis -> viewModel.activateProFromCode(expiryTimeMillis) },
                onClose = { navController.popBackStack() }
            )
        }

        // -------------------------------------------------------------
        // TOOLS ROUTES
        // -------------------------------------------------------------

        // PASSPORT & ID PHOTO
        composable("tool_passport_photo") {
            PassportPhotoScreen(
                isSwahili = isSwahili,
                isProUser = isProUser,
                photosUsed = passportPhotosUsed,
                onGenerateSuccess = { viewModel.recordPassportGeneration() },
                onOpenPro = { navController.navigate("pro") },
                onBack = { navController.popBackStack() }
            )
        }

        // FINANCE TOOLS
        composable("tool_calculator") {
            CalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_percentage") {
            PercentageCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_budget") {
            BudgetCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_home_expense") {
            HomeExpenseTrackerScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_savings") {
            SavingsCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_loan") {
            LoanCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_tithe") {
            TitheCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        // BUSINESS TOOLS
        composable("tool_profit") {
            ProfitCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_discount") {
            DiscountCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_markup") {
            MarkupCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        // DOCUMENT GENERATOR ROUTES
        composable("doc_invoice") {
            DocumentEditorScreen(
                docType = "INVOICE",
                isSwahili = isSwahili,
                isProUser = isProUser,
                onSaveDoc = { doc -> viewModel.saveDocument(doc) },
                onPreviewDoc = { docType, title, client, amount, json ->
                    viewModel.setPreviewDoc(docType, title, client, amount, json)
                    navController.navigate("doc_preview")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("doc_quotation") {
            DocumentEditorScreen(
                docType = "QUOTATION",
                isSwahili = isSwahili,
                isProUser = isProUser,
                onSaveDoc = { doc -> viewModel.saveDocument(doc) },
                onPreviewDoc = { docType, title, client, amount, json ->
                    viewModel.setPreviewDoc(docType, title, client, amount, json)
                    navController.navigate("doc_preview")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("doc_receipt") {
            DocumentEditorScreen(
                docType = "RECEIPT",
                isSwahili = isSwahili,
                isProUser = isProUser,
                onSaveDoc = { doc -> viewModel.saveDocument(doc) },
                onPreviewDoc = { docType, title, client, amount, json ->
                    viewModel.setPreviewDoc(docType, title, client, amount, json)
                    navController.navigate("doc_preview")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("doc_expense") {
            DocumentEditorScreen(
                docType = "EXPENSE",
                isSwahili = isSwahili,
                isProUser = isProUser,
                onSaveDoc = { doc -> viewModel.saveDocument(doc) },
                onPreviewDoc = { docType, title, client, amount, json ->
                    viewModel.setPreviewDoc(docType, title, client, amount, json)
                    navController.navigate("doc_preview")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("doc_report") {
            DocumentEditorScreen(
                docType = "REPORT",
                isSwahili = isSwahili,
                isProUser = isProUser,
                onSaveDoc = { doc -> viewModel.saveDocument(doc) },
                onPreviewDoc = { docType, title, client, amount, json ->
                    viewModel.setPreviewDoc(docType, title, client, amount, json)
                    navController.navigate("doc_preview")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("templates_hub") {
            TemplatesScreen(
                isSwahili = isSwahili,
                isProUser = isProUser,
                onOpenDocWithTemplate = { docType ->
                    val route = when (docType.uppercase()) {
                        "INVOICE" -> "doc_invoice"
                        "QUOTATION" -> "doc_quotation"
                        "RECEIPT" -> "doc_receipt"
                        "EXPENSE" -> "doc_expense"
                        else -> "doc_report"
                    }
                    navController.navigate(route)
                },
                onOpenPro = { navController.navigate("pro") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("doc_preview") {
            val doc = previewDocData
            if (doc != null) {
                DocumentPreviewScreen(
                    docType = doc.docType,
                    title = doc.title,
                    customer = doc.customer,
                    totalAmount = doc.totalAmount,
                    jsonContent = doc.jsonContent,
                    isSwahili = isSwahili,
                    isProUser = isProUser,
                    onOpenPro = { navController.navigate("pro") },
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        // EDUCATION TOOLS
        composable("tool_grade") {
            GpaCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_study_planner") {
            StudyPlannerScreen(
                sessions = studyItems,
                isSwahili = isSwahili,
                onAddSession = { subject, task, dueDate ->
                    viewModel.addStudyItem(subject, task, dueDate)
                },
                onToggleSession = { item -> viewModel.toggleStudyCompleted(item) },
                onDeleteSession = { item -> viewModel.deleteStudyItem(item) },
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_study_timer") {
            StudyTimerScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_exam_countdown") {
            ExamCountdownScreen(
                exams = examItems,
                isSwahili = isSwahili,
                onAddExam = { name, date, notes ->
                    viewModel.addExam(name, date, notes)
                },
                onDeleteExam = { item -> viewModel.deleteExam(item) },
                onBack = { navController.popBackStack() }
            )
        }

        // CHURCH TOOLS
        composable("tool_bible_planner") {
            BibleReaderScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_hymns") {
            HymnsScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_prayer_list") {
            PrayerRequestsScreen(
                prayers = prayerItems,
                isSwahili = isSwahili,
                onAddPrayer = { title, description, date ->
                    viewModel.addPrayerItem(title, description, date)
                },
                onToggleAnswered = { item -> viewModel.togglePrayerAnswered(item) },
                onDeletePrayer = { item -> viewModel.deletePrayerItem(item) },
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_church_planner") {
            ChurchAnnouncementsScreen(
                announcements = churchEvents,
                isSwahili = isSwahili,
                onAddAnnouncement = { title, date, time, location, notes ->
                    viewModel.addChurchEvent(title, date, time, location, notes)
                },
                onDeleteAnnouncement = { item -> viewModel.deleteChurchEvent(item) },
                onBack = { navController.popBackStack() }
            )
        }

        // HOME TOOLS
        composable("tool_shopping_list") {
            ShoppingListScreen(
                items = shoppingItems,
                isSwahili = isSwahili,
                onAddItem = { title, quantity, category ->
                    viewModel.addShoppingItem(title, quantity, category)
                },
                onTogglePurchased = { item -> viewModel.toggleShoppingItem(item) },
                onDeleteItem = { item -> viewModel.deleteShoppingItem(item) },
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_travel_checklist") {
            TravelChecklistScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_luku") {
            LukuCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        // TRANSPORT TOOLS
        composable("tool_fuel") {
            FuelCostCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_trip_split") {
            TripCostSplitterScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        // HEALTH TOOLS
        composable("tool_emergency") {
            EmergencyContactsScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_first_aid") {
            FirstAidScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_medicine") {
            MedicineReminderScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        // AGRICULTURE TOOLS
        composable("tool_farm_tasks") {
            FarmTaskPlannerScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_farm_profit") {
            FarmProfitCalculatorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_planting_planner") {
            PlantingPlannerScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        // TECH TOOLS
        composable("tool_qr") {
            QrGeneratorScannerScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_unit_converter") {
            UnitConverterScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_password") {
            PasswordGeneratorScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_text_counter") {
            TextCounterScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        composable("tool_tech_utilities") {
            TechnologyUtilitiesScreen(
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() }
            )
        }

        // 6 NEW INTEGRATED TOOLS
        composable("tool_risitisafe") {
            RisitiSafeScreen(
                viewModel = viewModel,
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() },
                onOpenPro = { navController.navigate("pro") }
            )
        }

        composable("tool_umeme_calc") {
            UmemeCalculatorScreen(
                viewModel = viewModel,
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() },
                onOpenPro = { navController.navigate("pro") }
            )
        }

        composable("tool_msaidizi_fomu") {
            MsaidiziWaFomuScreen(
                viewModel = viewModel,
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() },
                onOpenPro = { navController.navigate("pro") }
            )
        }

        composable("tool_mkataba_rahisi") {
            MkatabaRahisiScreen(
                viewModel = viewModel,
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() },
                onOpenPro = { navController.navigate("pro") }
            )
        }

        composable("tool_kikoba_calc") {
            KikobaCalculatorScreen(
                viewModel = viewModel,
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() },
                onOpenPro = { navController.navigate("pro") }
            )
        }

        composable("tool_biashara_calc") {
            BiasharaCalculatorScreen(
                viewModel = viewModel,
                isSwahili = isSwahili,
                onBack = { navController.popBackStack() },
                onOpenPro = { navController.navigate("pro") }
            )
        }
    }
}

@Composable
fun MainContainerScreen(
    viewModel: MsaadaViewModel,
    navController: NavHostController,
    isSwahili: Boolean,
    favoriteToolIds: Set<String>,
    recentTools: List<MsaadaTool>,
    language: String,
    themeMode: String,
    notificationsEnabled: Boolean,
    isProUser: Boolean,
    onToolClick: (MsaadaTool) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                // Home
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = MsaadaIcons.NavHome,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text(text = StringsManager.getString("nav_home", isSwahili)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MsaadaNavy,
                        selectedTextColor = MsaadaNavy,
                        indicatorColor = MsaadaTeal.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                // Categories
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = MsaadaIcons.NavCategories,
                            contentDescription = "Categories"
                        )
                    },
                    label = { Text(text = StringsManager.getString("nav_categories", isSwahili)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MsaadaNavy,
                        selectedTextColor = MsaadaNavy,
                        indicatorColor = MsaadaTeal.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_categories")
                )

                // Favorites
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) MsaadaIcons.NavFavoritesActive else MsaadaIcons.NavFavorites,
                            contentDescription = "Favorites"
                        )
                    },
                    label = { Text(text = StringsManager.getString("nav_favorites", isSwahili)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MsaadaNavy,
                        selectedTextColor = MsaadaNavy,
                        indicatorColor = MsaadaTeal.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_favorites")
                )

                // Settings
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = MsaadaIcons.NavSettings,
                            contentDescription = "Settings"
                        )
                    },
                    label = { Text(text = StringsManager.getString("nav_settings", isSwahili)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MsaadaNavy,
                        selectedTextColor = MsaadaNavy,
                        indicatorColor = MsaadaTeal.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_settings")
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = selectedTab,
            label = "tab_crossfade",
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                0 -> HomeScreen(
                    isSwahili = isSwahili,
                    recentTools = recentTools,
                    favoriteToolIds = favoriteToolIds,
                    onToolClick = onToolClick,
                    onCategoryClick = { cat ->
                        navController.navigate("category_detail/${cat.id}")
                    },
                    onSearchClick = { navController.navigate("search") },
                    onFavoriteToggle = { toolId -> viewModel.toggleFavorite(toolId) },
                    onViewAllCategories = { selectedTab = 1 },
                    onProClick = { navController.navigate("pro") }
                )

                1 -> CategoriesScreen(
                    isSwahili = isSwahili,
                    onCategoryClick = { cat ->
                        navController.navigate("category_detail/${cat.id}")
                    }
                )

                2 -> FavoritesScreen(
                    isSwahili = isSwahili,
                    favoriteToolIds = favoriteToolIds,
                    onToolClick = onToolClick,
                    onFavoriteToggle = { toolId -> viewModel.toggleFavorite(toolId) },
                    onExploreClick = { selectedTab = 1 }
                )

                3 -> SettingsScreen(
                    isSwahili = isSwahili,
                    language = language,
                    themeMode = themeMode,
                    notificationsEnabled = notificationsEnabled,
                    isProUser = isProUser,
                    onLanguageChange = { lang -> viewModel.setLanguage(lang) },
                    onThemeModeChange = { mode -> viewModel.setThemeMode(mode) },
                    onNotificationsToggle = { en -> viewModel.setNotificationsEnabled(en) },
                    onClearAllData = { viewModel.clearAllData() },
                    onOpenPro = { navController.navigate("pro") }
                )
            }
        }
    }
}
