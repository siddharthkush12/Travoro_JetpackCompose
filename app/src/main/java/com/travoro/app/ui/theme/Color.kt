package com.travoro.app.ui.theme

import androidx.compose.ui.graphics.Color

// --- Default Material 3 Colors ---
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// --- Core Brand Colors ---
val TealCyan = Color(0xFF18C0C1)
val TealCyanLight = Color(0xFF5CE1E6)
val TealCyanDark = Color(0xFF0E8A8B)
val SunsetOrange = Color(0xFFFF7E67)

// --- Backgrounds & Surfaces ---
val DeepSpace = Color(0xFF020617)
val MidnightBlue = Color(0xFF0F172A)
val SurfaceDark = Color(0xFF1E293B)
val SurfaceLighter = Color(0xFF334155)

// --- Typography ---
val TextWhite = Color(0xFFFFFFFF)
val TextOffWhite = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF64748B)

// --- Semantic / Feedback ---
val ErrorRed = Color(0xFFEF4444)
val SuccessGreen = Color(0xFF10B981)
val WarningYellow = Color(0xFFF59E0B)

// =========================================================================
// 🌟 NEW: THE "BEAUTIFUL UI" EXPANSION
// =========================================================================

// --- 1. Destination Tags & Badges (Neon pastels that pop on dark backgrounds) ---
val CoralAdventure = Color(0xFFFF6B6B) // Use for "Adventure" or "Trending" tags
val MintNature = Color(0xFF2ED573) // Use for "Outdoors", "Eco", or "Forest" trips
val LavenderUrban = Color(0xFF9B59B6) // Use for "City", "Culture", or "Nightlife"
val GoldLuxury = Color(0xFFF1C40F) // Use for star ratings, "Premium", or "Top Pick"
val OceanBlue = Color(0xFF2980B9) // Use for "Beach", "Islands", or "Water Sports"

// --- 2. Atmospheric Gradients (For stunning trip cards and hero banners) ---
val GradientSunsetStart = Color(0xFFFF512F)
val GradientSunsetEnd = Color(0xFFF09819)

val GradientAuroraStart = Color(0xFF00B4DB)
val GradientAuroraEnd = Color(0xFF0083B0)

val GradientTwilightStart = Color(0xFF4B1248)
val GradientTwilightEnd = Color(0xFFF0C27B)

// --- 3. Glassmorphism & Overlays (For that premium, frosted-glass look) ---
val GlassSurface = Color(0xFFFFFFFF).copy(alpha = 0.08f) // Soft translucent card backgrounds
val GlassBorder = Color(0xFFFFFFFF).copy(alpha = 0.15f) // Thin borders for glass cards
val DarkScrim = Color(0xFF000000).copy(alpha = 0.5f) // Perfect for dimming images behind text
val LightGlow = TealCyan.copy(alpha = 0.2f) // Use in Modifier.shadow for glowing buttons

// --- Light Mode Backgrounds & Surfaces ---
val CloudWhite = Color(0xFFF8FAFC) // Main Light Background
val PureWhite = Color(0xFFFFFFFF) // Light Surface (Cards, Dialogs)
val SoftGray = Color(0xFFF1F5F9) // Borders or unselected states in Light Mode

// --- Light Mode Text ---
val TextDark = Color(0xFF0F172A) // Primary text in Light Mode (Matches MidnightBlue)
val TextDarkSecondary = Color(0xFF475569) // Subtitles in Light Mode
