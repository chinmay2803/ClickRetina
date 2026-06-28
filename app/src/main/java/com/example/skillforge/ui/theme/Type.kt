package com.example.skillforge.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO: download Plus Jakarta Sans .ttf files from Google Fonts and place
// them in app/src/main/res/font/ as:
//   plus_jakarta_sans_regular.ttf
//   plus_jakarta_sans_medium.ttf
//   plus_jakarta_sans_semibold.ttf
//   plus_jakarta_sans_bold.ttf
// then uncomment the FontFamily below and replace PlaceholderFamily usages.

 import com.example.skillforge.R
 val PlusJakartaSans = FontFamily(
     Font(R.font.plusjakartasans_regular, FontWeight.Normal),
     Font(R.font.plusjakartasans_medium, FontWeight.Medium),
     Font(R.font.plusjakartasans_semibold, FontWeight.SemiBold),
     Font(R.font.plusjakartasans_bold, FontWeight.Bold),
 )

private val PlaceholderFamily = FontFamily.SansSerif

val SkillforgeTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)
