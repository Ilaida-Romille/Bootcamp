// auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { Token } from '@angular/compiler';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const sessionRaw = localStorage.getItem('eventhub_session');
    const token = localStorage.getItem('token'); // Ensure key matches your storage key

    if (sessionRaw) {
        try {
            const session = JSON.parse(sessionRaw);
            const token = session?.token;

            if (token) {
                const cloned = req.clone({
                    setHeaders: {
                        Authorization: `Bearer ${token}`
                    }
                });
                return next(cloned);
            }
        } catch (error) {
            console.error('Error parsing eventhub_session from localStorage:', error);
        };
    }
    return next(req);
};