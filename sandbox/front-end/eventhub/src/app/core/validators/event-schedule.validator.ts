export interface EventScheduleErrors {
  endBeforeStart?: string;
  registrationOrderInvalid?: string;
  registrationAfterStart?: string;
}

export function validateEventSchedule(
  startDateTime: string,
  endDateTime: string,
  registrationOpensAt: string,
  registrationClosesAt: string
): EventScheduleErrors {
  const errors: EventScheduleErrors = {};

  if (startDateTime && endDateTime && new Date(endDateTime) <= new Date(startDateTime)) {
    errors.endBeforeStart = 'End date/time must be after the start date/time.';
  }

  if (registrationOpensAt && registrationClosesAt && new Date(registrationClosesAt) <= new Date(registrationOpensAt)) {
    errors.registrationOrderInvalid = 'Registration close date must be after the registration open date.';
  }

  if (registrationClosesAt && startDateTime && new Date(registrationClosesAt) > new Date(startDateTime)) {
    errors.registrationAfterStart = 'Registration must close on or before the event start date/time.';
  }

  return errors;
}

export function getFirstScheduleError(errors: EventScheduleErrors): string | null {
  return errors.endBeforeStart ?? errors.registrationOrderInvalid ?? errors.registrationAfterStart ?? null;
}

export function validateEventCapacity(maximum: number): string | null {
  return maximum < 1 ? 'Maximum capacity must be at least 1.' : null;
}

export function validateAgendaItems(
  agenda: Array<{ startTime: string; endTime: string; title: string }>
): string | null {
  for (let i = 0; i < agenda.length; i++) {
    const item = agenda[i];
    if (!item.title.trim()) {
      return `Agenda item ${i + 1} requires a title.`;
    }
    if (item.startTime && item.endTime && new Date(item.endTime) <= new Date(item.startTime)) {
      return `Agenda item ${i + 1}: end time must be after start time.`;
    }
  }
  return null;
}
