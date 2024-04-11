package com.example.letslaugh.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.letslaugh.common.Constants
import com.example.letslaugh.presentation.ai.AIScreen
import com.example.letslaugh.presentation.favourites_list.components.JokeFavouriteList
import com.example.letslaugh.presentation.feedback.FeedBackScreen
import com.example.letslaugh.presentation.jokes_generator.components.JokeItem
import com.example.letslaugh.presentation.payment_gateway.PaymentGatewayScreen


@Composable
fun RootGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = Constants.HOME) {
        composable(Constants.HOME) {
            JokeItem(Modifier.padding())
        }
        composable(Constants.FAVOURITE) {
            JokeFavouriteList()
        }
        composable(Constants.FEEDBACK) {
            FeedBackScreen()
        }
        composable(Constants.AI) {
            AIScreen()
        }
        composable(Constants.PAYMENT_GATEWAY) {
            PaymentGatewayScreen()
        }
    }


}
