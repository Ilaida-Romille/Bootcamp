import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const PASSWORD_MIN_LENGTH = 8;

export interface PasswordStrengthErrors {
    minLength?: true;
    missingUppercase?: true;
    missingLowercase?: true;
    missingNumber?: true;
    missingSpecialChar?: true;
}

const UPPERCASE = /[A-Z]/;
const LOWERCASE = /[a-z]/;
const NUMBER = /[0-9]/;
const SPECIAL_CHAR = /[!@#$%^&*(),.?":{}|<>]/;

/**
 * Validates password strength rule-by-rule so the template can render a
 * specific, actionable message per missing rule (same UX as the original
 * page's dynamically-built sentence, without hand-building the string in
 * the submit handler).
 */
export function passwordStrengthValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value: string = control.value ?? '';
        if (!value) {
            return null; // let `Validators.required` own the empty case
        }

        const errors: PasswordStrengthErrors = {};

        if (value.length < PASSWORD_MIN_LENGTH) errors.minLength = true;
        if (!UPPERCASE.test(value)) errors.missingUppercase = true;
        if (!LOWERCASE.test(value)) errors.missingLowercase = true;
        if (!NUMBER.test(value)) errors.missingNumber = true;
        if (!SPECIAL_CHAR.test(value)) errors.missingSpecialChar = true;

        return Object.keys(errors).length > 0 ? { passwordStrength: errors } : null;
    };
}

/** Builds the same human-readable sentence the vanilla page assembled by hand. */
export function describePasswordErrors(errors: PasswordStrengthErrors): string {
    const rules: string[] = [];
    if (errors.minLength) rules.push('be at least 8 characters');
    if (errors.missingUppercase) rules.push('contain an uppercase letter');
    if (errors.missingLowercase) rules.push('contain a lowercase letter');
    if (errors.missingNumber) rules.push('contain a number');
    if (errors.missingSpecialChar) rules.push('contain a special character (e.g., !@#$)');

    if (rules.length === 0) return '';
    if (rules.length === 1) return `Password must ${rules[0]}.`;
    if (rules.length === 2) return `Password must ${rules.join(' and ')}.`;

    const last = rules[rules.length - 1];
    return `Password must ${rules.slice(0, -1).join(', ')}, and ${last}.`;
}