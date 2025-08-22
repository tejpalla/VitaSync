from fastapi import FastAPI
from pydantic import BaseModel
from models import demand_model, donor_model, inventory_model, emergency_model

app = FastAPI(title="VitaSync AI Microservice")

class ForecastRequest(BaseModel):
    hospital_id: int
    city: str
    days: int

class DonorPredictionRequest(BaseModel):
    donor_id: int

class StockOptimizationRequest(BaseModel):
    blood_bank_id: int

@app.post("/forecast/demand")
def forecast_demand(req: ForecastRequest):
    forecast = demand_model.predict(req.hospital_id, req.city, req.days)
    return {"hospital_id": req.hospital_id, "forecast": forecast}

@app.post("/predict/donor")
def predict_donor(req: DonorPredictionRequest):
    return donor_model.predict_next_donation(req.donor_id)

@app.post("/optimize/stock")
def optimize_stock(req: StockOptimizationRequest):
    return inventory_model.optimize(req.blood_bank_id)

@app.get("/alert/emergency")
def emergency_alerts():
    return {"alerts": emergency_model.detect()}