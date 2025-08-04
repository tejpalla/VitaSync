import React, { useState, useEffect } from 'react';
import { X, ArrowRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';

interface OnboardingStep {
  id: string;
  title: string;
  description: string;
  target: string;
  position: 'top' | 'bottom' | 'left' | 'right';
}

const onboardingSteps: OnboardingStep[] = [
  {
    id: 'inventory-cards',
    title: 'Blood Inventory Overview',
    description: 'Monitor your blood type inventory levels and expiring units at a glance.',
    target: '[data-onboarding="inventory-summary"]',
    position: 'bottom'
  },
  {
    id: 'upcoming-transfusions',
    title: 'Upcoming Transfusions',
    description: 'Keep track of scheduled patient transfusions and their priorities.',
    target: '[data-onboarding="upcoming-transfusions"]',
    position: 'left'
  },
  {
    id: 'match-suggestions',
    title: 'Smart Matching',
    description: 'AI-powered suggestions help you find optimal patient-blood unit matches.',
    target: '[data-onboarding="match-suggestions"]',
    position: 'left'
  }
];

export const OnboardingTooltip: React.FC = () => {
  const [currentStep, setCurrentStep] = useState(0);
  const [isVisible, setIsVisible] = useState(false);
  const [hasSeenOnboarding, setHasSeenOnboarding] = useState(false);

  useEffect(() => {
    const seen = localStorage.getItem('vitasync-onboarding-seen');
    if (!seen) {
      // Delay showing onboarding to let the page load
      const timer = setTimeout(() => {
        setIsVisible(true);
      }, 1500);
      return () => clearTimeout(timer);
    } else {
      setHasSeenOnboarding(true);
    }
  }, []);

  const handleNext = () => {
    if (currentStep < onboardingSteps.length - 1) {
      setCurrentStep(currentStep + 1);
    } else {
      handleComplete();
    }
  };

  const handleComplete = () => {
    setIsVisible(false);
    setHasSeenOnboarding(true);
    localStorage.setItem('vitasync-onboarding-seen', 'true');
  };

  const handleSkip = () => {
    handleComplete();
  };

  if (!isVisible || hasSeenOnboarding) {
    return null;
  }

  const step = onboardingSteps[currentStep];

  return (
    <>
      {/* Overlay */}
      <div className="fixed inset-0 bg-black/50 z-40" />
      
      {/* Tooltip */}
      <div 
        className="fixed z-50 max-w-sm"
        style={{
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)'
        }}
      >
        <Card className="border-primary shadow-2xl">
          <CardContent className="p-6">
            <div className="flex items-start justify-between mb-4">
              <div className="flex-1">
                <h3 className="font-semibold text-lg mb-2">{step.title}</h3>
                <p className="text-muted-foreground text-sm">{step.description}</p>
              </div>
              <Button
                variant="ghost"
                size="sm"
                onClick={handleSkip}
                className="ml-2 p-1 h-auto"
              >
                <X className="h-4 w-4" />
              </Button>
            </div>
            
            <div className="flex items-center justify-between">
              <div className="flex space-x-1">
                {onboardingSteps.map((_, index) => (
                  <div
                    key={index}
                    className={`w-2 h-2 rounded-full ${
                      index === currentStep ? 'bg-primary' : 'bg-muted'
                    }`}
                  />
                ))}
              </div>
              
              <div className="flex space-x-2">
                <Button variant="outline" size="sm" onClick={handleSkip}>
                  Skip
                </Button>
                <Button size="sm" onClick={handleNext}>
                  {currentStep === onboardingSteps.length - 1 ? 'Get Started' : 'Next'}
                  <ArrowRight className="h-4 w-4 ml-1" />
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </>
  );
};