package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.R

data class CuratedSetup(
    val id: String,
    val titleGe: String,
    val titleEn: String,
    val aestheticVibeGe: String,
    val aestheticVibeEn: String,
    val theme: ThemeItem,
    val config: LockScreenConfig,
    val colorPaletteList: List<Color>,
    val colorPaletteNamesGe: List<String>,
    val colorPaletteNamesEn: List<String>,
    val typographyReasoningGe: String,
    val typographyReasoningEn: String,
    val widgetIntegrationReasoningGe: String,
    val widgetIntegrationReasoningEn: String,
    val aodRecommendationGe: String,
    val aodRecommendationEn: String
)

object CuratedSetupsData {
    val setups: List<CuratedSetup> = listOf(
        CuratedSetup(
            id = "titanium_executive",
            titleGe = "Titanium Executive Minimal",
            titleEn = "Titanium Executive Minimal",
            aestheticVibeGe = "არქიტექტურული ფუფუნება, მქრქალი ტიტანი და თბილი ქვიშაქვის ტექსტურა",
            aestheticVibeEn = "Architectural luxury, matte titanium body and warm travertine stone",
            theme = ThemeRepository.themes.first { it.id == "titanium_minimal" },
            config = LockScreenConfig(
                clockStyle = ClockStyle.SERIF_BOLD,
                clockColor = Color(0xFFFBF9F5),
                clockSizeMultiplier = 1.05f,
                activeWidgets = listOf(WidgetType.WEATHER, WidgetType.BATTERY_RING),
                showNotificationPill = true,
                showShortcuts = true,
                homeScreenBlur = 8f
            ),
            colorPaletteList = listOf(
                Color(0xFFE2DDD9),
                Color(0xFFD6C7B2),
                Color(0xFF8B8884),
                Color(0xFF1E1F24)
            ),
            colorPaletteNamesGe = listOf("ტიტანის ნაცრისფერი", "თბილი ქვიშა", "მქრქალი ფოლადი", "ობსიდიანი"),
            colorPaletteNamesEn = listOf("Titanium Gray", "Warm Travertine", "Matte Steel", "Deep Obsidian"),
            typographyReasoningGe = "Bold Serif (Luxury) შრიფტი ქმნის პრემიუმ მოდის ჟურნალის (Editorial) ესთეტიკას, რაც იდეალურად ავსებს S24 Ultra-ს ბრტყელ ტიტანის ჩარჩოს.",
            typographyReasoningEn = "Bold Serif creates a high-fashion editorial magazine cover aesthetic, beautifully complementing the S24 Ultra's sharp titanium bezels.",
            widgetIntegrationReasoningGe = "საათის ქვეშ განთავსებულია ამინდი და ელემენტის ორმაგი რგოლი (ტელეფონი + Buds), რაც ინარჩუნებს მინიმალისტურ ჰორიზონტალურ ბალანსს და არ ფარავს ფონის არქიტექტურულ ტალღებს.",
            widgetIntegrationReasoningEn = "Weather and dual battery rings sit neatly under the clock, maintaining balanced horizontal symmetry without obscuring the wallpaper's sculptural curves.",
            aodRecommendationGe = "S24 Ultra-ს 1Hz Always On Display რეჟიმში ჩართეთ 'Show Lock screen wallpaper' 20% დაბნელებით და თეთრი საათით.",
            aodRecommendationEn = "Enable S24 Ultra's full-screen 1Hz AOD with 20% wallpaper dimming and crisp white clock typography."
        ),
        CuratedSetup(
            id = "tokyo_cyberpunk",
            titleGe = "Tokyo Midnight Cyberpunk",
            titleEn = "Tokyo Midnight Cyberpunk",
            aestheticVibeGe = "წვიმიანი შინჯუკუს ნეონი, ასფალტის ანარეკლები და მაღალი კონტრასტი",
            aestheticVibeEn = "Rainy Shinjuku neon reflections, slick wet asphalt and high contrast",
            theme = ThemeRepository.themes.first { it.id == "cyberpunk_neon" },
            config = LockScreenConfig(
                clockStyle = ClockStyle.CLEAN_SANS,
                clockColor = Color(0xFFF3E8FF),
                clockSizeMultiplier = 1.0f,
                activeWidgets = listOf(WidgetType.MUSIC_PLAYER, WidgetType.WEATHER, WidgetType.STEP_COUNTER),
                showNotificationPill = true,
                showShortcuts = true,
                homeScreenBlur = 12f
            ),
            colorPaletteList = listOf(
                Color(0xFF5CE1E6),
                Color(0xFFF43F5E),
                Color(0xFFA855F7),
                Color(0xFF0F071D)
            ),
            colorPaletteNamesGe = listOf("კიბერ ციანი", "ნეონ მაჯენტა", "ელექტრიკ იისფერი", "შუაღამის ასფალტი"),
            colorPaletteNamesEn = listOf("Cyber Cyan", "Neon Magenta", "Electric Violet", "Midnight Asphalt"),
            typographyReasoningGe = "Clean Sans-Serif შრიფტი იძლევა ფუტურისტულ, სუფთა და ტექნოლოგიურ ხასიათს, რომელიც გამოირჩევა ნეონის ანარეკლების ფონზე.",
            typographyReasoningEn = "Clean Modern Sans-Serif gives a sleek futuristic cyberpunk vibe with maximum legibility against glowing neon signs.",
            widgetIntegrationReasoningGe = "მუსიკის მინი-პლეერი ('Midnight City') + ამინდი + ნაბიჯები ქმნის აქტიურ ღამის ქალაქის რიტმს. ციანისა და იისფერი აქცენტები პირდაპირ ეხმიანება ფონის განათებას.",
            widgetIntegrationReasoningEn = "Music player widget ('Midnight City') + Weather + Steps matches the pulse of a late-night metropolis, with cyan highlights echoing wet road reflections.",
            aodRecommendationGe = "AOD რეჟიმში ჩართეთ 'Erase Background', რათა გამოჩნდეს მხოლოდ ნეონის შუქურები და დრო.",
            aodRecommendationEn = "In AOD settings, use 'Erase Background' cutout to highlight the neon signs while the rest turns true-black AMOLED."
        ),
        CuratedSetup(
            id = "kyoto_lofi",
            titleGe = "Kyoto Lo-Fi Coffee Sanctuary",
            titleEn = "Kyoto Lo-Fi Coffee Sanctuary",
            aestheticVibeGe = "მყუდრო იაპონური კაფე, წვიმის წვეთები, თბილი შუქი და სიმშვიდე",
            aestheticVibeEn = "Cozy Japanese cafe window overlooking a rainy courtyard garden with warm ambient light",
            theme = ThemeRepository.themes.first { it.id == "lofi_cafe" },
            config = LockScreenConfig(
                clockStyle = ClockStyle.HANDWRITTEN,
                clockColor = Color(0xFFFFF7ED),
                clockSizeMultiplier = 1.0f,
                activeWidgets = listOf(WidgetType.QUOTE_TEXT, WidgetType.WEATHER, WidgetType.CALENDAR_EVENT),
                showNotificationPill = true,
                showShortcuts = true,
                homeScreenBlur = 6f
            ),
            colorPaletteList = listOf(
                Color(0xFFFFB37C),
                Color(0xFFF59E0B),
                Color(0xFF4A342B),
                Color(0xFF1B1412)
            ),
            colorPaletteNamesGe = listOf("თბილი კარამელი", "ქარვისფერი შუქი", "ესპრესოს ყავისფერი", "მუქი ხე"),
            colorPaletteNamesEn = listOf("Warm Caramel", "Amber Glow", "Espresso Brown", "Dark Timber"),
            typographyReasoningGe = "Handwritten Cursive / Lo-Fi შრიფტი ანიჭებს პერსონალურ, თბილ და არტისტულ იერს, როგორც ხელნაწერი დღიური მყუდრო კაფეში.",
            typographyReasoningEn = "Handwritten aesthetic script brings a personal, cozy, artistic journal feel, evoking a quiet rainy afternoon in Kyoto.",
            widgetIntegrationReasoningGe = "ესთეტიკური ციტატა ('ესთეტიკა / Mindful') და კალენდრის ივენთი საათის ქვეშ აყალიბებს მშვიდ, ორგანიზებულ დღის განრიგს.",
            widgetIntegrationReasoningEn = "Aesthetic mindful quote pill + Calendar widget under the clock creates a calm, organized daily sanctuary.",
            aodRecommendationGe = "One UI Color Palette-ში აირჩიეთ ყავისფერი/ქარვისფერი პალიტრა და მოარგეთ აპლიკაციების იკონებს.",
            aodRecommendationEn = "In One UI Color Palette, choose the warm espresso/amber swatch and apply it to all home screen icons."
        ),
        CuratedSetup(
            id = "dreamy_coastal",
            titleGe = "Dreamy Lilac Coastal Sunset",
            titleEn = "Dreamy Lilac Coastal Sunset",
            aestheticVibeGe = "პასტელური ლავანდა, ვარდისფერი ბინდი და მშვიდი ოკეანის ტალღები",
            aestheticVibeEn = "Pastel lilac, blush peach sunset and calm serene ocean waves",
            theme = ThemeRepository.themes.first { it.id == "coastal_sunset" },
            config = LockScreenConfig(
                clockStyle = ClockStyle.THIN_ROMAN,
                clockColor = Color(0xFFFFFDF9),
                clockSizeMultiplier = 1.05f,
                activeWidgets = listOf(WidgetType.WEATHER, WidgetType.BATTERY_RING),
                showNotificationPill = false,
                showShortcuts = true,
                homeScreenBlur = 10f
            ),
            colorPaletteList = listOf(
                Color(0xFF8B7EAC),
                Color(0xFFDE9B99),
                Color(0xFFFFC09F),
                Color(0xFFFFFDF9)
            ),
            colorPaletteNamesGe = listOf("ლავანდისფერი", "პასტელური ატამი", "ოქროსფერი ჰორიზონტი", "მარგალიტისფერი"),
            colorPaletteNamesEn = listOf("Lilac Lavender", "Pastel Peach", "Golden Twilight", "Pure Pearl"),
            typographyReasoningGe = "Thin Roman შრიფტი ჰაეროვანი და დახვეწილია, რათა არ გადატვირთოს ნაზი პასტელური გრადიენტი.",
            typographyReasoningEn = "Thin Roman typography feels airy, weightless, and refined, letting the soft gradient sky breathe.",
            widgetIntegrationReasoningGe = "მინიმალისტური გამჭვირვალე ამინდის ვიჯეტი ნაზად ერწყმის ოკეანის ჰორიზონტს ყოველგვარი უხეში ჩარჩოების გარეშე.",
            widgetIntegrationReasoningEn = "Minimalist translucent weather widget seamlessly blends into the ocean horizon without harsh borders.",
            aodRecommendationGe = "S24 Ultra-ს AOD-ზე გამოიყენეთ სრული პასტელური ფონი 15% სიკაშკაშით.",
            aodRecommendationEn = "On S24 Ultra's AOD, use the full pastel gradient with 15% brightness for a serene bedside clock."
        )
    )
}
