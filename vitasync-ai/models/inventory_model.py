from pulp import LpProblem, LpVariable, LpMinimize, lpSum

def optimize(blood_bank_id: int):
    blood_types = ["A_POSITIVE","O_POSITIVE","B_POSITIVE","AB_POSITIVE","O_NEGATIVE"]
    stocks = {bt: LpVariable(bt, lowBound=0) for bt in blood_types}
    forecasted_demand = {bt: 10 for bt in blood_types}
    prob = LpProblem("StockOptimization", LpMinimize)
    prob += lpSum([(stocks[bt]-forecasted_demand[bt])**2 for bt in blood_types])
    prob.solve()
    optimized_stock = {bt: int(stocks[bt].value()) for bt in blood_types}
    return {"blood_bank_id": blood_bank_id, "recommended_stock": optimized_stock}