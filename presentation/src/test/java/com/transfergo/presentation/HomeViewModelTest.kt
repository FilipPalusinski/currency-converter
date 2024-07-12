package com.transfergo.presentation

import com.transfergo.domain.repository.ConversionRepository
import com.transfergo.presentation.home.HomeViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private var conversionRepository: ConversionRepository = mockk(relaxUnitFun = true)

    private lateinit var tested: HomeViewModel

    @Before
    fun setup() {
        tested = HomeViewModel(conversionRepository = conversionRepository)
    }

    @Test
    fun `test limit out of range updates limitAlertText`() = runBlocking {

        val amount = 25000f
        val currency = "PLN"

        tested.checkLimitRange(amount, currency)

        val state = tested.uiState.first()
        assert(state.limitAlertText == "20000.0 PLN")
    }

    @Test
    fun `test updateSendingFromText updates sendingFromValueText`() = runBlocking {
        val newText = "300.0"

        tested.updateSendingFromText(newText)

        val state = tested.uiState.first()
        assert(state.sendingFromValueText == newText)
    }

    @Test
    fun `test updateReceiverFromText updates receiverGetsValueText`() = runBlocking {
        val newText = "500.0"

        tested.updateReceiverFromText(newText)

        val state = tested.uiState.first()
        assert(state.receiverGetsValueText == newText)
    }

    @Test
    fun `test swapCurrency swaps sending and receiver currencies`() = runBlocking {
        val initialSendingCurrency = tested.uiState.first().sendingCurrency
        val initialReceiverCurrency = tested.uiState.first().receiverCurrency

        tested.swapCurrency()

        val state = tested.uiState.first()
        assert(state.sendingCurrency == initialReceiverCurrency)
        assert(state.receiverCurrency == initialSendingCurrency)
    }

}

