package com.example.letslaugh.presentation.payment_gateway

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.letslaugh.presentation.ai.components.FancyAnimatedIndicatorWithModifier
import com.example.letslaugh.presentation.ai.components.FancyTab
import com.example.letslaugh.presentation.payment_gateway.google_pay.GooglePayScreen
import com.example.letslaugh.presentation.payment_gateway.razor_pay.RazorPayScreen
import com.example.letslaugh.presentation.payment_gateway.stripe.StripeScreen

@Composable
fun PaymentGatewayScreen() {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Google Pay", "RazorPay", "Stripe", "Tab 4", "Tab 5", "Tab 6")
    Column {
        ScrollableTabRow(
            edgePadding = 0.dp,
            indicator = {
                FancyAnimatedIndicatorWithModifier(state, it)
            },
            selectedTabIndex = state
        ) {
            titles.forEachIndexed { index, title ->
                FancyTab(
                    title = title,
                    onClick = { state = index },
                    selected = (index == state)
                )
            }
        }
        PaymentContent(tab = state)
    }
}

@Composable
fun PaymentContent(tab: Int) {
    val context = LocalContext.current
    when (tab) {
        0 -> GooglePayScreen()
        1 -> RazorPayScreen()
        2 -> StripeScreen()
        else -> Text(
            color = Color.White,
            text = "Fancy payment tab ${tab + 1} selected",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}