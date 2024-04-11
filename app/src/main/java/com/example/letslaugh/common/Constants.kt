package com.example.letslaugh.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import com.example.letslaugh.common.model.NavItem
import com.google.android.gms.wallet.WalletConstants
import java.math.BigDecimal

object Constants {
    val CENTS: BigDecimal = BigDecimal(100)
    val BASE_URL: String = "https://official-joke-api.appspot.com/"
    val STRIPE_BASE_URL: String = "https://oceanic-pepper-psychiatrist.glitch.me"
    val HOME: String = "home"
    val FAVOURITE: String = "favourite"
    val FEEDBACK: String = "feedback"
    val AI: String = "ai"
    val PAYMENT_GATEWAY: String = "payment_gateway"
    val navBarItems: Array<NavItem> = arrayOf(
        NavItem("HOME", icon = Icons.Default.Home, HOME),
        NavItem("FAVOUTIRE", icon = Icons.Default.Favorite, FAVOURITE),
        NavItem("FEEDBACK", icon = Icons.Default.Star, FEEDBACK),
        NavItem("GOOGLE AI", icon = Icons.Default.Face, AI),
        NavItem("PAYMENT GATEWAY", icon = Icons.Default.CurrencyRupee, PAYMENT_GATEWAY),
    )
    const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST
    const val COUNTRY_CODE = "US"
    const val CURRENCY_CODE = "USD"
    const val API_KEY = "rzp_test_U50ESy2ykpfCw9"
}