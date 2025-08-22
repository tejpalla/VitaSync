import pandas as pd
from datetime import datetime, timedelta
from db.db_connection import engine

def predict_next_donation(donor_id: int):
    query = f"SELECT id, last_donation_date FROM users WHERE id={donor_id} AND role='DONOR'"
    donor = pd.read_sql(query, engine)
    if donor.empty or pd.isna(donor.loc[0,'last_donation_date']):
        next_donation = datetime.now() + timedelta(days=30)
    else:
        last_donation = donor.loc[0,'last_donation_date']
        next_donation = last_donation + timedelta(days=90)
    risk_of_dropout = 1 if (datetime.now() - next_donation).days > 180 else 0
    return {
        "donor_id": donor_id,
        "next_donation_date": next_donation.strftime("%Y-%m-%d"),
        "risk_of_dropout": risk_of_dropout
    }