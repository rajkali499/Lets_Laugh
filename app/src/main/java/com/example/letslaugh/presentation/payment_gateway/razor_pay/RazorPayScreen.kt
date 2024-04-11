package com.example.letslaugh.presentation.payment_gateway.razor_pay

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letslaugh.common.Constants
import com.example.letslaugh.presentation.jokes_generator.components.LLTextButton
import com.razorpay.Checkout

@Composable
fun RazorPayScreen() {

    val viewmodel = hiltViewModel<RazorPayViewmodel>()
//    val setGooglePayAvailable by viewmodel.setGooglePayAvailable.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val checkout = Checkout()
    var textData by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {

        checkout.setKeyID(Constants.API_KEY)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(20.dp), text = textData, color = Color.White)
        LLTextButton(
            btnOnClick = { initPayment(viewmodel,checkout,activity,context)},
            buttonTxt = "Pay now using RazorPay",
            modifier = Modifier.padding()
        )
    }
}

fun initPayment(
    viewmodel: RazorPayViewmodel,
    checkout: Checkout,
    activity: Activity,
    context: Context,) {

    /*
       *  You need to pass the current activity to let Razorpay create CheckoutActivity
       * */
    Checkout.clearUserData(context)
    val jsonResult = viewmodel.startPayment()
    checkout.open(activity,jsonResult)
}
