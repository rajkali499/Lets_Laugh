package com.example.letslaugh.presentation.payment_gateway.stripe

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letslaugh.common.Resource
import com.example.letslaugh.domain.model.StripeResponse
import com.example.letslaugh.domain.use_cases.chekout_for_stripe.CheckoutForStripeUseCase
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher
import com.stripe.android.paymentsheet.PaymentSheet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StripeViewmodel @Inject constructor(context: Context, private val checkoutForStripeUseCase: CheckoutForStripeUseCase) : ViewModel() {

    companion object {
        const val STRIPE_API_KEY = "pk_test_51P2XehSIzAdpdmgefoQmUANZrJlJMCoZOAj3M9jWADsCRf2C4akMvDcplnNYyV7b12UR2yaGr6TKgY9nvzNtfkJ500k94sfTj2"
        const val SECRET_KEY = "sk_test_51P2XehSIzAdpdmge768tb5gerFERnaecq8LoTvvCnPMTLMYaBrHdgFF8ePa5K8fBg2cteQTdJSHctiUQLiQpBcEi00OmO6Kvv2"
    }

    lateinit var stripe: Stripe
    private val _state = mutableStateOf(StripeState())
    private var customerConfig = mutableStateOf<PaymentSheet.CustomerConfiguration?>(null)
    var paymentIntentClientSecret = mutableStateOf<String?>(null)
    var googlePayLauncher : GooglePayLauncher? = null

    init {
        //       These will come from the server side
        checkoutForStripeUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = result.data?.let { StripeState(result = StripeResponse(publishableKey = it.publishableKey , paymentIntent = it.paymentIntent, customer = it.customer, ephemeralKey = it.ephemeralKey))  }!!
                    Toast.makeText(context,"Api Success",Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    _state.value = StripeState(isLoading = true)
                    Toast.makeText(context,"Api Loading",Toast.LENGTH_LONG).show()
                }

                is Resource.Error -> {
                    _state.value =
                        StripeState(error = result.message ?: "An Unexpected Error occurred")
                    Toast.makeText(context,result.message ?: "An Unexpected Error occurred",Toast.LENGTH_LONG).show()
                }
            }
        }.launchIn(viewModelScope)

        // For Testing we can use this
//        _state.value.result.publishableKey?.let { PaymentConfiguration.init(context, publishableKey = it) }
        PaymentConfiguration.init(context, STRIPE_API_KEY)
        stripe = Stripe(
            context,
            PaymentConfiguration.getInstance(context).publishableKey,
            "acct_1P2XehSIzAdpdmge"
        )
        val componentActivity = context as? ComponentActivity
        googlePayLauncher = componentActivity?.let {
            GooglePayLauncher(
                activity = it,
                config = GooglePayLauncher.Config(
                    environment = GooglePayEnvironment.Test,
                    merchantCountryCode = "US",
                    merchantName = "Widget Store"
                ),
                readyCallback = ::onGooglePayReady,
                resultCallback = ::onGooglePayResult
            )
        }
    }

    fun initPayment(paymentSheet: PaymentSheet) {
        customerConfig.value = _state.value.result.customer?.let {
            _state.value.result.ephemeralKey?.let { it1 ->
                PaymentSheet.CustomerConfiguration(
                    it,
                    it1
                )
            }
        }
        val googlePayConfiguration = PaymentSheet.GooglePayConfiguration(
            environment = PaymentSheet.GooglePayConfiguration.Environment.Test,
            countryCode = "US",
            currencyCode = "USD" // Required for Setup Intents, optional for Payment Intents
        )
        _state.value.result.paymentIntent?.let {
            paymentSheet.presentWithPaymentIntent(
                it,
                PaymentSheet.Configuration(
                    merchantDisplayName = "My merchant name",
                    customer = customerConfig.value,
                    // Set `allowsDelayedPaymentMethods` to true if your business handles
                    // delayed notification payment methods like US bank accounts.
                    allowsDelayedPaymentMethods = true,
                    defaultBillingDetails = PaymentSheet.BillingDetails(
                        name = "Jenny Rosen",
                        email = "jenny.rosen@example.com",
                        phone = "888-888-8888"
                    ),
                    googlePay = googlePayConfiguration
                )
            )
        }
    }

    fun doGooglePaymentWithStripe() {
        _state.value.result.paymentIntent?.let { googlePayLauncher?.presentForSetupIntent(it, currencyCode = "US") }
        _state.value.result.paymentIntent?.let { googlePayLauncher?.presentForPaymentIntent(it) }
    }

    private fun onGooglePayResult(result: GooglePayLauncher.Result) {
        when (result) {
            GooglePayLauncher.Result.Completed -> {
                Log.i("Gpay Stripe","Success")
            }
            GooglePayLauncher.Result.Canceled -> {
                Log.i("Gpay Stripe","Cancelled")
            }
            is GooglePayLauncher.Result.Failed -> {
                Log.i("Gpay Stripe","Failed")
            }
        }
    }

}