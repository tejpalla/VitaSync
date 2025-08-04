import { useLocation, Link } from "react-router-dom";
import { useEffect } from "react";
import { Home, AlertTriangle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";

const NotFound = () => {
  const location = useLocation();

  useEffect(() => {
    console.error(
      "404 Error: User attempted to access non-existent route:",
      location.pathname
    );
  }, [location.pathname]);

  return (
    <div className="min-h-[60vh] flex items-center justify-center">
      <Card className="max-w-md w-full text-center">
        <CardContent className="p-8">
          <div className="mb-6">
            <AlertTriangle className="h-16 w-16 text-muted-foreground mx-auto mb-4" />
            <h1 className="text-4xl font-bold text-foreground mb-2">404</h1>
            <p className="text-xl text-muted-foreground mb-4">Page not found</p>
            <p className="text-sm text-muted-foreground">
              The page you're looking for doesn't exist or has been moved.
            </p>
          </div>
          <Button asChild className="rounded-2xl">
            <Link to="/">
              <Home className="h-4 w-4 mr-2" />
              Return to Dashboard
            </Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  );
};

export default NotFound;
