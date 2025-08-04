# VitaSync – Proactive Blood Logistics

**Live Demo:** [vitasync.surge.sh](https://vitasync.surge.sh)

## Why We Built VitaSync
Every week, thousands of Thalassemia patients wait anxiously for their blood transfusions.  
Sometimes blood units expire unused. Other times, the right blood type simply isn’t available when needed.  
We thought — *what if technology could fix that?*

## What is VitaSync?
VitaSync is a **simple, smart, and proactive** web app for blood banks and hospitals.  
It helps you **track, predict, and act** before shortages or emergencies happen.

**In plain English:**  
- Know exactly **what blood types you have** and **when they expire**  
- Get **alerts before units go to waste**  
- Automatically **schedule transfusions** for patients  
- Instantly **match the right donor to the right patient**  
- **Predict shortages** so you can plan ahead  

## 🚀 Quick Start

### Prerequisites
- Node.js 18+ and npm
- Modern web browser

### Installation
```bash
# Clone the repository
git clone <repository-url>
cd vitasync

# Install dependencies
npm install

# Start development server
npm run dev
```

### Building for Production
```bash
# Build optimized bundle
npm run build

# Preview production build
npm run preview
```

## 📁 Project Structure

```
src/
├── components/
│   ├── dashboard/          # Dashboard-specific components
│   ├── layout/            # Layout components (TopNav, MainLayout)
│   ├── onboarding/        # User onboarding components
│   └── ui/                # Reusable UI components (shadcn/ui)
├── contexts/
│   └── ThemeContext.tsx   # Theme management
├── data/
│   └── mockData.ts        # Sample data for development
├── pages/
│   ├── Dashboard.tsx      # Main dashboard page
│   ├── Patients.tsx       # Patient management
│   ├── Inventory.tsx      # Blood inventory management
│   └── Matching.tsx       # Patient-blood matching
├── types/
│   └── index.ts           # TypeScript type definitions
└── lib/
    └── utils.ts           # Utility functions
```

## 🧩 Key Components

### Dashboard
- **StatsCard**: KPI cards with trend indicators
- **InventorySummary**: Blood type inventory with expiry alerts
- **UpcomingTransfusions**: Scheduled patient transfusions
- **MatchSuggestions**: AI-powered compatibility suggestions

### Patient Management
- **Patient Cards**: Comprehensive patient information display
- **Search & Filter**: Real-time patient search functionality
- **Priority Badges**: Visual priority indication system

### Inventory Management
- **Blood Unit Cards**: Detailed unit information with test results
- **Expiry Monitoring**: Color-coded expiry warnings
- **Location Tracking**: Storage location management

### Matching System
- **Compatibility Scoring**: Algorithm-based match percentages
- **Multi-step Selection**: Patient → Compatible Units → Match Summary
- **Real-time Updates**: Dynamic compatibility calculations

### Adding New Features
1. Define types in `src/types/index.ts`
2. Add mock data in `src/data/mockData.ts`
3. Create components in appropriate directories
4. Add routing in `src/App.tsx`

## 🔌 API Integration

The application is designed for easy backend integration:

### Data Layer
Replace mock data imports with API calls:
```typescript
// Current: import { mockPatients } from '@/data/mockData';
// Replace with: import { fetchPatients } from '@/services/api';
```

### Recommended API Structure
```typescript
// GET /api/patients
// GET /api/blood-units
// GET /api/match-suggestions
// POST /api/transfusions
// PUT /api/patients/:id
```
## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🩺 Healthcare Compliance

This application is designed as a development template. For production use in healthcare:
- Implement proper data encryption
- Ensure HIPAA compliance
- Add audit trails
- Implement proper authentication
- Consider FDA regulations for medical software

---

**VitaSync** - Making blood logistics more efficient, one unit at a time. 🩸❤️