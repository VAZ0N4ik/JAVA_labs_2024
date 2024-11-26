import React from 'react';

const Alert = ({ className, children, ...props }) => (
    <div
        role="alert"
        className={`relative w-full rounded-lg border px-4 py-3 text-sm bg-white ${className}`}
        {...props}
    >
        {children}
    </div>
);

const AlertTitle = ({ className, children, ...props }) => (
    <h5
        className={`mb-1 font-medium leading-none tracking-tight ${className}`}
        {...props}
    >
        {children}
    </h5>
);

const AlertDescription = ({ className, children, ...props }) => (
    <div
        className={`text-sm ${className}`}
        {...props}
    >
        {children}
    </div>
);

export { Alert, AlertTitle, AlertDescription };