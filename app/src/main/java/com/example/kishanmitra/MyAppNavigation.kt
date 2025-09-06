package com.example.kishanmitra

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.example.kishanmitra.Screens.*
import com.example.kishanmitra.Api.MarketViewModel
import com.example.kishanmitra.Api.MarketRepository
import com.example.kishanmitra.Api.NetworkModule
import com.example.kishanmitra.Subsections.AIHelperScreen
import com.example.kishanmitra.Subsections.CalamityAlarmScreen
import com.example.kishanmitra.Subsections.ComplaintsSectionScreen
import com.example.kishanmitra.Subsections.ConsumerHelplineScreen
import com.example.kishanmitra.Subsections.CropInfoScreen
import com.example.kishanmitra.Subsections.CultivationInputScreen
import com.example.kishanmitra.Subsections.DirectConsumerReachScreen
import com.example.kishanmitra.Subsections.IndustrialAgreementScreen
import com.example.kishanmitra.Subsections.KisanMitraSevaScreen
import com.example.kishanmitra.Subsections.MarketRatingScreen
import com.example.kishanmitra.Subsections.RealTimeInteractionScreen
import com.example.kishanmitra.screens.sections.OptimalCropScreen
import com.example.kishanmitra.screens.sections.PricePredictionScreen

@Composable
fun MyAppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "cropSuggestion"
    ) {
        composable("cropSuggestion") {
            CropSuggestionScreen(navController)
        }

        composable("farmerInput") {
            val api = NetworkModule.provideApi(
                NetworkModule.provideRetrofit(
                    NetworkModule.provideOkHttp(),
                    NetworkModule.provideGson()
                )
            )
            val repository = MarketRepository(api)
            val vm: MarketViewModel = MarketViewModel(repository)
            FarmerInputScreen(navController, vm)
        }

        composable("marketProjection") {
            val api = NetworkModule.provideApi(
                NetworkModule.provideRetrofit(
                    NetworkModule.provideOkHttp(),
                    NetworkModule.provideGson()
                )
            )
            val repository = MarketRepository(api)
            val vm: MarketViewModel = MarketViewModel(repository)
            MarketProjectionScreen(navController, vm)
        }

        // Crop detail with argument
        composable(
            route = "cropDetail/{cropName}",
            arguments = listOf(navArgument("cropName") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("cropName").orEmpty()
            val cropName = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
            PredictedPriceScreen(
                cropName = cropName,
                onBack = { navController.popBackStack() }
            )
        }

        // Other routes (placeholders where implementations exist)
        composable("rtInteraction") { RealTimeInteractionScreen(navController) }
        composable("optimalCrop") { OptimalCropScreen(navController) }
        composable("pricePrediction") { PricePredictionScreen(navController) }
        composable("cultivationInput") { CultivationInputScreen(navController) }
        composable("kisanMitraSeva") { KisanMitraSevaScreen(navController) }
        composable("complaints") { ComplaintsSectionScreen(navController) }
        composable("calamityAlarm") { CalamityAlarmScreen(navController) }
        composable("marketRating") { MarketRatingScreen(navController) }
        composable("cropInfo") { CropInfoScreen(navController) }
        composable("aiHelper") { AIHelperScreen(navController) }
        composable("consumerHelpline") { ConsumerHelplineScreen(navController) }
        composable("industrialAgreement") { IndustrialAgreementScreen(navController) }
        composable("directReach") { DirectConsumerReachScreen(navController) }
    }
}
