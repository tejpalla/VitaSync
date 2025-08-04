import { Patient, BloodUnit, TransfusionRecord, MatchSuggestion, User, BloodType } from '@/types';

export const mockUsers: User[] = [
  {
    id: '1',
    name: 'Dr. Sarah Chen',
    email: 'sarah.chen@vitasync.com',
    role: 'ADMIN',
    avatar: 'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=150&h=150&fit=crop&crop=face'
  },
  {
    id: '2',
    name: 'Mike Rodriguez',
    email: 'mike.rodriguez@vitasync.com',
    role: 'COORDINATOR',
    avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face'
  }
];

export const mockPatients: Patient[] = [
  {
    id: 'p1',
    name: 'John Anderson',
    bloodType: 'O+',
    email: 'john.anderson@email.com',
    phone: '+1-555-0123',
    nextTransfusion: new Date('2024-08-15T09:00:00'),
    lastTransfusion: new Date('2024-07-15T14:30:00'),
    medicalRecord: 'MR-001',
    priority: 'HIGH',
    status: 'SCHEDULED',
    transfusionHistory: []
  },
  {
    id: 'p2',
    name: 'Emma Wilson',
    bloodType: 'A-',
    email: 'emma.wilson@email.com',
    phone: '+1-555-0124',
    nextTransfusion: new Date('2024-08-20T11:00:00'),
    lastTransfusion: new Date('2024-06-20T10:15:00'),
    medicalRecord: 'MR-002',
    priority: 'MEDIUM',
    status: 'ACTIVE',
    transfusionHistory: []
  },
  {
    id: 'p3',
    name: 'Robert Chen',
    bloodType: 'B+',
    email: 'robert.chen@email.com',
    phone: '+1-555-0125',
    nextTransfusion: new Date('2024-08-18T15:30:00'),
    lastTransfusion: new Date('2024-07-18T16:00:00'),
    medicalRecord: 'MR-003',
    priority: 'CRITICAL',
    status: 'SCHEDULED',
    transfusionHistory: []
  },
  {
    id: 'p4',
    name: 'Lisa Martinez',
    bloodType: 'AB+',
    email: 'lisa.martinez@email.com',
    phone: '+1-555-0126',
    nextTransfusion: new Date('2024-08-25T13:00:00'),
    lastTransfusion: new Date('2024-07-25T14:45:00'),
    medicalRecord: 'MR-004',
    priority: 'LOW',
    status: 'ACTIVE',
    transfusionHistory: []
  }
];

export const mockBloodUnits: BloodUnit[] = [
  {
    id: 'bu1',
    bloodType: 'O+',
    volume: 450,
    collectionDate: new Date('2024-07-20T10:00:00'),
    expiryDate: new Date('2024-08-20T10:00:00'),
    donorId: 'd1',
    status: 'AVAILABLE',
    location: 'Storage Unit A-1',
    testResults: {
      hiv: false,
      hepatitisB: false,
      hepatitisC: false,
      syphilis: false
    }
  },
  {
    id: 'bu2',
    bloodType: 'A-',
    volume: 450,
    collectionDate: new Date('2024-07-25T14:30:00'),
    expiryDate: new Date('2024-08-25T14:30:00'),
    donorId: 'd2',
    status: 'AVAILABLE',
    location: 'Storage Unit B-2',
    testResults: {
      hiv: false,
      hepatitisB: false,
      hepatitisC: false,
      syphilis: false
    }
  },
  {
    id: 'bu3',
    bloodType: 'B+',
    volume: 450,
    collectionDate: new Date('2024-07-15T11:15:00'),
    expiryDate: new Date('2024-08-15T11:15:00'),
    donorId: 'd3',
    status: 'AVAILABLE',
    location: 'Storage Unit C-1',
    testResults: {
      hiv: false,
      hepatitisB: false,
      hepatitisC: false,
      syphilis: false
    }
  },
  {
    id: 'bu4',
    bloodType: 'O-',
    volume: 450,
    collectionDate: new Date('2024-07-10T09:00:00'),
    expiryDate: new Date('2024-08-10T09:00:00'),
    donorId: 'd4',
    status: 'AVAILABLE',
    location: 'Storage Unit A-2',
    testResults: {
      hiv: false,
      hepatitisB: false,
      hepatitisC: false,
      syphilis: false
    }
  },
  {
    id: 'bu5',
    bloodType: 'AB+',
    volume: 450,
    collectionDate: new Date('2024-07-30T16:00:00'),
    expiryDate: new Date('2024-08-30T16:00:00'),
    donorId: 'd5',
    status: 'AVAILABLE',
    location: 'Storage Unit D-1',
    testResults: {
      hiv: false,
      hepatitisB: false,
      hepatitisC: false,
      syphilis: false
    }
  }
];

export const mockMatchSuggestions: MatchSuggestion[] = [
  {
    id: 'ms1',
    patientId: 'p1',
    bloodUnitIds: ['bu1'],
    compatibilityScore: 98,
    urgency: 'HIGH',
    createdAt: new Date('2024-08-04T10:00:00'),
    notes: 'Perfect blood type match, high compatibility'
  },
  {
    id: 'ms2',
    patientId: 'p2',
    bloodUnitIds: ['bu2'],
    compatibilityScore: 95,
    urgency: 'MEDIUM',
    createdAt: new Date('2024-08-04T10:15:00'),
    notes: 'Exact blood type match available'
  },
  {
    id: 'ms3',
    patientId: 'p3',
    bloodUnitIds: ['bu3'],
    compatibilityScore: 92,
    urgency: 'CRITICAL',
    createdAt: new Date('2024-08-04T10:30:00'),
    notes: 'Compatible unit found, urgent scheduling recommended'
  }
];

export const bloodTypeColors: Record<BloodType, string> = {
  'O+': 'bg-red-500 text-white',
  'O-': 'bg-red-700 text-white',
  'A+': 'bg-blue-500 text-white',
  'A-': 'bg-blue-700 text-white',
  'B+': 'bg-purple-500 text-white',
  'B-': 'bg-purple-700 text-white',
  'AB+': 'bg-green-500 text-white',
  'AB-': 'bg-green-700 text-white'
};

export const priorityColors = {
  LOW: 'bg-green-100 text-green-800 border-green-200',
  MEDIUM: 'bg-yellow-100 text-yellow-800 border-yellow-200',
  HIGH: 'bg-orange-100 text-orange-800 border-orange-200',
  CRITICAL: 'bg-red-100 text-red-800 border-red-200'
};

export const urgencyColors = {
  LOW: 'bg-green-100 text-green-800',
  MEDIUM: 'bg-yellow-100 text-yellow-800',
  HIGH: 'bg-orange-100 text-orange-800',
  CRITICAL: 'bg-red-100 text-red-800'
};