import React, { useState } from 'react';
import { Target, Users, Droplet, ArrowRight, Star } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { mockPatients, mockBloodUnits, mockMatchSuggestions, bloodTypeColors, urgencyColors, priorityColors } from '@/data/mockData';

export const Matching: React.FC = () => {
  const [selectedPatient, setSelectedPatient] = useState<string | null>(null);

  const getCompatibleUnits = (patientBloodType: string) => {
    // Simplified compatibility logic
    const compatibility: Record<string, string[]> = {
      'O-': ['O-'],
      'O+': ['O-', 'O+'],
      'A-': ['O-', 'A-'],
      'A+': ['O-', 'O+', 'A-', 'A+'],
      'B-': ['O-', 'B-'],
      'B+': ['O-', 'O+', 'B-', 'B+'],
      'AB-': ['O-', 'A-', 'B-', 'AB-'],
      'AB+': ['O-', 'O+', 'A-', 'A+', 'B-', 'B+', 'AB-', 'AB+'],
    };

    return mockBloodUnits.filter(unit => 
      unit.status === 'AVAILABLE' && 
      compatibility[patientBloodType]?.includes(unit.bloodType)
    );
  };

  const calculateCompatibilityScore = (patientBloodType: string, unitBloodType: string) => {
    if (patientBloodType === unitBloodType) return 100;
    if (unitBloodType === 'O-') return 95; // Universal donor
    if (patientBloodType === 'AB+') return 90; // Universal recipient
    return 85; // Other compatible combinations
  };

  const formatDate = (date: Date) => {
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date);
  };

  const selectedPatientData = selectedPatient 
    ? mockPatients.find(p => p.id === selectedPatient)
    : null;

  const compatibleUnits = selectedPatientData 
    ? getCompatibleUnits(selectedPatientData.bloodType)
    : [];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-foreground">Patient-Blood Matching</h1>
        <p className="text-muted-foreground mt-2">
          Find optimal blood unit matches for patients
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Patient Selection */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Users className="h-5 w-5" />
              <span>Select Patient</span>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            {mockPatients
              .filter(p => p.status === 'ACTIVE' || p.status === 'SCHEDULED')
              .map(patient => (
              <div
                key={patient.id}
                className={`p-3 border rounded-2xl cursor-pointer transition-smooth hover:bg-muted/50 ${
                  selectedPatient === patient.id ? 'border-primary bg-primary/5' : ''
                }`}
                onClick={() => setSelectedPatient(patient.id)}
              >
                <div className="flex items-center justify-between mb-2">
                  <p className="font-medium">{patient.name}</p>
                  <Badge variant="priority" className={priorityColors[patient.priority]}>
                    {patient.priority}
                  </Badge>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <Badge variant="bloodType" className={bloodTypeColors[patient.bloodType]}>
                    {patient.bloodType}
                  </Badge>
                  <span className="text-muted-foreground">
                    Next: {formatDate(patient.nextTransfusion)}
                  </span>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>

        {/* Compatible Units */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Droplet className="h-5 w-5" />
              <span>Compatible Units</span>
              {selectedPatientData && (
                <Badge variant="outline">
                  {compatibleUnits.length} available
                </Badge>
              )}
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            {!selectedPatientData ? (
              <p className="text-muted-foreground text-center py-8">
                Select a patient to view compatible blood units
              </p>
            ) : compatibleUnits.length === 0 ? (
              <p className="text-muted-foreground text-center py-8">
                No compatible units available
              </p>
            ) : (
              compatibleUnits.map(unit => {
                const score = calculateCompatibilityScore(selectedPatientData.bloodType, unit.bloodType);
                const daysLeft = Math.ceil((unit.expiryDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
                
                return (
                  <div key={unit.id} className="p-3 border rounded-2xl hover:bg-muted/50 transition-smooth">
                    <div className="flex items-center justify-between mb-2">
                      <div className="flex items-center space-x-2">
                        <Badge variant="bloodType" className={bloodTypeColors[unit.bloodType]}>
                          {unit.bloodType}
                        </Badge>
                        <span className="font-medium text-sm">{unit.id}</span>
                      </div>
                      <div className="flex items-center space-x-1">
                        <Star className="h-3 w-3 text-warning fill-current" />
                        <span className="text-sm font-medium">{score}%</span>
                      </div>
                    </div>
                    <div className="text-xs text-muted-foreground space-y-1">
                      <div>Volume: {unit.volume} mL</div>
                      <div>Expires: {daysLeft} days</div>
                      <div>Location: {unit.location}</div>
                    </div>
                    <Button size="sm" className="w-full mt-3 rounded-2xl">
                      Select Unit
                    </Button>
                  </div>
                );
              })
            )}
          </CardContent>
        </Card>

        {/* Match Summary */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Target className="h-5 w-5" />
              <span>Match Summary</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            {!selectedPatientData ? (
              <p className="text-muted-foreground text-center py-8">
                Select a patient and compatible unit to create a match
              </p>
            ) : (
              <div className="space-y-4">
                <div className="p-4 bg-muted/50 rounded-2xl">
                  <h4 className="font-medium mb-2">Patient Details</h4>
                  <div className="space-y-2 text-sm">
                    <div className="flex justify-between">
                      <span>Name:</span>
                      <span className="font-medium">{selectedPatientData.name}</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Blood Type:</span>
                      <Badge variant="bloodType" className={bloodTypeColors[selectedPatientData.bloodType]}>
                        {selectedPatientData.bloodType}
                      </Badge>
                    </div>
                    <div className="flex justify-between">
                      <span>Priority:</span>
                      <Badge variant="priority" className={priorityColors[selectedPatientData.priority]}>
                        {selectedPatientData.priority}
                      </Badge>
                    </div>
                    <div className="flex justify-between">
                      <span>Next Transfusion:</span>
                      <span>{formatDate(selectedPatientData.nextTransfusion)}</span>
                    </div>
                  </div>
                </div>

                <div className="text-center">
                  <ArrowRight className="h-6 w-6 text-muted-foreground mx-auto mb-4" />
                  <p className="text-sm text-muted-foreground mb-4">
                    Select a compatible unit to schedule the transfusion
                  </p>
                  <Button disabled className="w-full rounded-2xl">
                    Schedule Transfusion
                  </Button>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </div>

      {/* Recent Suggestions */}
      <Card>
        <CardHeader>
          <CardTitle>Recent Match Suggestions</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {mockMatchSuggestions.map(suggestion => {
              const patient = mockPatients.find(p => p.id === suggestion.patientId);
              const unit = mockBloodUnits.find(u => u.id === suggestion.bloodUnitIds[0]);
              
              if (!patient || !unit) return null;

              return (
                <div key={suggestion.id} className="p-4 border rounded-2xl hover:bg-muted/50 transition-smooth">
                  <div className="flex items-center justify-between mb-3">
                    <div>
                      <p className="font-medium">{patient.name}</p>
                      <p className="text-sm text-muted-foreground">{patient.medicalRecord}</p>
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
                      <ArrowRight className="h-3 w-3 text-muted-foreground" />
                      <Badge variant="bloodType" className={bloodTypeColors[unit.bloodType]}>
                        {unit.bloodType}
                      </Badge>
                    </div>
                    <div className="text-right">
                      <div className="text-sm font-bold text-success">
                        {suggestion.compatibilityScore}%
                      </div>
                    </div>
                  </div>
                  <div className="flex space-x-2">
                    <Button variant="outline" size="sm" className="flex-1 rounded-2xl">
                      Review
                    </Button>
                    <Button size="sm" className="flex-1 rounded-2xl">
                      Schedule
                    </Button>
                  </div>
                </div>
              );
            })}
          </div>
        </CardContent>
      </Card>
    </div>
  );
};