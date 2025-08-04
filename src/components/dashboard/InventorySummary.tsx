import React from 'react';
import { Package, AlertTriangle, Clock } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { mockBloodUnits, bloodTypeColors } from '@/data/mockData';
import { BloodType } from '@/types';

export const InventorySummary: React.FC = () => {
  const bloodTypeCounts = mockBloodUnits.reduce((acc, unit) => {
    if (unit.status === 'AVAILABLE') {
      acc[unit.bloodType] = (acc[unit.bloodType] || 0) + 1;
    }
    return acc;
  }, {} as Record<BloodType, number>);

  const expiringUnits = mockBloodUnits.filter(unit => {
    const daysUntilExpiry = Math.ceil(
      (unit.expiryDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24)
    );
    return daysUntilExpiry <= 7 && unit.status === 'AVAILABLE';
  });

  const getDaysUntilExpiry = (expiryDate: Date) => {
    return Math.ceil((expiryDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6" data-onboarding="inventory-summary">
      {/* Blood Type Inventory */}
      <Card className="hover-lift">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <Package className="h-5 w-5" />
            <span>Blood Type Inventory</span>
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            {Object.entries(bloodTypeCounts).map(([bloodType, count]) => (
              <div key={bloodType} className="flex items-center justify-between p-3 bg-muted rounded-2xl">
                <Badge variant="bloodType" className={bloodTypeColors[bloodType as BloodType]}>
                  {bloodType}
                </Badge>
                <span className="font-semibold text-lg">{count}</span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Expiring Units */}
      <Card className="hover-lift">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <AlertTriangle className="h-5 w-5 text-warning" />
            <span>Expiring Soon</span>
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-3">
          {expiringUnits.length === 0 ? (
            <p className="text-muted-foreground text-center py-4">
              No units expiring in the next 7 days
            </p>
          ) : (
            expiringUnits.map(unit => {
              const daysLeft = getDaysUntilExpiry(unit.expiryDate);
              return (
                <div key={unit.id} className="flex items-center justify-between p-3 border rounded-2xl">
                  <div className="flex items-center space-x-3">
                    <Badge variant="bloodType" className={bloodTypeColors[unit.bloodType]}>
                      {unit.bloodType}
                    </Badge>
                    <span className="text-sm text-muted-foreground">{unit.id}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Clock className="h-4 w-4 text-warning" />
                    <span className={`text-sm font-medium ${
                      daysLeft <= 3 ? 'text-destructive' : 'text-warning'
                    }`}>
                      {daysLeft} days
                    </span>
                  </div>
                </div>
              );
            })
          )}
        </CardContent>
      </Card>
    </div>
  );
};