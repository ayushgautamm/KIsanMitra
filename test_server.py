from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional
import uvicorn

app = FastAPI(title="Crop Supply-Demand Balance API")

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class PredictRequest(BaseModel):
    year: int
    area: float
    production: float
    yield_per_ha: float
    fertilizer: float
    irrigation: float
    crop: str
    season: str

class PredictResponse(BaseModel):
    prediction: str
    probability: Optional[float] = None

@app.get("/")
async def root():
    return {"message": "🌾 Crop Supply-Demand Balance API running!"}

@app.post("/predict", response_model=PredictResponse)
async def predict(request: PredictRequest):
    # Simple mock prediction logic
    prediction = f"Based on your {request.crop} crop with {request.area} acres, the predicted market outlook is positive!"
    probability = 0.85
    
    return PredictResponse(
        prediction=prediction,
        probability=probability
    )

if __name__ == "__main__":
    print("🚀 Starting FastAPI server...")
    print("📱 Android emulator can access via: http://10.0.2.2:8000")
    print("🌐 Local access via: http://127.0.0.1:8000")
    print("📊 API docs available at: http://127.0.0.1:8000/docs")
    
    uvicorn.run(app, host="0.0.0.0", port=8000)
