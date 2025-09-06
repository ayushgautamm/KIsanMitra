package com.example.kishanmitra.Api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kishanmitra.Api.Model.PredictRequest
import com.example.kishanmitra.Api.Model.PredictResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MarketViewModel(private val repo: MarketRepository) : ViewModel() {

    private val _state = MutableStateFlow<UiState<PredictResponse>>(UiState.Idle)
    val state: StateFlow<UiState<PredictResponse>> = _state

    fun submit(body: PredictRequest) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                println("🚀 Submitting prediction request: $body")
                val res: PredictResponse = repo.submitPrediction(body)
                println("✅ Prediction response received: $res")
                _state.value = UiState.Success(res)
            } catch (e: Exception) {
                println("❌ Error in prediction: ${e.message}")
                e.printStackTrace()
                _state.value = UiState.Error("Network error: ${e.message ?: "Failed to connect to server"}")
            }
        }
    }
}
