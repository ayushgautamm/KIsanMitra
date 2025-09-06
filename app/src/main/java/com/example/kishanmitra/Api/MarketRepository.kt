// com/example/kishanmitra/Api/MarketRepository.kt
package com.example.kishanmitra.Api

import com.example.kishanmitra.Api.Model.PredictRequest
import com.example.kishanmitra.Api.Model.PredictResponse

class MarketRepository(private val api: ApiService) {
    suspend fun submitPrediction(req: PredictRequest): PredictResponse = api.predict(req)
}
