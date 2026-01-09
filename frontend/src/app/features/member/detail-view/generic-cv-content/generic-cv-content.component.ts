import { Component, effect, input, signal } from '@angular/core';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/GenericTableDataSource';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';

@Component({
  selector: 'app-generic-cv-content',
  standalone: true,
  imports: [
    ScopedTranslationPipe,
    MatButton,
    MatIcon,
    GenericTableComponent
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
        console.log(this.tableDataSource());
        console.log(this.columns());
        console.log(this.data());
      }
    });
  }
}
