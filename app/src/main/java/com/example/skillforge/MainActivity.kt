package com.example.skillforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.skillforge.ui.navigation.SkillforgeNavGraph
import com.example.skillforge.ui.theme.SkillforgeTheme
import com.example.skillforge.viewmodel.SkillforgeViewModel


class MainActivity : ComponentActivity() {
    private val viewModel: SkillforgeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkillforgeTheme {
                SkillforgeNavGraph()
            }
        }
    }
}