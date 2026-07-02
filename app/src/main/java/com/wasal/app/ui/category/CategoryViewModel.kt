package com.wasal.app.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryUiState(
    val isLoading: Boolean = true,
    val categoryName: String = "",
    val categoryImage: String? = null,
    val groups: List<CategoryRepository.CategoryGroup> = emptyList(),
    val filteredGroups: List<CategoryRepository.CategoryGroup> = emptyList(),
    val totalItems: Int = 0,
    val searchQuery: String = "",
    val error: String? = null
)

class CategoryViewModel : ViewModel() {
    private val repo = CategoryRepository()
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun load(categoryName: String) {
        _uiState.update { it.copy(isLoading = true, categoryName = categoryName, error = null) }
        viewModelScope.launch {
            try {
                val groups = repo.getCategoryGroups(categoryName)
                val catImage = groups.firstOrNull()?.items?.firstOrNull { !it.imageUrl.isNullOrBlank() }?.imageUrl
                    ?: groups.firstOrNull()?.items?.firstOrNull()?.imageUrl
                _uiState.update { it.copy(
                    isLoading = false,
                    groups = groups,
                    filteredGroups = groups,
                    totalItems = groups.sumOf { g -> g.items.size },
                    categoryImage = catImage,
                    error = null
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "حدث خطأ أثناء تحميل البيانات") }
            }
        }
    }

    fun onSearchChanged(query: String) {
        val groups = _uiState.value.groups
        val filtered = if (query.isBlank()) {
            groups
        } else {
            groups.mapNotNull { group ->
                val matchedItems = group.items.filter {
                    it.nameAr.contains(query, ignoreCase = true) || it.description?.contains(query, ignoreCase = true) == true
                }
                if (matchedItems.isNotEmpty()) group.copy(items = matchedItems) else null
            }
        }
        _uiState.update { it.copy(
            searchQuery = query,
            filteredGroups = filtered,
            totalItems = filtered.sumOf { g -> g.items.size }
        )}
    }
}
