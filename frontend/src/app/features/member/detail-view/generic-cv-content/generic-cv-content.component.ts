import { Component, effect, input, signal } from '@angular/core';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/GenericTableDataSource';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { JsonPipe } from '@angular/common';
import { ColumnTemplateDirective } from '../../../../shared/generic-table/column-template.directive';
import { TypedTemplateDirective } from '../../../../shared/generic-table/typed-template.directive';

@Component({
  selector: 'app-generic-cv-content',
  standalone: true,
  imports: [
    ScopedTranslationPipe,
    MatButton,
    MatIcon,
    GenericTableComponent,
    ColumnTemplateDirective,
    JsonPipe,
    TypedTemplateDirective
  ],
  templateUrl: './generic-cv-content.component.html',
  styleUrl: './generic-cv-content.component.scss'
})
export class GenericCvContentComponent<T extends object> {
  title = input.required<string>();

  data = input.required<T[] | null>();

  columns = input.required<GenCol<T>[]>();

  tableDataSource = signal<GenericTableDataSource<T> | null>(null);

  constructor() {
    effect(() => {
      const cols = this.columns();
      const data = this.data();
      if (data) {
        if (this.tableDataSource()) {
          this.tableDataSource()!.columnDefs = cols;
          this.tableDataSource()!.data = data;
        } else {
          this.tableDataSource.set(new GenericTableDataSource<T>(cols, data));
        }
      }
    });
  }

  getExperienceBadgeClass(type: string): string {
    switch (type) {
      case 'Internship':
        return 'badge-internship';
      case 'Hackathon':
        return 'badge-hackathon';
      default:
        return 'badge-default';
    }
  }
}
