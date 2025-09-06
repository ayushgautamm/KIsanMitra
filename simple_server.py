import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI()

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
    probability: float = 0.85

@app.get("/")
async def root():
    return {"message": "🌾 Crop Supply-Demand Balance API running!"}

@app.post("/predict")
async def predict(request: PredictRequest):
    return PredictResponse(
        prediction=f"Based on your {request.crop} crop with {request.area} acres, the predicted market outlook is positive!",
        probability=0.85
    )

if __name__ == "__main__":
    print("🚀 Starting server on 0.0.0.0:8000")
    uvicorn.run(app, host="0.0.0.0", port=8000)





