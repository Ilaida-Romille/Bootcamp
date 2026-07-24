import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Structural email regex requiring at least a 2-letter domain extension.
 * Kept in sync with the input's min/max length so the template and the
 * validator can never disagree (they did in the original vanilla page:
 * minlength=15 in HTML vs. a 5-character floor in JS).
 */
const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[a-zA-Z]{2,}$/;

export const EMAIL_MIN_LENGTH = 5;
export const EMAIL_MAX_LENGTH = 100;

/**
 * Validates that a control's value resembles a basic, well-formed email
 * address. Returns a `likelyInvalidEmail` error when it does not.
 */
export function likelyValidEmailValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = (control.value ?? '').toString().trim();

        if (!value) {
            return null; // let `Validators.required` own the empty case
        }

        if (!EMAIL_PATTERN.test(value)) {
            return { likelyInvalidEmail: true };
        }

        return null;
    };
}