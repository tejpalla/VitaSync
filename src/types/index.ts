export type BloodType = 'O+' | 'O-' | 'A+' | 'A-' | 'B+' | 'B-' | 'AB+' | 'AB-';

export type UserRole = 'ADMIN' | 'COORDINATOR' | 'PATIENT';

export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
  avatar?: string;
}

export interface Patient {
  id: string;
  name: string;
  bloodType: BloodType;
  email: string;
  phone: string;
  nextTransfusion: Date;
  lastTransfusion?: Date;
  medicalRecord: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  status: 'ACTIVE' | 'INACTIVE' | 'SCHEDULED';
  transfusionHistory: TransfusionRecord[];
}

export interface BloodUnit {
  id: string;
  bloodType: BloodType;
  volume: number; // in mL
  collectionDate: Date;
  expiryDate: Date;
  donorId: string;
  status: 'AVAILABLE' | 'RESERVED' | 'EXPIRED' | 'USED';
  location: string;
  testResults: {
    hiv: boolean;
    hepatitisB: boolean;
    hepatitisC: boolean;
    syphilis: boolean;
  };
}

export interface TransfusionRecord {
  id: string;
  patientId: string;
  bloodUnitId: string;
  date: Date;
  volume: number;
  reactions?: string;
  status: 'SCHEDULED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
}

export interface MatchSuggestion {
  id: string;
  patientId: string;
  bloodUnitIds: string[];
  compatibilityScore: number;
  urgency: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  createdAt: Date;
  notes?: string;
}

export interface DashboardStats {
  totalUnits: number;
  expiringUnits: number;
  lowStockTypes: BloodType[];
  upcomingTransfusions: number;
  matchSuggestions: number;
}

export interface Notification {
  id: string;
  type: 'INFO' | 'WARNING' | 'ERROR' | 'SUCCESS';
  title: string;
  message: string;
  timestamp: Date;
  read: boolean;
}