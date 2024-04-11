package com.example.letslaugh.presentation.payment_gateway.razor_pay

import android.content.Context
import androidx.lifecycle.ViewModel
import com.razorpay.Checkout
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RazorPayViewmodel @Inject constructor(context: Context) : ViewModel() {

    init {
        Checkout.preload(context)
    }

    fun startPayment() : JSONObject{

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
//            options.put("theme.color", "#3399cc")
            options.put("theme.color", "#99CC33")
            options.put("currency","INR")
            // comment this order id for testing and enable it to use production
//            options.put("order_id", "order_DBJOWzybf0sJbb")
            options.put("amount","50000")//pass amount in currency subunits

            val retryObj =  JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email","gaurav.kumar@example.com")
            prefill.put("contact","+919876543210")

            options.put("prefill",prefill)
            return  options
        }catch (e: Exception){
            e.printStackTrace()
            return JSONObject()
        }
    }

}