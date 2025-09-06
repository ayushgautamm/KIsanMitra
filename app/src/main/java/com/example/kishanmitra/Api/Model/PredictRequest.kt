package com.example.kishanmitra.Api.Model

data class PredictRequest(
    val year: Int,
    val area: Double,
    val production: Double,
    val yield_per_ha: Double,
    val fertilizer: Double,
    val irrigation: Double,
    val crop: String,
    val season: String
)

data class PredictResponse(
    val prediction: String,
    val probability: Double? = null
)
