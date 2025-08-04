import React, { useState } from 'react';
import { Search, Plus, Filter, Package, AlertTriangle, Clock, MapPin } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { mockBloodUnits, bloodTypeColors } from '@/data/mockData';

export const Inventory: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [filterType, setFilterType] = useState<'all' | 'expiring' | 'low-stock'>('all');

  const getDaysUntilExpiry = (expiryDate: Date) => {
    return Math.ceil((expiryDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
  };

  const filteredUnits = mockBloodUnits.filter(unit => {
    const matchesSearch = 
      unit.id.toLowerCase().includes(searchQuery.toLowerCase()) ||
      unit.bloodType.toLowerCase().includes(searchQuery.toLowerCase()) ||
      unit.location.toLowerCase().includes(searchQuery.toLowerCase());

    if (filterType === 'expiring') {
      return matchesSearch && getDaysUntilExpiry(unit.expiryDate) <= 7 && unit.status === 'AVAILABLE';
    }
    
    return matchesSearch && unit.status === 'AVAILABLE';
  });

  const formatDate = (date: Date) => {
    return new Intl.DateTimeFormat('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    }).format(date);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'AVAILABLE': return 'border-success text-success';
      case 'RESERVED': return 'border-warning text-warning';
      case 'EXPIRED': return 'border-destructive text-destructive';
      case 'USED': return 'border-muted text-muted-foreground';
      default: return 'border-muted text-muted-foreground';
    }
  };

  const getExpiryStatus = (daysLeft: number) => {
    if (daysLeft <= 3) return { color: 'text-destructive', icon: AlertTriangle };
    if (daysLeft <= 7) return { color: 'text-warning', icon: Clock };
    return { color: 'text-muted-foreground', icon: Clock };
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Inventory Management</h1>
          <p className="text-muted-foreground mt-2">
            Track blood units, expiry dates, and stock levels
          </p>
        </div>
        <Button className="rounded-2xl">
          <Plus className="h-4 w-4 mr-2" />
          Add Blood Unit
        </Button>
      </div>

      {/* Search and Filters */}
      <div className="flex items-center space-x-4">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search units..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-10 rounded-2xl"
          />
        </div>
        <div className="flex space-x-2">
          <Button 
            variant={filterType === 'all' ? 'default' : 'outline'} 
            onClick={() => setFilterType('all')}
            className="rounded-2xl"
          >
            All Units
          </Button>
          <Button 
            variant={filterType === 'expiring' ? 'default' : 'outline'} 
            onClick={() => setFilterType('expiring')}
            className="rounded-2xl"
          >
            Expiring Soon
          </Button>
        </div>
      </div>

      {/* Inventory Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredUnits.map(unit => {
          const daysLeft = getDaysUntilExpiry(unit.expiryDate);
          const expiryStatus = getExpiryStatus(daysLeft);
          const ExpiryIcon = expiryStatus.icon;

          return (
            <Card key={unit.id} className="hover-lift">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="flex items-center space-x-2">
                    <div className="p-2 bg-primary/10 rounded-2xl">
                      <Package className="h-4 w-4 text-primary" />
                    </div>
                    <span className="text-lg">{unit.id}</span>
                  </CardTitle>
                  <Badge variant="bloodType" className={bloodTypeColors[unit.bloodType]}>
                    {unit.bloodType}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-muted-foreground">Volume</span>
                  <span className="text-sm font-medium">{unit.volume} mL</span>
                </div>
                
                <div className="flex items-center justify-between">
                  <span className="text-sm text-muted-foreground">Collection Date</span>
                  <span className="text-sm font-medium">{formatDate(unit.collectionDate)}</span>
                </div>

                <div className="flex items-center justify-between">
                  <span className="text-sm text-muted-foreground">Expiry Date</span>
                  <div className="flex items-center space-x-2">
                    <span className="text-sm font-medium">{formatDate(unit.expiryDate)}</span>
                    <ExpiryIcon className={`h-4 w-4 ${expiryStatus.color}`} />
                  </div>
                </div>

                <div className="flex items-center justify-between">
                  <span className="text-sm text-muted-foreground">Days Left</span>
                  <span className={`text-sm font-medium ${expiryStatus.color}`}>
                    {daysLeft > 0 ? `${daysLeft} days` : 'Expired'}
                  </span>
                </div>

                <div className="flex items-center space-x-2 text-sm">
                  <MapPin className="h-4 w-4 text-muted-foreground" />
                  <span className="text-muted-foreground">{unit.location}</span>
                </div>

                <div className="pt-2">
                  <Badge variant="outline" className={getStatusColor(unit.status)}>
                    {unit.status}
                  </Badge>
                </div>

                {/* Test Results */}
                <div className="border-t pt-3">
                  <p className="text-xs text-muted-foreground mb-2">Test Results</p>
                  <div className="grid grid-cols-2 gap-1 text-xs">
                    <div className="flex justify-between">
                      <span>HIV:</span>
                      <span className="text-success">✓ Clear</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Hep B:</span>
                      <span className="text-success">✓ Clear</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Hep C:</span>
                      <span className="text-success">✓ Clear</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Syphilis:</span>
                      <span className="text-success">✓ Clear</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          );
        })}
      </div>

      {filteredUnits.length === 0 && (
        <Card>
          <CardContent className="text-center py-12">
            <Package className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-medium text-foreground mb-2">No blood units found</h3>
            <p className="text-muted-foreground">
              {searchQuery ? 'Try adjusting your search criteria' : 'Get started by adding your first blood unit'}
            </p>
          </CardContent>
        </Card>
      )}
    </div>
  );
};