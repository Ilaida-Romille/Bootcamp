export interface EventItem {
  id: number;
  title: string;
  date: string;
  organizer: string;
  category: string;
  capacity: string;
  status: 'Open' | 'Filling Fast' | 'Full';
  statusClass: 'status-open' | 'status-filling' | 'status-full';
}