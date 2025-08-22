from fastapi import FastAPI, Query
from pydantic import BaseModel
import pandas as pd
from prophet import Prophet
from datetime import datetime, timedelta

app = FastAPI(title="Blood Demand Forecast Service")

# Mock historical data generator (replace with DB fetch if available)
def generate_mock_data(blood_type: str):
    today = datetime.today()
    dates = [today - timedelta(days=i) for i in range(90)]
    # Fake demand pattern
    values = [20 + (i % 7) * 2 for i in range(90)]
    df = pd.DataFrame({"ds": dates, "y": values})
    return df

class ForecastResponse(BaseModel):
    bloodType: str
    forecast: list

@app.get("/forecast", response_model=ForecastResponse)
def forecast(bloodType: str = Query(...), days: int = 14):
    # Get historical data
    df = generate_mock_data(bloodType)
    
    # Train model
    model = Prophet(daily_seasonality=True)
    model.fit(df)
    
    # Predict future
    future = model.make_future_dataframe(periods=days)
    forecast = model.predict(future)
    
    # Format response
    result = forecast.tail(days)[["ds", "yhat"]]
    predictions = [{"date": str(row.ds.date()), "predicted_units": round(row.yhat)} 
                   for _, row in result.iterrows()]
    
    return {"bloodType": bloodType, "forecast": predictions}

# Run: uvicorn main:app --reload --port 8000
