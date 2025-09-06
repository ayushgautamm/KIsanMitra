// com/example/kishanmitra/Api/ApiService.kt
package com.example.kishanmitra.Api

import com.example.kishanmitra.Api.Model.PredictRequest
import com.example.kishanmitra.Api.Model.PredictResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("predict")
    suspend fun predict(@Body body: PredictRequest): PredictResponse
}
