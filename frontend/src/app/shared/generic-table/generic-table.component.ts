import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCell, MatColumnDef, MatHeaderCell, MatTable } from '@angular/material/table';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';

@Component({
  selector: 'app-generic-table',
  standalone: true,
  imports: [
    CommonModule,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    ScopedTranslationPipe
  ],
  templateUrl: './generic-table.component.html'
})
export class GenericTableComponent<T> {
  data = input.required<T[]>();

  columns = input.required<{ key: keyof T;
    label: string; }[]>();
}
