package com.example.letslaugh.presentation.payment_gateway.google_pay

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letslaugh.presentation.jokes_generator.components.LLTextButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.wallet.PaymentData
import com.google.pay.button.PayButton


@Composable
fun GooglePayScreen() {
    val viewmodel = hiltViewModel<GooglePayViewmodel>()
    val setGooglePayAvailable by viewmodel.setGooglePayAvailable.collectAsState()
    val context = LocalContext.current

    var textData by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        viewmodel.possiblyShowGooglePayButton()
    }

    val result = remember { mutableStateOf<Bitmap?>(null) }

    val paymentDataLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        when(it.resultCode){
            RESULT_OK ->
                it.data?.let { intent ->
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                    PaymentData.getFromIntent(intent)?.let{
                        Log.i("Google Pay result:", it.toString())
                        textData = it.toString()
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                    }
                }

            RESULT_CANCELED -> {
                textData = "Something went wrong"
                Toast.makeText(context, "Canceled", Toast.LENGTH_LONG).show()
            }
        }
    }

    val resolvePaymentForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            result: ActivityResult ->
        when (result.resultCode) {
            RESULT_OK ->
                result.data?.let { intent ->
                    PaymentData.getFromIntent(intent)?.let{
                        Log.i("Google Pay result:", it.toString())
                        textData = it.toString()
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                    }
                }

            RESULT_CANCELED -> {
                textData = "Something went wrong"
                Toast.makeText(context, "Canceled", Toast.LENGTH_LONG).show()
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(20.dp),text = textData, color = Color.White)
        AnimatedVisibility(visible = setGooglePayAvailable) {
            PayButton(
                modifier = Modifier
                    .testTag("payButton")
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                onClick = { onGooglePayButtonClick(viewmodel, resolvePaymentForResult) },
                allowedPaymentMethods = viewmodel.allowedPaymentMethods.toString()
            )
        }
        LLTextButton(btnOnClick = { payWithUPI(context as Activity,paymentDataLauncher)}, buttonTxt = "Pay with Gpay Upi", modifier = Modifier.padding(20.dp) )
    }

}

fun payWithUPI(
    context: Activity,
    paymentDataLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {

    try{
        val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"

        val uri =
            Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "test@axisbank")
                .appendQueryParameter("pn", "Test Merchant")
                .appendQueryParameter("mc", "1234")
                .appendQueryParameter("tr", "123456789")
                .appendQueryParameter("tn", "test transaction note")
                .appendQueryParameter("am", "10.01")
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("url", "https://test.merchant.website")
                .build();
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(uri)
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
        paymentDataLauncher.launch(intent)

    }catch (e : Exception){
        Log.d("Google Pay", e.toString())
    }
}

fun onGooglePayButtonClick(
    viewmodel: GooglePayViewmodel,
//    paymentDataLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    resolvePaymentForResult: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
) {
    val task = viewmodel.getLoadPaymentDataTask(priceCents = 1000L)
    task.addOnCompleteListener { completedTask ->
        if (completedTask.isSuccessful) {
//            completedTask.result.let{
//               Log.i("payment", "Success")
//                val intent = Intent()
//                intent.putExtra("EXTRA_PAYMENT_DATA",it)
//                paymentDataLauncher.launch(intent)
//            }
        } else {
            when (val exception = completedTask.exception) {
                is ResolvableApiException -> {
                    resolvePaymentForResult.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                }
                is ApiException -> {
                    Log.e("Google Pay API error", "Error code: ${exception.statusCode}, Message: ${exception.message}")
                }
                else -> {
                    Log.e("Google Pay Error","Unexpected non API exception")
                }
            }
        }
    }
}


