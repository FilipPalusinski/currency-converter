package com.transfergo.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.transfergo.common.Resource
import com.transfergo.domain.models.ConversionResponse
import com.transfergo.domain.repository.ConversionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var conversionRepository: ConversionRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeState> =
        MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    suspend fun convertCurrency(from: String, to: String, amount: Float) = viewModelScope.launch {
        val result = conversionRepository.convertCurrency(from, to, amount)
        _uiState.value = when (result) {
            is Resource.Success -> HomeState(conversionResponse = result.data)
            is Resource.Error -> HomeState(apiError = result.message)
        }
    }
}


data class HomeState(
    val conversionResponse: ConversionResponse? = null,
    val apiError: String? = ""
)
