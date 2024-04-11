package com.example.letslaugh.domain.use_cases.chekout_for_stripe

import com.example.letslaugh.common.Resource
import com.example.letslaugh.data.remote.dto.toStripeResponse
import com.example.letslaugh.domain.model.StripeResponse
import com.example.letslaugh.domain.repository.StripeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CheckoutForStripeUseCase @Inject constructor(private val stripeRepository: StripeRepository) {

    operator fun invoke() : Flow<Resource<StripeResponse>> = flow {
        try {
            emit(Resource.Loading<StripeResponse>())
            val joke = stripeRepository.checkout().toStripeResponse()
            emit(Resource.Success<StripeResponse>(data = joke))
        }catch (e : HttpException){
            emit(Resource.Error<StripeResponse>(message = e.localizedMessage?:"An Error occurred during process"))
        }catch (e : IOException) {
            emit(Resource.Error<StripeResponse>(message = "Couldn't reach the server. Please check your Internet Connection"))
        }
    }
}