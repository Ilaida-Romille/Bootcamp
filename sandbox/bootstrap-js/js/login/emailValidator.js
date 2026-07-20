// emailValidator.js

/**
 * Validates if a string resembles a basic email structure.
 * @param {string} str - Input email to validate
 * @returns {boolean}
 */
function isLikelyValidEmail(str) {
    if (typeof str !== 'string') return false;
    
    const trimmed = str.trim();
    
    if (trimmed.length < 5 || trimmed.length > 100) {
        return false;
    }

    // Standard structural email regex requiring at least a 2-letter domain extension
    const emailRegex = /^[^\s@]+@[^\s@]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(trimmed);
}

// --- Unit Tests for Isolation ---
function runEmailTests() {
    console.log("--- Running Email Validation Tests ---");
    
    const testCases = [
        { email: "user@company.com", expected: true },
        { email: "admin@school.edu", expected: true },
        { email: "  user@domain.com  ", expected: true }, // Tests whitespace trimming
        { email: "invalid-email", expected: false },      // Missing @ and domain
        { email: "user@.com", expected: false },          // Missing domain name
        { email: "user@domain", expected: false },        // Missing TLD (.com)
        { email: "@domain.com", expected: false },        // Missing username
        { email: null, expected: false }                  // Non-string input
    ];

    let passed = 0;
    testCases.forEach((tc, index) => {
        const result = isLikelyValidEmail(tc.email);
        if (result === tc.expected) {
            console.log(`✅ Test ${index + 1} Passed: "${tc.email}" -> ${result}`);
            passed++;
        } else {
            console.error(`❌ Test ${index + 1} Failed: "${tc.email}". Expected ${tc.expected}, got ${result}`);
        }
    });

    console.log(`--- Tests Complete: ${passed}/${testCases.length} Passed ---`);
}

// Only execute console tests when running in a browser environment (not during Jest test runner)
if (typeof window !== 'undefined' && typeof process === 'undefined') {
    runEmailTests();
}

// Export for Jest testing environment
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { isLikelyValidEmail };
}