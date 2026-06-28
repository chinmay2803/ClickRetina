package com.example.skillforge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SkillforgeColorScheme = lightColorScheme(
    primary = Teal,
    onPrimary = CreamCard,
    background = Cream,
    onBackground = TextPrimary,
    surface = CreamCard,
    onSurface = TextPrimary,
    secondaryContainer = TealSoft,
    onSecondaryContainer = Teal,
    outline = Divider
)

@Composable
fun SkillforgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SkillforgeColorScheme,
        typography = SkillforgeTypography,
        content = content
    )
}
