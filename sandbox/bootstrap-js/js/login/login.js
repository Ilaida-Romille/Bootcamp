// main.js

// --- Trigger Transition Animation on Get Started Button click ---
const getStartedBtn = document.getElementById('get-started-btn');
const contentRow = document.getElementById('content-row');
const heroSection = document.getElementById('hero-section');

if (getStartedBtn) {
    getStartedBtn.addEventListener('click', function() {
        contentRow.classList.add('split-active');
        
        if (window.innerWidth >= 992) {
            heroSection.style.setProperty('text-align', 'left', 'important');
        }

        getStartedBtn.style.opacity = '0';
        getStartedBtn.style.pointerEvents = 'none';
    });
}

// --- Login Validation & Routing Handler ---
function handleLoginSubmit(event) {
    if (event && event.preventDefault) event.preventDefault(); 

    const emailInput = document.getElementById('login-email');
    const passwordInput = document.getElementById('login-password');
    const emailErrorEl = document.getElementById('email-error');
    const passwordErrorEl = document.getElementById('password-error');
    
    const email = emailInput.value.trim().toLowerCase(); 
    const password = passwordInput.value;

    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    let isValid = true;

    // 1. Core Structural Email Constraints using the validator function
    if (!isLikelyValidEmail(email)) { 
        emailInput.classList.add('user-invalid');
        if (emailErrorEl) {
            emailErrorEl.style.maxHeight = "2.5rem";
            emailErrorEl.style.opacity = "1";
        }
        isValid = false;
    } else {
        emailInput.classList.remove('user-invalid');
        if (emailErrorEl) {
            emailErrorEl.style.maxHeight = "0";
            emailErrorEl.style.opacity = "0";
        }
    }

    // 2. Dynamic Password Validation & Error Generation
    let passwordErrors = [];

    if (password.length < 8) passwordErrors.push("be at least 8 characters");
    if (!hasUppercase) passwordErrors.push("contain an uppercase letter");
    if (!hasLowercase) passwordErrors.push("contain a lowercase letter");
    if (!hasNumber) passwordErrors.push("contain a number");
    if (!hasSpecial) passwordErrors.push("contain a special character (e.g., !@#$)");

    if (passwordErrors.length > 0) {
        passwordInput.classList.add('user-invalid');
        
        let finalMessage = "Password must ";
        if (passwordErrors.length === 1) {
            finalMessage += passwordErrors[0] + '.';
        } else if (passwordErrors.length === 2) {
            finalMessage += passwordErrors.join(" and ") + '.';
        } else {
            finalMessage += passwordErrors.slice(0, -1).join(", ") + ", and " + passwordErrors[passwordErrors.length - 1] + '.';
        }

        if (passwordErrorEl) {
            passwordErrorEl.textContent = finalMessage;
            passwordErrorEl.style.maxHeight = "4rem";
            passwordErrorEl.style.opacity = "1";
        }
        isValid = false;
    } else {
        passwordInput.classList.remove('user-invalid');
        if (passwordErrorEl) {
            passwordErrorEl.style.maxHeight = "0";
            passwordErrorEl.style.opacity = "0";
        }
    }

    if (!isValid) return;

    // 3. Domain Routing Engine
    if (email.includes('@eventhub.com')) {
        window.location.href = 'platform/dashboard.html';
    } else if (
        email === 'admin@company.com' || 
        (email.startsWith('admin') && (email.includes('@foundation.') || email.includes('@school.')))
    ) {
        window.location.href = 'organizer/dashboard.html';
    } else if (
        email.includes('@company.com') || 
        email.includes('@school.') || 
        email.includes('@foundation.')
    ) {
        window.location.href = 'users/upcoming_events.html';
    } else {
        alert('Access Denied: This corporate domain or email configuration is unregistered.');
    }
}

// Bind event listener in the browser environment
const loginForm = document.getElementById('dynamic-login-form');
if (loginForm) {
    loginForm.addEventListener('submit', handleLoginSubmit);
}

// Clear warning visuals on typing
document.querySelectorAll('.glass-input').forEach(input => {
    input.addEventListener('input', function() {
        this.classList.remove('user-invalid');
        const errorSpan = this.closest('.mb-3, .mb-4')?.querySelector('.field-error');
        if (errorSpan) {
            errorSpan.style.maxHeight = "0";
            errorSpan.style.opacity = "0";
        }
    });
});

// Export for Jest testing (does not break the browser)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { handleLoginSubmit };
}