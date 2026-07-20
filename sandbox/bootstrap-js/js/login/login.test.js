const { describe, it, beforeEach } = require('node:test');
const assert = require('node:assert');
const { JSDOM } = require('jsdom');

describe('Login Handler Tests (Node Test Runner)', () => {
    let handleLoginSubmit;

    beforeEach(() => {
        // 1. Setup a fresh HTML DOM structure for each test
        const dom = new JSDOM(`
            <!DOCTYPE html>
            <html>
            <body>
                <form id="dynamic-login-form">
                    <input id="login-email" class="glass-input" value="" />
                    <span id="email-error" class="field-error"></span>

                    <input id="login-password" class="glass-input" value="" />
                    <span id="password-error" class="field-error"></span>

                    <button id="get-started-btn">Get Started</button>
                    <div id="content-row"></div>
                    <div id="hero-section"></div>
                </form>
            </body>
            </html>
        `, {
            url: "https://localhost/"
        });

        // 2. Assign globals so login.js can access document, window, & alert
        global.window = dom.window;
        global.document = dom.window.document;
        global.alert = (msg) => { global.lastAlert = msg; };
        global.lastAlert = null;

        // 3. Load email validator helper into global scope
        const emailValidatorModule = require('./emailValidator.js');
        global.isLikelyValidEmail = emailValidatorModule.isLikelyValidEmail;

        // 4. Clear module cache so login.js re-initializes clean DOM bindings
        delete require.cache[require.resolve('./login.js')];
        const loginModule = require('./login.js');
        handleLoginSubmit = loginModule.handleLoginSubmit;
    });

    it('should show error for invalid email and password', () => {
        const emailInput = global.document.getElementById('login-email');
        const passwordInput = global.document.getElementById('login-password');
        const emailErrorEl = global.document.getElementById('email-error');

        emailInput.value = 'invalid-email';
        passwordInput.value = 'short';

        handleLoginSubmit({ preventDefault: () => {} });

        // Assert invalid styles & email error display
        assert.strictEqual(emailInput.classList.contains('user-invalid'), true);
        assert.strictEqual(emailErrorEl.style.opacity, '1');
    });

    it('should trigger alert when email is valid but domain is unregistered', () => {
        const emailInput = global.document.getElementById('login-email');
        const passwordInput = global.document.getElementById('login-password');

        emailInput.value = 'user@unknown.com';
        passwordInput.value = 'ValidPass123!';

        handleLoginSubmit({ preventDefault: () => {} });

        assert.strictEqual(
            global.lastAlert,
            'Access Denied: This corporate domain or email configuration is unregistered.'
        );
    });
});