package com.transfergo.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.transfergo.presentation.common.BottomOverlay
import com.transfergo.presentation.common.OverlayScreenBackground

val countries = listOf(
    Country.Poland, Country.Germany, Country.GreatBritain, Country.Ukraine
)

@Composable
fun CurrencyOverlay(
    visible: Boolean, onCurrencyClick: (Country) -> Unit, modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        if (visible) {
            OverlayScreenBackground()
        }

        BottomOverlay(
            visible = visible, modifier = Modifier.align(Alignment.BottomCenter)
        ) {

            CountrySearchScreen(onCurrencyClick)

        }

    }
}

@Composable
fun CountrySearchScreen(
    onCurrencyClick: (Country) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredCountries = countries.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        SearchBar(searchQuery) {
            searchQuery = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        CountryList(onCurrencyClick, countries = filteredCountries)
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}

@Composable
fun CountryList(
    onCurrencyClick: (Country) -> Unit,
    countries: List<Country>
) {
    Column {
        countries.forEach { country ->
            CountryItem(onCurrencyClick, country = country)
        }
    }
}

@Composable
fun CountryItem(
    onCurrencyClick: (Country) -> Unit,
    country: Country
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCurrencyClick(country) }
    ) {
        Image(
            painter = painterResource(id = country.flagRes),
            contentDescription = "${country.name} flag",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = country.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = country.currency, fontSize = 14.sp, color = Color.Gray)
        }
    }
}


