import { Component, effect, input, output } from '@angular/core';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { GenericTableDataSource } from '../generic-table-data-source';
import { MatRadioButton } from '@angular/material/radio';
import { Relevancy } from '../../../features/calculations/relevancy.enum';

@Component({
  imports: [
    ColumnTemplateDirective,
    GenericTableComponent,
    ScopedTranslationPipe,
    MatRadioButton
  ],
  selector: 'app-generic-radio-form-table',
  styleUrl: './generic-radio-form-table.component.scss',
  templateUrl: './generic-radio-form-table.component.html'
})

export class GenericRadioFormTableComponent<T extends object> {
  data = input.required<T[]>();

  table = input.required<GenericTableDataSource<T>>();

  handleRadioButtonClick = output<{ row: T;
    value: Relevancy; }>();

  constructor() {
    effect(() => {
      const dataValue = this.data();
      if (dataValue) {
        this.table().data = dataValue;
      }
    });
  }

  protected readonly Relevancy = Relevancy;
}


