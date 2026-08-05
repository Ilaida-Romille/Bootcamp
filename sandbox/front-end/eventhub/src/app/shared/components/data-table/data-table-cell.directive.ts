import { Directive, Input, TemplateRef } from '@angular/core';

@Directive({
  selector: '[appDtCell]',
  standalone: true
})
export class DataTableCellDirective {
  @Input('appDtCell') column!: string;
  constructor(readonly template: TemplateRef<{ $implicit: unknown }>) {}
}
