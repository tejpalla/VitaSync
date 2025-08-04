import React from 'react';
import { Activity, Users, Package, TrendingUp } from 'lucide-react';
import { StatsCard } from '@/components/dashboard/StatsCard';
import { InventorySummary } from '@/components/dashboard/InventorySummary';
import { UpcomingTransfusions } from '@/components/dashboard/UpcomingTransfusions';
import { MatchSuggestions } from '@/components/dashboard/MatchSuggestions';
import { OnboardingTooltip } from '@/components/onboarding/OnboardingTooltip';
import { mockBloodUnits, mockPatients, mockMatchSuggestions } from '@/data/mockData';

export const Dashboard: React.FC = () => {
  const availableUnits = mockBloodUnits.filter(unit => unit.status === 'AVAILABLE').length;
  const activePatients = mockPatients.filter(patient => patient.status === 'ACTIVE' || patient.status === 'SCHEDULED').length;
  const expiringUnits = mockBloodUnits.filter(unit => {
    const daysUntilExpiry = Math.ceil(
      (unit.expiryDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24)
    );
    return daysUntilExpiry <= 7 && unit.status === 'AVAILABLE';
  }).length;

  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-foreground">Dashboard</h1>
        <p className="text-muted-foreground mt-2">
          Monitor blood inventory, patient schedules, and matching opportunities
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatsCard
          title="Available Units"
          value={availableUnits}
          icon={Package}
          trend={{ value: 12, isPositive: true }}
        />
        <StatsCard
          title="Active Patients"
          value={activePatients}
          icon={Users}
          trend={{ value: 5, isPositive: true }}
        />
        <StatsCard
          title="Expiring Soon"
          value={expiringUnits}
          icon={Activity}
          trend={{ value: 8, isPositive: false }}
        />
        <StatsCard
          title="Match Suggestions"
          value={mockMatchSuggestions.length}
          icon={TrendingUp}
          trend={{ value: 15, isPositive: true }}
        />
      </div>

      {/* Inventory Summary */}
      <InventorySummary />

      {/* Main Content Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <UpcomingTransfusions />
        <MatchSuggestions />
      </div>

      {/* Onboarding */}
      <OnboardingTooltip />
    </div>
  );
};