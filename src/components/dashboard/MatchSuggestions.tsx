import React from 'react';
import { Target, User, Droplet, ChevronRight } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { mockMatchSuggestions, mockPatients, mockBloodUnits, bloodTypeColors, urgencyColors } from '@/data/mockData';

export const MatchSuggestions: React.FC = () => {
  const recentSuggestions = mockMatchSuggestions.slice(0, 3);

  const getPatientName = (patientId: string) => {
    return mockPatients.find(p => p.id === patientId)?.name || 'Unknown';
  };

  const getBloodUnit = (unitId: string) => {
    return mockBloodUnits.find(u => u.id === unitId);
  };

  return (
    <Card className="hover-lift" data-onboarding="match-suggestions">
      <CardHeader>
        <CardTitle className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Target className="h-5 w-5" />
            <span>Match Suggestions</span>
          </div>
          <Button variant="outline" size="sm" className="rounded-2xl">
            View All
          </Button>
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {recentSuggestions.length === 0 ? (
          <p className="text-muted-foreground text-center py-8">
            No match suggestions available
          </p>
        ) : (
          recentSuggestions.map(suggestion => {
            const patient = mockPatients.find(p => p.id === suggestion.patientId);
            const bloodUnit = getBloodUnit(suggestion.bloodUnitIds[0]);
            
            if (!patient || !bloodUnit) return null;

            return (
              <div key={suggestion.id} className="p-4 border rounded-2xl hover:bg-muted/50 transition-smooth">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center space-x-3">
                    <div className="p-2 bg-success/10 rounded-2xl">
                      <User className="h-4 w-4 text-success" />
                    </div>
                    <div>
                      <p className="font-medium">{patient.name}</p>
                      <p className="text-sm text-muted-foreground">{patient.medicalRecord}</p>
                    </div>
                  </div>
                  <Badge variant="urgency" className={urgencyColors[suggestion.urgency]}>
                    {suggestion.urgency}
                  </Badge>
                </div>

                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center space-x-2">
                    <Badge variant="bloodType" className={bloodTypeColors[patient.bloodType]}>
                      {patient.bloodType}
                    </Badge>
                    <ChevronRight className="h-4 w-4 text-muted-foreground" />
                    <div className="flex items-center space-x-2">
                      <Droplet className="h-4 w-4 text-primary" />
                      <Badge variant="bloodType" className={bloodTypeColors[bloodUnit.bloodType]}>
                        {bloodUnit.bloodType}
                      </Badge>
                    </div>
                  </div>
                  <div className="text-right">
                    <div className="text-lg font-bold text-success">
                      {suggestion.compatibilityScore}%
                    </div>
                    <div className="text-xs text-muted-foreground">
                      compatibility
                    </div>
                  </div>
                </div>

                {suggestion.notes && (
                  <p className="text-sm text-muted-foreground mb-3">
                    {suggestion.notes}
                  </p>
                )}

                <div className="flex justify-end space-x-2">
                  <Button variant="outline" size="sm" className="rounded-2xl">
                    View Details
                  </Button>
                  <Button size="sm" className="rounded-2xl">
                    Schedule Match
                  </Button>
                </div>
              </div>
            );
          })
        )}
      </CardContent>
    </Card>
  );
};