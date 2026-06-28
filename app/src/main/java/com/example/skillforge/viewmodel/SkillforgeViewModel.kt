package com.example.skillforge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillforge.data.Category
import com.example.skillforge.data.SkillforgeApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    data object Loading : UiState
    data class Success(val categories: List<Category>) : UiState
    data class Error(val message: String) : UiState
}

class SkillforgeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = SkillforgeApi.fetchData()
                _uiState.value = UiState.Success(response.categories)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    // Convenience helpers used by the nav graph since we don't have a
    // real backend with per-course/per-lesson endpoints — everything
    // comes from the one cached response.
    fun findCourse(title: String) =
        (uiState.value as? UiState.Success)?.categories
            ?.flatMap { it.courses }
            ?.firstOrNull { it.title == title }

    fun findLesson(courseTitle: String, lessonTitle: String) =
        findCourse(courseTitle)?.lessons?.firstOrNull { it.title == lessonTitle }
}
