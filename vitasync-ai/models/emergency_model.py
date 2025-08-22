import pandas as pd
from db.db_connection import engine

def detect():
    query = """
        SELECT blood_type, units_required
        FROM transfusion_requests
        WHERE required_by_date > NOW() - INTERVAL '7 days'
    """
    df = pd.read_sql(query, engine)
    alerts = []
    for bt in df['blood_type'].unique():
        series = df[df['blood_type']==bt]['units_required']
        if len(series) < 2:
            continue
        mean, std = series.mean(), series.std()
        latest = series.iloc[-1]
        if latest > mean + 2*std:
            alerts.append({"blood_type": bt, "alert_level": "HIGH"})
    return alerts