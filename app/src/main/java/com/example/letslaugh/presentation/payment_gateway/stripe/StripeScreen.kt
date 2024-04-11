package com.example.letslaugh.presentation.payment_gateway.stripe

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.letslaugh.presentation.jokes_generator.components.LLTextButton
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun StripeScreen() {

    val viewmodel = hiltViewModel<StripeViewmodel>()
    val setGooglePayAvailable by remember {
        mutableStateOf(false)
    }
    var textData by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for lifecycle events
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Toast.makeText(
                        context,
                        "DisposableEffectWithLifeCycle ON_CREATE",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Lifecycle.Event.ON_START -> {
                    Toast.makeText(
                        context,
                        "DisposableEffectWithLifeCycle ON_START",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Lifecycle.Event.ON_RESUME -> {
                    Toast.makeText(
                        context,
                        "DisposableEffectWithLifeCycle ON_RESUME",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Toast.makeText(
                        context,
                        "DisposableEffectWithLifeCycle ON_PAUSE",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Lifecycle.Event.ON_STOP -> {
                    Toast.makeText(
                        context,
                        "DisposableEffectWithLifeCycle ON_STOP",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Toast.makeText(
                        context,
                        "DisposableEffectWithLifeCycle ON_DESTROY",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)

            Toast.makeText(
                context,
                "DisposableEffectWithLifeCycle composition EXIT",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

//   LaunchedEffect(key1 = Unit) {
//        googlePayLauncher = GooglePayPaymentMethodLauncher(
//           activity = context as ComponentActivity,
//           config = GooglePayPaymentMethodLauncher.Config(
//               environment = GooglePayEnvironment.Test,
//               merchantCountryCode = "FR",
//               merchantName = "Widget Store"
//           ),
//           readyCallback = ::onGooglePayReady,
//           resultCallback = ::onGooglePayResult
//       )
//   }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(20.dp), text = textData, color = Color.White)
        LLTextButton(
            btnOnClick = {
                viewmodel.initPayment(paymentSheet)
            },
            buttonTxt = "Pay using Stripe Payment Sheet",
            modifier = Modifier.padding()
        )
        AnimatedVisibility(modifier = Modifier.padding(20.dp), visible = true) {
            LLTextButton(
                btnOnClick = {
                   viewmodel.doGooglePaymentWithStripe()
                },
                buttonTxt = "Pay using Gpay",
                modifier = Modifier.padding()
            )
        }
    }
}

fun onGooglePayResult(result: GooglePayPaymentMethodLauncher.Result) {
    when (result) {
        is GooglePayPaymentMethodLauncher.Result.Completed -> {
            // Payment details successfully captured.
            // Send the paymentMethodId to your server to finalize payment.
            val paymentMethodId = result.paymentMethod.id
        }

        GooglePayPaymentMethodLauncher.Result.Canceled -> {
            // User canceled the operation
        }

        is GooglePayPaymentMethodLauncher.Result.Failed -> {
            // Operation failed; inspect `result.error` for the exception
        }
    }
}

fun onGooglePayReady(b: Boolean) {

}


fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
    when (paymentSheetResult) {
        is PaymentSheetResult.Canceled -> {
//            Toast.makeText(LocalContext,"",Toast.LENGTH_LONG).show()
            Log.w("Stripe", "Cancelled")
            print("Canceled")
        }

        is PaymentSheetResult.Failed -> {
            Log.w("Stripe", "Error: ${paymentSheetResult.error}")
            print("Error: ${paymentSheetResult.error}")
        }

        is PaymentSheetResult.Completed -> {
            // Display for example, an order confirmation screen
            Log.w("Stripe", "Completed")
            print("Completed")
        }
    }
}


