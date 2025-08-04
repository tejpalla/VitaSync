import React, { useState } from 'react';
import { Search, Plus, Filter, User, Calendar, Phone } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { mockPatients, bloodTypeColors, priorityColors } from '@/data/mockData';

export const Patients: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedPatient, setSelectedPatient] = useState<string | null>(null);

  const filteredPatients = mockPatients.filter(patient =>
    patient.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    patient.medicalRecord.toLowerCase().includes(searchQuery.toLowerCase()) ||
    patient.bloodType.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const formatDate = (date: Date) => {
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Patient Management</h1>
          <p className="text-muted-foreground mt-2">
            Manage patient records and transfusion schedules
          </p>
        </div>
        <Button className="rounded-2xl">
          <Plus className="h-4 w-4 mr-2" />
          Add Patient
        </Button>
      </div>

      {/* Search and Filters */}
      <div className="flex items-center space-x-4">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search patients..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-10 rounded-2xl"
          />
        </div>
        <Button variant="outline" className="rounded-2xl">
          <Filter className="h-4 w-4 mr-2" />
          Filter
        </Button>
      </div>

      {/* Patients Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredPatients.map(patient => (
          <Card key={patient.id} className="hover-lift cursor-pointer" onClick={() => setSelectedPatient(patient.id)}>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="flex items-center space-x-2">
                  <div className="p-2 bg-primary/10 rounded-2xl">
                    <User className="h-4 w-4 text-primary" />
                  </div>
                  <span className="text-lg">{patient.name}</span>
                </CardTitle>
                <Badge variant="priority" className={priorityColors[patient.priority]}>
                  {patient.priority}
                </Badge>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Blood Type</span>
                <Badge variant="bloodType" className={bloodTypeColors[patient.bloodType]}>
                  {patient.bloodType}
                </Badge>
              </div>
              
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Medical Record</span>
                <span className="text-sm font-medium">{patient.medicalRecord}</span>
              </div>

              <div className="space-y-2">
                <div className="flex items-center space-x-2 text-sm">
                  <Calendar className="h-4 w-4 text-muted-foreground" />
                  <span className="text-muted-foreground">Next Transfusion:</span>
                </div>
                <p className="text-sm font-medium">{formatDate(patient.nextTransfusion)}</p>
              </div>

              <div className="flex items-center space-x-2 text-sm">
                <Phone className="h-4 w-4 text-muted-foreground" />
                <span className="text-muted-foreground">{patient.phone}</span>
              </div>

              <div className="pt-2">
                <Badge 
                  variant="outline" 
                  className={`${
                    patient.status === 'SCHEDULED' ? 'border-primary text-primary' :
                    patient.status === 'ACTIVE' ? 'border-success text-success' :
                    'border-muted-foreground text-muted-foreground'
                  }`}
                >
                  {patient.status}
                </Badge>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredPatients.length === 0 && (
        <Card>
          <CardContent className="text-center py-12">
            <User className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-medium text-foreground mb-2">No patients found</h3>
            <p className="text-muted-foreground">
              {searchQuery ? 'Try adjusting your search criteria' : 'Get started by adding your first patient'}
            </p>
          </CardContent>
        </Card>
      )}
    </div>
  );
};