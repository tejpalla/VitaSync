import React from 'react';
import { Calendar, User, Clock } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { mockPatients, bloodTypeColors, priorityColors } from '@/data/mockData';

export const UpcomingTransfusions: React.FC = () => {
  const upcomingTransfusions = mockPatients
    .filter(patient => patient.status === 'SCHEDULED')
    .sort((a, b) => a.nextTransfusion.getTime() - b.nextTransfusion.getTime())
    .slice(0, 5);

  const formatDateTime = (date: Date) => {
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date);
  };

  const getTimeUntil = (date: Date) => {
    const now = new Date();
    const diffMs = date.getTime() - now.getTime();
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffDays = Math.floor(diffHours / 24);
    
    if (diffDays > 0) {
      return `${diffDays} days`;
    } else if (diffHours > 0) {
      return `${diffHours} hours`;
    } else {
      return 'Soon';
    }
  };

  return (
    <Card className="hover-lift" data-onboarding="upcoming-transfusions">
      <CardHeader>
        <CardTitle className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Calendar className="h-5 w-5" />
            <span>Upcoming Transfusions</span>
          </div>
          <Button variant="outline" size="sm" className="rounded-2xl">
            View All
          </Button>
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {upcomingTransfusions.length === 0 ? (
          <p className="text-muted-foreground text-center py-8">
            No scheduled transfusions
          </p>
        ) : (
          upcomingTransfusions.map(patient => (
            <div key={patient.id} className="flex items-center justify-between p-4 border rounded-2xl hover:bg-muted/50 transition-smooth">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-primary/10 rounded-2xl">
                  <User className="h-4 w-4 text-primary" />
                </div>
                <div>
                  <p className="font-medium">{patient.name}</p>
                  <p className="text-sm text-muted-foreground">{patient.medicalRecord}</p>
                </div>
              </div>
              <div className="text-right space-y-1">
                <div className="flex items-center space-x-2">
                  <Badge variant="bloodType" className={bloodTypeColors[patient.bloodType]}>
                    {patient.bloodType}
                  </Badge>
                  <Badge variant="priority" className={priorityColors[patient.priority]}>
                    {patient.priority}
                  </Badge>
                </div>
                <div className="flex items-center space-x-1 text-sm text-muted-foreground">
                  <Clock className="h-3 w-3" />
                  <span>{formatDateTime(patient.nextTransfusion)}</span>
                </div>
                <p className="text-xs font-medium text-primary">
                  in {getTimeUntil(patient.nextTransfusion)}
                </p>
              </div>
            </div>
          ))
        )}
      </CardContent>
    </Card>
  );
};