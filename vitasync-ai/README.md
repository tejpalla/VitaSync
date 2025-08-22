# VitaSync AI Microservice

### Features
1. Blood demand forecasting per blood type and hospital.
2. Donor behavior prediction (next donation + dropout risk).
3. Stock & inventory optimization to minimize shortages and waste.
4. Emergency detection for sudden spikes in demand.

### Run
pip install -r requirements.txt
uvicorn main:app --reload --port 8000

### Integration
- Use Spring Boot RestTemplate/WebClient to call these endpoints.
- Endpoints:
  - POST /forecast/demand
  - POST /predict/donor
  - POST /optimize/stock
  - GET /alert/emergency