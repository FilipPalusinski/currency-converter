package com.transfergo.presentation.home

import com.transfergo.presentation.R

sealed class Country(val name: String, val currency: String, val flagRes: Int) {
    object Poland : Country("Poland", "PLN", R.drawable.flag_pl)
    object Germany : Country("Germany", "EUR", R.drawable.flag_de)
    object GreatBritain : Country("Great Britain", "GBP", R.drawable.flag_uk)
    object Ukraine : Country("Ukraine", "UAH", R.drawable.flag_ua)
}
