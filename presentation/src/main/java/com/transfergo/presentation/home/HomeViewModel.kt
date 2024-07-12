package com.transfergo.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.transfergo.common.Resource
import com.transfergo.domain.repository.ConversionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var conversionRepository: ConversionRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeState> =
        MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    fun updateSendingFromText(newText: String) {
        _uiState.update {
            it.copy(sendingFromValueText = newText)
        }
        val amount = newText.toFloatOrNull()
        val sendingCurrency = _uiState.value.sendingCurrency
        val receiverCurrency = _uiState.value.receiverCurrency
        if (amount != null) {
            viewModelScope.launch {
                sendingCurrencyConvert(sendingCurrency, receiverCurrency, amount)
            }
        }
    }

    private suspend fun sendingCurrencyConvert(from: String, to: String, amount: Float) = viewModelScope.launch {
        val result = conversionRepository.convertCurrency(from, to, amount)
        var amountResult = result.data?.toAmount.toString()
        var rateResult = result.data?.rate.let {
            String.format("%.2f", it)
        }
        if (amountResult.isEmpty()) {
            amountResult = "0.00"
        }
        _uiState.update { currentState ->
            when (result) {
                is Resource.Success -> currentState.copy(
                    receiverGetsValueText = amountResult,
                    apiError = ""
                )

                is Resource.Error -> currentState.copy(
                    receiverGetsValueText = "0.00",
                    apiError = result.message
                )
            }
        }
        if (rateResult.isNotEmpty()) {
            _uiState.update { it.copy(rateResult = rateResult) }
            onRateValueTextChange()
        }
    }

    fun updateReceiverFromText(newText: String) {
        _uiState.update {
            it.copy(receiverGetsValueText = newText)
        }
        val amount = newText.toFloatOrNull()
        val sendingCurrency = _uiState.value.sendingCurrency
        val receiverCurrency = _uiState.value.receiverCurrency
        if (amount != null) {
            viewModelScope.launch {
                receiverCurrencyConvert(receiverCurrency, sendingCurrency, amount)
            }
        }
    }

    private suspend fun receiverCurrencyConvert(from: String, to: String, amount: Float) = viewModelScope.launch {
        val result = conversionRepository.convertCurrency(from, to, amount)
        var amountResult = result.data?.toAmount.toString()
        var rateResult = result.data?.rate.let {
            String.format("%.2f", it)
        }
        if (amountResult.isEmpty()) {
            amountResult = "0.00"
        }
        _uiState.update { currentState ->
            when (result) {
                is Resource.Success -> currentState.copy(
                    sendingFromValueText = amountResult,
                    apiError = ""
                )

                is Resource.Error -> currentState.copy(
                    sendingFromValueText = "0.00",
                    apiError = result.message
                )
            }
        }
        if (rateResult.isNotEmpty()) {
            onRateValueTextChange()
        }
    }

    fun swapCurrency() {
        val tempCurrency = _uiState.value.sendingCurrency
        _uiState.update {
            it.copy(
                sendingCurrency = it.receiverCurrency,
                receiverCurrency = tempCurrency
            )
        }
        refreshApiCall()

    }

    private fun refreshApiCall() {
        val amount = _uiState.value.sendingFromValueText.toFloatOrNull()
        val sendingCurrency = _uiState.value.sendingCurrency
        val receiverCurrency = _uiState.value.receiverCurrency
        if (amount != null) {
            viewModelScope.launch {
                sendingCurrencyConvert(sendingCurrency, receiverCurrency, amount)
            }
        }
    }

    private fun onRateValueTextChange() {
        val sendingCurrency = _uiState.value.sendingCurrency
        val rate = _uiState.value.rateResult
        val receiverCurrency = _uiState.value.receiverCurrency
        _uiState.update {
            it.copy(
                rateValueText = "1 $sendingCurrency = $rate $receiverCurrency",
            )
        }
    }

    fun onShowSenderOverlay() {
        _uiState.update { it.copy(senderCurrencyOverlayVisibility = true) }
    }

    fun onShowReceievrOverlay() {
        _uiState.update { it.copy(receiverCurrencyOverlayVisibility = true) }
    }

    fun onCurrencyChangeFromSendingConvertCard(selectedCountry: Country) {
        _uiState.update {
            it.copy(
                senderCurrencyOverlayVisibility = false,
                sendingCurrency = selectedCountry.currency
            )
        }
        refreshApiCall()
    }

    fun onCurrencyChangeFromReceiverConvertCard(selectedCountry: Country) {
        _uiState.update {
            it.copy(
                receiverCurrencyOverlayVisibility = false,
                receiverCurrency = selectedCountry.currency
            )
        }
        refreshApiCall()
    }

}


data class HomeState(
    val apiError: String? = "",
    val rateResult: String = "",
    val rateValueText: String = "",
    val sendingFromValueText: String = "0.00",
    val receiverGetsValueText: String = "0.00",
    val sendingCurrency: String = "PLN",
    val receiverCurrency: String = "UAH",
    val senderCurrencyOverlayVisibility: Boolean = false,
    val receiverCurrencyOverlayVisibility: Boolean = false
)
