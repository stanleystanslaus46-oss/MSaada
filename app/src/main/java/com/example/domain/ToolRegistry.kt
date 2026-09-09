package com.example.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.MsaadaIcons
import com.example.ui.theme.MsaadaNavy
import com.example.ui.theme.MsaadaTeal

data class MsaadaCategory(
    val id: String,
    val nameSw: String,
    val nameEn: String,
    val descSw: String,
    val descEn: String,
    val icon: ImageVector,
    val accentColor: Color
)

data class MsaadaTool(
    val id: String,
    val categoryId: String,
    val nameSw: String,
    val nameEn: String,
    val descSw: String,
    val descEn: String,
    val icon: ImageVector,
    val route: String,
    val isPro: Boolean = false,
    val isQuickTool: Boolean = false,
    val iconEmoji: String = ""
)

object ToolRegistry {
    // Brand-aligned colors (Navy #001848, Teal #008C95)
    val categories = listOf(
        MsaadaCategory(
            id = "fedha",
            nameSw = "Fedha",
            nameEn = "Finance",
            descSw = "Hesabu na panga fedha zako",
            descEn = "Calculate and organize your finances",
            icon = MsaadaIcons.Finance,
            accentColor = MsaadaTeal
        ),
        MsaadaCategory(
            id = "biashara",
            nameSw = "Biashara",
            nameEn = "Business",
            descSw = "Zana za wafanyabiashara na hati",
            descEn = "Business tools and documents",
            icon = MsaadaIcons.Business,
            accentColor = MsaadaNavy
        ),
        MsaadaCategory(
            id = "picha_nyaraka",
            nameSw = "Picha & Nyaraka",
            nameEn = "Photos & Docs",
            descSw = "Unda picha za passport na ID kwa urahisi",
            descEn = "Generate passport and ID photos easily",
            icon = MsaadaIcons.Documents,
            accentColor = MsaadaTeal
        ),
        MsaadaCategory(
            id = "elimu",
            nameSw = "Elimu",
            nameEn = "Education",
            descSw = "Panga masomo na mitihani",
            descEn = "Plan studies and exams",
            icon = MsaadaIcons.Education,
            accentColor = MsaadaNavy
        ),
        MsaadaCategory(
            id = "kanisa",
            nameSw = "Kanisa",
            nameEn = "Church",
            descSw = "Zana za maisha ya kiroho na kanisa",
            descEn = "Tools for spiritual life and church",
            icon = MsaadaIcons.Church,
            accentColor = MsaadaTeal
        ),
        MsaadaCategory(
            id = "nyumbani",
            nameSw = "Nyumbani",
            nameEn = "Home",
            descSw = "Mipango ya familia na nyumba",
            descEn = "Family and home planners",
            icon = MsaadaIcons.Home,
            accentColor = MsaadaNavy
        ),
        MsaadaCategory(
            id = "usafiri",
            nameSw = "Usafiri",
            nameEn = "Transport",
            descSw = "Zana za usafiri na safari",
            descEn = "Tools for travel and transport",
            icon = MsaadaIcons.Transport,
            accentColor = MsaadaTeal
        ),
        MsaadaCategory(
            id = "afya",
            nameSw = "Afya",
            nameEn = "Health",
            descSw = "Taarifa za afya na usalama",
            descEn = "Health and first aid information",
            icon = MsaadaIcons.Health,
            accentColor = MsaadaNavy
        ),
        MsaadaCategory(
            id = "kilimo",
            nameSw = "Kilimo",
            nameEn = "Agriculture",
            descSw = "Zana za wakulima na mazao",
            descEn = "Tools for farmers and crops",
            icon = MsaadaIcons.Agriculture,
            accentColor = MsaadaTeal
        ),
        MsaadaCategory(
            id = "teknolojia",
            nameSw = "Teknolojia",
            nameEn = "Technology",
            descSw = "Zana za kidijitali na mtandao",
            descEn = "Digital and network tools",
            icon = MsaadaIcons.Technology,
            accentColor = MsaadaNavy
        )
    )

    val tools = listOf(
        // Quick Tools
        MsaadaTool(
            id = "bajeti",
            categoryId = "fedha",
            nameSw = "Bajeti",
            nameEn = "Budget Calculator",
            descSw = "Panga mapato na matumizi",
            descEn = "Plan income and expenses",
            icon = MsaadaIcons.Budget,
            route = "tool_budget",
            isQuickTool = true
        ),
        MsaadaTool(
            id = "calculator",
            categoryId = "fedha",
            nameSw = "Calculator",
            nameEn = "Calculator",
            descSw = "Hesabu za haraka na kumbukumbu",
            descEn = "Quick math calculations & history",
            icon = MsaadaIcons.Calculator,
            route = "tool_calculator",
            isQuickTool = true
        ),
        MsaadaTool(
            id = "faida",
            categoryId = "biashara",
            nameSw = "Faida",
            nameEn = "Profit Calculator",
            descSw = "Hesabu faida na asilimia ya faida",
            descEn = "Calculate profit & profit margin",
            icon = MsaadaIcons.Profit,
            route = "tool_profit",
            isQuickTool = true
        ),
        MsaadaTool(
            id = "qr_code",
            categoryId = "teknolojia",
            nameSw = "QR Code",
            nameEn = "QR Code Generator",
            descSw = "Tengeneza QR codes bure na haraka",
            descEn = "Generate free offline QR codes",
            icon = MsaadaIcons.QR,
            route = "tool_qr",
            isQuickTool = true
        ),

        // PASSPORT PHOTO & NYARAKA
        MsaadaTool(
            id = "passport_photo",
            categoryId = "picha_nyaraka",
            nameSw = "Passport Photo Generator",
            nameEn = "Passport Photo Generator",
            descSw = "Unda picha za passport na ID kwa urahisi",
            descEn = "Create passport & ID photos easily",
            icon = MsaadaIcons.PassportPhoto,
            route = "tool_passport_photo"
        ),
        MsaadaTool(
            id = "msaidizi_fomu",
            categoryId = "picha_nyaraka",
            nameSw = "Msaidizi wa Fomu",
            nameEn = "Form Filling Assistant",
            descSw = "Ujazaji wa fomu za maombi, taarifa binafsi na barua",
            descEn = "Personal, business & application form assistant",
            icon = MsaadaIcons.FormAssistant,
            route = "tool_msaidizi_fomu"
        ),
        MsaadaTool(
            id = "mkataba_rahisi",
            categoryId = "picha_nyaraka",
            nameSw = "Mkataba Rahisi",
            nameEn = "Simple Agreement & Contracts",
            descSw = "Unda mikataba ya kodi, mauziano, mikopo na huduma",
            descEn = "Create rent, sales, loan & service agreements",
            icon = MsaadaIcons.SimpleContract,
            route = "tool_mkataba_rahisi"
        ),

        // FEDHA (Finance)
        MsaadaTool(
            id = "asilimia",
            categoryId = "fedha",
            nameSw = "Percentage Calculator",
            nameEn = "Percentage Calculator",
            descSw = "Hesabu asilimia, ongezeko na upungufu",
            descEn = "Calculate percentage, increase & decrease",
            icon = MsaadaIcons.Percentage,
            route = "tool_percentage"
        ),
        MsaadaTool(
            id = "akiba",
            categoryId = "fedha",
            nameSw = "Savings Calculator",
            nameEn = "Savings Calculator",
            descSw = "Kadiria muda wa kufikia lengo lako la akiba",
            descEn = "Estimate time to reach your savings target",
            icon = MsaadaIcons.Savings,
            route = "tool_savings"
        ),
        MsaadaTool(
            id = "mikopo",
            categoryId = "fedha",
            nameSw = "Loan Calculator",
            nameEn = "Loan Calculator",
            descSw = "Kadiria marejesho ya mkopo na riba",
            descEn = "Estimate loan repayment & interest",
            icon = MsaadaIcons.Loan,
            route = "tool_loan"
        ),
        MsaadaTool(
            id = "kikoba_calc",
            categoryId = "fedha",
            nameSw = "Kikoba Calculator",
            nameEn = "Kikoba & Savings Calculator",
            descSw = "Panga mizunguko ya michango na wanachama wa kikoba",
            descEn = "Calculate group savings rounds & member payouts",
            icon = MsaadaIcons.Kikoba,
            route = "tool_kikoba_calc"
        ),

        // BIASHARA (Business)
        MsaadaTool(
            id = "risitisafe",
            categoryId = "biashara",
            nameSw = "RisitiSafe",
            nameEn = "RisitiSafe Receipt Generator",
            descSw = "Tengeneza na hifadhi risiti za kidijitali za biashara",
            descEn = "Generate & organize digital sales receipts",
            icon = MsaadaIcons.ReceiptSafe,
            route = "tool_risitisafe",
            isQuickTool = true
        ),
        MsaadaTool(
            id = "biashara_calc",
            categoryId = "biashara",
            nameSw = "Biashara Calculator",
            nameEn = "Business Profit & Break-Even",
            descSw = "Hesabu faida, uwekezaji na kurudisha gharama",
            descEn = "Calculate profit, markup, margin & break-even",
            icon = MsaadaIcons.BusinessCalculator,
            route = "tool_biashara_calc"
        ),
        MsaadaTool(
            id = "discount",
            categoryId = "biashara",
            nameSw = "Discount Calculator",
            nameEn = "Discount Calculator",
            descSw = "Hesabu punguzo la bei kwa mteja",
            descEn = "Calculate customer price discount",
            icon = MsaadaIcons.Discount,
            route = "tool_discount"
        ),
        MsaadaTool(
            id = "markup",
            categoryId = "biashara",
            nameSw = "Markup Calculator",
            nameEn = "Markup Calculator",
            descSw = "Weka asilimia ya ziada juu ya gharama",
            descEn = "Add markup percentage on unit cost",
            icon = MsaadaIcons.Markup,
            route = "tool_markup"
        ),
        MsaadaTool(
            id = "invoice_gen",
            categoryId = "biashara",
            nameSw = "Invoice Generator",
            nameEn = "Invoice Generator",
            descSw = "Tengeneza ankara za mauzo za kitaalamu",
            descEn = "Generate professional sales invoices",
            icon = MsaadaIcons.Invoice,
            route = "doc_invoice"
        ),
        MsaadaTool(
            id = "quotation_gen",
            categoryId = "biashara",
            nameSw = "Quotation Generator",
            nameEn = "Quotation Generator",
            descSw = "Tengeneza makisio ya bei (Proforma)",
            descEn = "Create price quotation & proforma",
            icon = MsaadaIcons.Quotation,
            route = "doc_quotation"
        ),
        MsaadaTool(
            id = "receipt_gen",
            categoryId = "biashara",
            nameSw = "Receipt Generator",
            nameEn = "Receipt Generator",
            descSw = "Tengeneza risiti ya malipo na miamala",
            descEn = "Create payment receipts & transactions",
            icon = MsaadaIcons.Receipt,
            route = "doc_receipt"
        ),
        MsaadaTool(
            id = "expense_report",
            categoryId = "biashara",
            nameSw = "Expense Report",
            nameEn = "Expense Report",
            descSw = "Fuatilia na rekodi matumizi ya biashara",
            descEn = "Track & record business expenses",
            icon = MsaadaIcons.ExpenseReport,
            route = "doc_expense"
        ),
        MsaadaTool(
            id = "business_report",
            categoryId = "biashara",
            nameSw = "Business Report",
            nameEn = "Business Report",
            descSw = "Muhtasari wa mapato, matumizi na faida",
            descEn = "Summary of revenue, expenses & profit",
            icon = MsaadaIcons.BusinessReport,
            route = "doc_report"
        ),
        MsaadaTool(
            id = "templates_hub",
            categoryId = "biashara",
            nameSw = "Violezo vya Kitaalamu",
            nameEn = "Professional Templates",
            descSw = "Gundua violezo vya biashara na maisha",
            descEn = "Explore business and lifestyle templates",
            icon = MsaadaIcons.TemplatesHub,
            route = "templates_hub"
        ),

        // ELIMU (Education)
        MsaadaTool(
            id = "grade_calc",
            categoryId = "elimu",
            nameSw = "Grade Calculator",
            nameEn = "Grade Calculator",
            descSw = "Hesabu wastani na madaraja ya masomo (A, B, C...)",
            descEn = "Calculate GPA, average & letter grades",
            icon = MsaadaIcons.Grade,
            route = "tool_grade"
        ),
        MsaadaTool(
            id = "study_timer",
            categoryId = "elimu",
            nameSw = "Study Timer",
            nameEn = "Study Timer",
            descSw = "Saa ya Pomodoro: vipindi vya kujisomea na mapumziko",
            descEn = "Pomodoro timer: study sessions & breaks",
            icon = MsaadaIcons.Timer,
            route = "tool_study_timer"
        ),
        MsaadaTool(
            id = "exam_countdown",
            categoryId = "elimu",
            nameSw = "Exam Countdown",
            nameEn = "Exam Countdown",
            descSw = "Kuhesabu siku zilizobaki kufikia mtihani",
            descEn = "Days remaining countdown until exams",
            icon = MsaadaIcons.ExamCountdown,
            route = "tool_exam_countdown"
        ),
        MsaadaTool(
            id = "unit_converter",
            categoryId = "elimu",
            nameSw = "Unit Converter",
            nameEn = "Unit Converter",
            descSw = "Urefu, uzito, eneo na joto",
            descEn = "Length, weight, area & temperature",
            icon = MsaadaIcons.UnitConverter,
            route = "tool_unit_converter"
        ),
        MsaadaTool(
            id = "study_planner",
            categoryId = "elimu",
            nameSw = "Study Planner",
            nameEn = "Study Planner",
            descSw = "Panga ratiba ya masomo na majukumu",
            descEn = "Schedule subjects and study tasks",
            icon = MsaadaIcons.StudyPlanner,
            route = "tool_study_planner"
        ),

        // KANISA (Church)
        MsaadaTool(
            id = "zaka_sadaka",
            categoryId = "kanisa",
            nameSw = "Tithe Calculator",
            nameEn = "Tithe Calculator",
            descSw = "Hesabu fungu la kumi (10%) na sadaka kwa usahihi",
            descEn = "Calculate tithes (10%) & offerings accurately",
            icon = MsaadaIcons.Tithe,
            route = "tool_tithe"
        ),
        MsaadaTool(
            id = "prayer_list",
            categoryId = "kanisa",
            nameSw = "Orodha ya Maombi",
            nameEn = "Prayer List",
            descSw = "Rekodi maombi, majibu na shukrani",
            descEn = "Record prayers, answers & thanks",
            icon = MsaadaIcons.PrayerList,
            route = "tool_prayer_list"
        ),
        MsaadaTool(
            id = "church_planner",
            categoryId = "kanisa",
            nameSw = "Church Planner",
            nameEn = "Church Planner",
            descSw = "Ratiba ya ibada, mikutano na matukio",
            descEn = "Services schedule, meetings & events",
            icon = MsaadaIcons.ChurchPlanner,
            route = "tool_church_planner"
        ),
        MsaadaTool(
            id = "bible_reader_plan",
            categoryId = "kanisa",
            nameSw = "Bible Reading Planner",
            nameEn = "Bible Reading Planner",
            descSw = "Fuatilia usomaji wa vitabu na mistari ya Biblia",
            descEn = "Track reading of Bible books and chapters",
            icon = MsaadaIcons.BiblePlanner,
            route = "tool_bible_planner"
        ),

        // NYUMBANI (Home)
        MsaadaTool(
            id = "shopping_list",
            categoryId = "nyumbani",
            nameSw = "Orodha ya Ununuzi",
            nameEn = "Shopping List",
            descSw = "Orodha ya mahitaji ya sokoni na nyumbani",
            descEn = "Market and home grocery shopping checklist",
            icon = MsaadaIcons.ShoppingList,
            route = "tool_shopping_list"
        ),
        MsaadaTool(
            id = "home_expenses",
            categoryId = "nyumbani",
            nameSw = "Home Expense Tracker",
            nameEn = "Home Expense Tracker",
            descSw = "Kodi, chakula, maji, shule na matumizi ya nyumbani",
            descEn = "Rent, food, water, school & home expenses",
            icon = MsaadaIcons.HomeExpenses,
            route = "tool_home_expense"
        ),
        MsaadaTool(
            id = "umeme_calc",
            categoryId = "nyumbani",
            nameSw = "Umeme Calculator",
            nameEn = "Electricity Cost Calculator",
            descSw = "Kadiria gharama za umeme kupitia mita na unit",
            descEn = "Estimate electricity cost by meter & units",
            icon = MsaadaIcons.Electricity,
            route = "tool_umeme_calc"
        ),
        MsaadaTool(
            id = "luku_estimator",
            categoryId = "nyumbani",
            nameSw = "Electricity Cost (LUKU)",
            nameEn = "Electricity Cost (LUKU)",
            descSw = "Kadiria matumizi ya umeme kwa vifaa vya nyumbani",
            descEn = "Estimate appliance power consumption & cost",
            icon = MsaadaIcons.Electricity,
            route = "tool_luku"
        ),

        // USAFIRI (Transport)
        MsaadaTool(
            id = "fuel_calc",
            categoryId = "usafiri",
            nameSw = "Fuel Cost Calculator",
            nameEn = "Fuel Cost Calculator",
            descSw = "Gharama ya mafuta kulingana na umbali (km)",
            descEn = "Fuel cost estimate based on distance",
            icon = MsaadaIcons.Fuel,
            route = "tool_fuel"
        ),
        MsaadaTool(
            id = "trip_splitter",
            categoryId = "usafiri",
            nameSw = "Trip Cost Splitter",
            nameEn = "Trip Cost Splitter",
            descSw = "Gawa gharama za safari kwa abiria kwa usawa",
            descEn = "Split journey & fuel costs among passengers",
            icon = MsaadaIcons.TripSplit,
            route = "tool_trip_split"
        ),
        MsaadaTool(
            id = "travel_checklist",
            categoryId = "usafiri",
            nameSw = "Travel Checklist",
            nameEn = "Travel Checklist",
            descSw = "Orodha ya vitu vya kufungasha safarini",
            descEn = "Luggage and travel packing checklist",
            icon = MsaadaIcons.TravelChecklist,
            route = "tool_travel_checklist"
        ),

        // AFYA (Health)
        MsaadaTool(
            id = "medicine_reminder",
            categoryId = "afya",
            nameSw = "Medicine Reminder",
            nameEn = "Medicine Reminder",
            descSw = "Kumbukumbu ya dawa na ratiba ya unywaji",
            descEn = "Medication dosage & intake schedule reminder",
            icon = MsaadaIcons.Medication,
            route = "tool_medicine"
        ),
        MsaadaTool(
            id = "emergency_contacts",
            categoryId = "afya",
            nameSw = "Emergency Contacts",
            nameEn = "Emergency Contacts",
            descSw = "Namba za dharura: Polisi, Zimamoto na jamaa",
            descEn = "Emergency contacts: Police, Fire, Ambulance",
            icon = MsaadaIcons.EmergencyContacts,
            route = "tool_emergency"
        ),
        MsaadaTool(
            id = "first_aid",
            categoryId = "afya",
            nameSw = "Huduma ya Kwanza",
            nameEn = "Basic First Aid",
            descSw = "Miongozo ya kuokoa maisha kwa majeraha",
            descEn = "First aid life-saving guide for injuries",
            icon = MsaadaIcons.FirstAid,
            route = "tool_first_aid"
        ),

        // KILIMO (Agriculture)
        MsaadaTool(
            id = "farm_profit",
            categoryId = "kilimo",
            nameSw = "Farm Profit Calculator",
            nameEn = "Farm Profit Calculator",
            descSw = "Gharama za mbegu, mbolea, vibarua na faida ya mavuno",
            descEn = "Cost of seeds, fertilizer, labor & harvest profit",
            icon = MsaadaIcons.FarmProfit,
            route = "tool_farm_profit"
        ),
        MsaadaTool(
            id = "farm_tasks",
            categoryId = "kilimo",
            nameSw = "Farm Task Planner",
            nameEn = "Farm Task Planner",
            descSw = "Ratiba ya kupanda, kupalilia, dawa na kuvuna",
            descEn = "Planting, weeding, spraying & harvesting schedule",
            icon = MsaadaIcons.FarmTasks,
            route = "tool_farm_tasks"
        ),
        MsaadaTool(
            id = "planting_planner",
            categoryId = "kilimo",
            nameSw = "Planting Planner",
            nameEn = "Planting Planner",
            descSw = "Muda wa kukomaa kwa mazao mbalimbali nchini",
            descEn = "Crop maturity periods & expected harvest dates",
            icon = MsaadaIcons.PlantingPlanner,
            route = "tool_planting_planner"
        ),

        // TEKNOLOJIA (Technology)
        MsaadaTool(
            id = "password_gen",
            categoryId = "teknolojia",
            nameSw = "Password Generator",
            nameEn = "Password Generator",
            descSw = "Tengeneza maneno siri imara na salama",
            descEn = "Generate strong and secure passwords",
            icon = MsaadaIcons.PasswordGen,
            route = "tool_password"
        ),
        MsaadaTool(
            id = "text_counter",
            categoryId = "teknolojia",
            nameSw = "Text Counter",
            nameEn = "Text Counter",
            descSw = "Hesabu maneno, herufi na muda wa kusoma",
            descEn = "Count words, characters & reading time",
            icon = MsaadaIcons.TextCounter,
            route = "tool_text_counter"
        ),
        MsaadaTool(
            id = "tech_utilities",
            categoryId = "teknolojia",
            nameSw = "JSON & Base64 & Colors",
            nameEn = "Developer Utilities",
            descSw = "JSON formatter, Base64 encoder na Color converter",
            descEn = "JSON formatter, Base64 encoder & Color converter",
            icon = MsaadaIcons.TechUtilities,
            route = "tool_tech_utilities"
        )
    )

    fun getToolsForCategory(categoryId: String): List<MsaadaTool> {
        return tools.filter { it.categoryId == categoryId }
    }

    fun getToolById(toolId: String): MsaadaTool? {
        return tools.find { it.id == toolId }
    }

    fun searchTools(query: String): List<MsaadaTool> {
        if (query.isBlank()) return emptyList()
        val q = query.trim().lowercase()
        return tools.filter { tool ->
            tool.nameSw.lowercase().contains(q) ||
            tool.nameEn.lowercase().contains(q) ||
            tool.descSw.lowercase().contains(q) ||
            tool.descEn.lowercase().contains(q) ||
            tool.categoryId.lowercase().contains(q)
        }
    }
}
