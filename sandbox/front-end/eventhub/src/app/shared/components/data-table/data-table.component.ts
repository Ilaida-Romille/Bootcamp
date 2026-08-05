import {
  AfterContentInit,
  Component,
  ContentChildren,
  EventEmitter,
  Input,
  Output,
  QueryList
} from '@angular/core';
import { CommonModule, NgTemplateOutlet } from '@angular/common';
import { DataTableCellDirective } from './data-table-cell.directive';

export interface ColumnDef {
  key: string;
  header: string;
  /** @default true */
  sortable?: boolean;
  cssClass?: string;
}

export interface SortEvent {
  key: string;
  dir: 'asc' | 'desc';
}

@Component({
  selector: 'app-data-table',
  standalone: true,
  imports: [CommonModule, NgTemplateOutlet],
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.css'
})
export class DataTableComponent implements AfterContentInit {
  @Input() columns: ColumnDef[] = [];
  @Input() data: unknown[] = [];
  @Input() loading = false;
  @Input() emptyMessage = 'No records found.';
  @Input() sortKey = '';
  @Input() sortDir: 'asc' | 'desc' = 'asc';

  @Output() readonly sortChange = new EventEmitter<SortEvent>();

  @ContentChildren(DataTableCellDirective) private readonly cellDefs!: QueryList<DataTableCellDirective>;

  private readonly templateMap = new Map<string, DataTableCellDirective>();

  ngAfterContentInit(): void {
    this.buildTemplateMap();
    this.cellDefs.changes.subscribe(() => this.buildTemplateMap());
  }

  private buildTemplateMap(): void {
    this.templateMap.clear();
    this.cellDefs.forEach((d) => this.templateMap.set(d.column, d));
  }

  getCellDirective(key: string): DataTableCellDirective | undefined {
    return this.templateMap.get(key);
  }

  /** Fallback plain-text cell value for columns without a custom template. */
  getCellValue(row: unknown, key: string): unknown {
    return (row as Record<string, unknown>)[key] ?? '';
  }

  onHeaderClick(key: string): void {
    const col = this.columns.find((c) => c.key === key);
    if (!col || col.sortable === false) return;
    const dir: 'asc' | 'desc' = this.sortKey === key && this.sortDir === 'asc' ? 'desc' : 'asc';
    this.sortChange.emit({ key, dir });
  }
}
