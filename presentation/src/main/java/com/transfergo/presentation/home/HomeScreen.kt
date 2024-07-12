package com.transfergo.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun Greeting(
    homeViewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    Column() {
        Button(onClick = {
            scope.launch {
                homeViewModel.convertCurrency("PLN", "UAH", 300.0f)
            }

        }) {
            Text(text = "Click")
        }

        Text(
            text = "Amount: ${uiState.conversionResponse?.toAmount}, rate: ${uiState.conversionResponse?.rate}",
            modifier = modifier
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}
