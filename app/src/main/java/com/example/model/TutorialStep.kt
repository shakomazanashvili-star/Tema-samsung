package com.example.model

data class TutorialStep(
    val id: Int,
    val titleGe: String,
    val titleEn: String,
    val shortSummaryGe: String,
    val shortSummaryEn: String,
    val instructionsGe: List<String>,
    val instructionsEn: List<String>,
    val tipGe: String,
    val tipEn: String,
    val tag: String
)

object TutorialData {
    val steps = listOf(
        TutorialStep(
            id = 1,
            titleGe = "1. ლოქსქრინის რედაქტირება One UI 6.1-ზე",
            titleEn = "1. Edit Lock Screen on One UI 6.1",
            shortSummaryGe = "როგორ გავააქტიუროთ ლოქსქრინის პარამეტრები Samsung S24 Ultra-ზე",
            shortSummaryEn = "How to enter Lock Screen customizer on Samsung S24 Ultra",
            instructionsGe = listOf(
                "გადადით დაბლოკილ ეკრანზე (Lock Screen).",
                "დააჭირეთ ეკრანის ცარიელ ადგილს და გეჭიროთ 1–2 წამი (Long Press).",
                "გაიარეთ ავტორიზაცია თითის ანაბეჭდით (Fingerprint) ან პინ-კოდით.",
                "გაიხსნება ლოქსქრინის რედაქტირების ინტერაქციული რეჟიმი."
            ),
            instructionsEn = listOf(
                "Turn on your Samsung Galaxy S24 Ultra to the Lock Screen.",
                "Touch and hold (long press) any empty space on the screen.",
                "Unlock with your fingerprint or PIN.",
                "The live One UI Lock Screen editor will open."
            ),
            tipGe = "რჩევა: შეგიძლიათ პირდაპირ ამ აპლიკაციიდან დააჭიროთ 'დაყენებას' და ფონი მომენტალურად დაყენდება!",
            tipEn = "Tip: You can also tap 'Apply' in this app to set the wallpaper instantly!",
            tag = "One UI 6.1"
        ),
        TutorialStep(
            id = 2,
            titleGe = "2. საათის შრიფტისა და ფერის მორგება",
            titleEn = "2. Customize Clock Font & Color",
            shortSummaryGe = "აირჩიეთ ესთეტიკური შრიფტი, რომელიც ემთხვევა ფონის ფერებს",
            shortSummaryEn = "Select aesthetic typography matching your wallpaper palette",
            instructionsGe = listOf(
                "რედაქტირების რეჟიმში დააჭირეთ საათის ბლოკს.",
                "ეკრანის ქვემოთ გამოჩნდება შრიფტების სია (Bold Serif, Modern Sans, Handwriting და ა.შ.).",
                "შეცვალეთ საათის ზომა კუთხის მარკერების გაწევით.",
                "ფერთა პალიტრიდან აირჩიეთ ფონთან შეხამებული ფერი ან გამოიყენეთ პიპეტკა (Color Picker)."
            ),
            instructionsEn = listOf(
                "Tap directly on the Clock in the lock screen editor.",
                "Browse the font styles along the bottom bar (Serif, Sans, Handwritten, etc.).",
                "Drag the corner handles to resize the clock to your liking.",
                "Pick an adaptive color from the palette that harmonizes with your wallpaper."
            ),
            tipGe = "რჩევა: S24 Ultra-ზე ხელმისაწვდომია ადაპტური ფერები, რომლებიც ავტომატურად იღებენ ტონებს ფონიდან.",
            tipEn = "Tip: S24 Ultra includes adaptive colors that automatically match your wallpaper.",
            tag = "Clock Styling"
        ),
        TutorialStep(
            id = 3,
            titleGe = "3. ვიჯეტების დამატება ლოქსქრინზე",
            titleEn = "3. Add Lock Screen Widgets",
            shortSummaryGe = "ელემენტის (Battery, Weather, Calendar) დამატება საათის ქვეშ",
            shortSummaryEn = "Add Battery, Weather, and Fitness rings under the clock",
            instructionsGe = listOf(
                "საათის ქვეშ დააჭირეთ ღილაკს '+ Widgets' (ვიჯეტები).",
                "აირჩიეთ სასურველი ვიჯეტები: ელემენტის სტატუსი (Battery rings), ამინდი (Weather), ნაბიჯები (Samsung Health) ან კალენდარი.",
                "მაქსიმუმ შეგიძლიათ დაამატოთ 4 პატარა ან 2 საშუალო ვიჯეტი.",
                "ზედა მარჯვენა კუთხეში დააჭირეთ 'Done' (შესრულება) შესანახად."
            ),
            instructionsEn = listOf(
                "Tap the '+ Widgets' area directly below the clock.",
                "Select matching aesthetic widgets: Battery status, Weather with high/low, Steps, or Calendar events.",
                "You can add up to 4 small widgets or 2 medium widgets.",
                "Tap 'Done' in the top right corner to save changes."
            ),
            tipGe = "რჩევა: ბატარეის ვიჯეტი ერთდროულად აჩვენებს S24 Ultra-ს, Galaxy Watch-ის და Galaxy Buds-ის მუხტს!",
            tipEn = "Tip: The battery widget displays your phone, Galaxy Watch, and Galaxy Buds battery levels together!",
            tag = "Widgets"
        ),
        TutorialStep(
            id = 4,
            titleGe = "4. Always On Display (AOD) ფონის გააქტიურება",
            titleEn = "4. S24 Ultra Full-Screen AOD",
            shortSummaryGe = "Galaxy S24 Ultra-ს ექსკლუზიური მუქი AOD ფონის რეჟიმი",
            shortSummaryEn = "Exclusive Galaxy S24 Ultra full-screen dim lockscreen AOD",
            instructionsGe = listOf(
                "გახსენით ტელეფონის პარამეტრები (Settings) > Lock Screen and AOD.",
                "ჩართეთ 'Always On Display'.",
                "მონიშნეთ 'Show Lock screen wallpaper' (ფონის ჩვენება AOD-ზე).",
                "ჩართეთ 'Erase background' თუ გსურთ მხოლოდ მთავარი ობიექტის გამოჩენა AOD რეჟიმში."
            ),
            instructionsEn = listOf(
                "Open Settings > Lock Screen and AOD.",
                "Turn on 'Always On Display'.",
                "Toggle on 'Show Lock screen wallpaper'.",
                "Optionally toggle 'Erase background' to keep only subjects highlighted."
            ),
            tipGe = "S24 Ultra-ს 1Hz ეკრანი ენერგოეფექტურად ინარჩუნებს ფონს AOD-ზე მინიმალური ბატარეის ხარჯვით!",
            tipEn = "S24 Ultra's 1Hz refresh rate keeps the wallpaper visible with almost zero battery drain!",
            tag = "AOD Feature"
        ),
        TutorialStep(
            id = 5,
            titleGe = "5. აპლიკაციების იკონების ფერთა პალიტრა",
            titleEn = "5. One UI Color Palette for App Icons",
            shortSummaryGe = "როგორ მივიღოთ მთლიანად ერთგვაროვანი ესთეტიკური იერსახე",
            shortSummaryEn = "Coordinate app icons with your wallpaper theme",
            instructionsGe = listOf(
                "გახსენით პარამეტრები (Settings) > Wallpaper and style.",
                "დააჭირეთ 'Color palette' (ფერთა პალიტრა).",
                "ჩართეთ 'Color palette' გადამრთველი.",
                "ჩართეთ 'Apply palette to app icons' (პალიტრის მორგება იკონებზე).",
                "დააჭირეთ 'Apply' — თქვენი მთავარი ეკრანის ყველა იკონი მიიღებს ფონის ტონალობას!"
            ),
            instructionsEn = listOf(
                "Open Settings > Wallpaper and style.",
                "Tap 'Color palette'.",
                "Turn on the Color palette toggle.",
                "Enable 'Apply palette to app icons'.",
                "Tap 'Apply' — all supported app icons will now match your aesthetic theme!"
            ),
            tipGe = "ეს მოგცემთ ზუსტად იმ მინიმალისტურ, დახვეწილ დიზაინს, რაც iThemeDesign-ის ვიდეოებშია!",
            tipEn = "This gives you the exact minimalist, unified look seen in iThemeDesign showcases!",
            tag = "Aesthetic Icons"
        )
    )
}
