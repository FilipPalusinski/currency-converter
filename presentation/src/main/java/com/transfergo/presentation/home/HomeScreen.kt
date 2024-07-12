package com.transfergo.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.transfergo.presentation.R

@Composable
fun CurrencyConverterRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    DoubleLayeredCard(
        uiState = uiState,
        onSwapCurrencyClick = homeViewModel::swapCurrency,
        updateSendingFromText = homeViewModel::updateSendingFromText,
        updateReceiverFromText = homeViewModel::updateReceiverFromText,
        onShowSenderOverlay = homeViewModel::onShowSenderOverlay,
        onShowReceiverOverlay = homeViewModel::onShowReceievrOverlay,
        modifier = modifier
    )

    CurrencyOverlay(
        visible = uiState.senderCurrencyOverlayVisibility,
        onCurrencyClick = homeViewModel::onCurrencyChangeFromSendingConvertCard
    )

    CurrencyOverlay(
        visible = uiState.receiverCurrencyOverlayVisibility,
        onCurrencyClick = homeViewModel::onCurrencyChangeFromReceiverConvertCard
    )
}

@Composable
fun DoubleLayeredCard(
    uiState: HomeState,
    onSwapCurrencyClick: () -> Unit,
    updateSendingFromText: (String) -> Unit,
    updateReceiverFromText: (String) -> Unit,
    onShowSenderOverlay: () -> Unit,
    onShowReceiverOverlay: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(4.dp, 8.dp, 6.dp, 5.dp, 10.dp, 0.dp)
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Column {
                FirstInnerCard(
                    uiState = uiState,
                    updateSendingFromText = updateSendingFromText,
                    onShowSenderOverlay = onShowSenderOverlay
                )
                SecondInnerCard(
                    uiState = uiState,
                    updateReceiverFromText = updateReceiverFromText,
                    onShowReceiverOverlay = onShowReceiverOverlay
                )
            }
            SwapButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 50.dp),
                onClick = onSwapCurrencyClick
            )
            if (uiState.rateValueText.isNotEmpty()) {
                RateInfoText(
                    text = uiState.rateValueText,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun FirstInnerCard(
    uiState: HomeState,
    updateSendingFromText: (String) -> Unit,
    onShowSenderOverlay: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp, 10.dp, 8.dp, 7.dp, 12.dp, 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp, horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(stringResource(id = R.string.sending_from_label), fontSize = 16.sp)
                    Row() {
                        Image(
                            painter = getCurrencyFlag(uiState.sendingCurrency),
                            contentDescription = "Flag",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(uiState.sendingCurrency, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        ChooseButton(
                            onClick = onShowSenderOverlay,
                            modifier = Modifier
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                ) {

                    PlainTextEdit(
                        text = uiState.sendingFromValueText,
                        onTextChange = updateSendingFromText,
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .align(Alignment.CenterEnd),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 40.sp,
                        textColor = Color.Blue
                    )


                }
            }
        }
    }
}

@Composable
fun getCurrencyFlag(currency: String): Painter {
    val country = when (currency) {
        Country.Poland.currency -> Country.Poland
        Country.Ukraine.currency -> Country.Ukraine
        Country.GreatBritain.currency -> Country.GreatBritain
        Country.Germany.currency -> Country.Germany
        else -> throw IllegalArgumentException("Unsupported currency: $currency")
    }
    return painterResource(id = country.flagRes)
}

@Composable
fun PlainTextEdit(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
    fontSize: TextUnit = 30.sp,
    textColor: Color = Color.Black
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier,
        textStyle = textStyle.copy(color = textColor, fontSize = fontSize),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
    )

}

@Composable
fun SecondInnerCard(
    uiState: HomeState,
    updateReceiverFromText: (String) -> Unit,
    onShowReceiverOverlay: () -> Unit,
    ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp, horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(stringResource(id = R.string.receiver_gets_label), fontSize = 16.sp)
                    Row() {
                        Image(
                            painter = getCurrencyFlag(uiState.receiverCurrency),
                            contentDescription = "Flag",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(uiState.receiverCurrency, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        ChooseButton(
                            onClick = onShowReceiverOverlay,
                            modifier = Modifier
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                ) {
                    PlainTextEdit(
                        text = uiState.receiverGetsValueText,
                        onTextChange = updateReceiverFromText,
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .align(Alignment.CenterEnd),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 40.sp,
                        textColor = Color.Black
                    )

                }
            }
        }
    }
}

@Composable
fun SwapButton(modifier: Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(Color.Blue, RoundedCornerShape(percent = 100))
            .size(25.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.SwapVert,
            contentDescription = "Swap",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun ChooseButton(modifier: Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(25.dp)
            .padding(bottom = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "Choose",
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun RateInfoText(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .background(Color.Black, RoundedCornerShape(percent = 50))
            .padding(horizontal = 5.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyConverterRoute()
}
