import pandas as pd
from prophet import Prophet
from db.db_connection import engine

def predict(hospital_id: int, city: str, days: int):
    query = f"""
        SELECT required_by_date AS ds, blood_type, SUM(units_required) AS y
        FROM transfusion_requests
        GROUP BY ds, blood_type
        ORDER BY ds
    """
    df = pd.read_sql(query, engine)
    forecasts = {}
    for bt in df['blood_type'].unique():
        temp = df[df['blood_type'] == bt][['ds','y']]
        model = Prophet(daily_seasonality=True)
        model.fit(temp)
        future = model.make_future_dataframe(periods=days)
        forecast = model.predict(future)
        forecasts[bt] = forecast[['ds','yhat']].tail(days).to_dict(orient='records')
    return forecasts