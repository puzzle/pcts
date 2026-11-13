import {


  Component, computed,
  effect,
  input,
  model,
  output, Signal
} from '@angular/core';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { GenericTableDataSource } from '../generic-table-data-source';
import { MatRadioButton } from '@angular/material/radio';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { FormValueControl, ValidationError } from '@angular/forms/signals';
import { RelevancyModel } from './RelevancyModel';
import { MatIcon } from '@angular/material/icon';
import { NoRelevancyError } from '../../../features/calculations/calculation-form/calculation-form.component';

@Component({
  imports: [
    ColumnTemplateDirective,
    GenericTableComponent,
    ScopedTranslationPipe,
    MatRadioButton,
    MatIcon
  ],
  selector: 'app-generic-radio-form-table',
  styleUrl: './generic-radio-form-table.component.scss',
  standalone: true,
  templateUrl: './generic-radio-form-table.component.html'
})

export class GenericRadioFormTableComponent<T extends RelevancyModel> implements FormValueControl<T[]> {
  protected readonly Relevancy = Relevancy;

  errors = input<readonly ValidationError.WithOptionalFieldTree[]>([]);

  value = model<T[]>([]);

  table = input.required<GenericTableDataSource<T>>();

  disabled = input<boolean>(false);

  private readonly invalidRows: Signal<Set<T>> = computed(() => {
    const errors = this.errors() as unknown as NoRelevancyError<T>[];
    return new Set(errors
      .map((error) => error?.row));
  });

  readonly touch = output<void>();

  readonly touched = input(true);

  constructor() {
    effect(() => {
      const value = this.value();
      if (value) {
        this.table().data = value;
      }
    });
  }

  shouldBeChecked(row: T, relevancy: Relevancy): boolean {
    return row.relevancy === relevancy;
  }

  changeData(row: T, relevancy: Relevancy) {
    this.value.update((experienceCalculation) => experienceCalculation.map((entry) => {
      return entry === row ? { ...entry,
        relevancy } : entry;
    }));

    this.touch.emit();
  }

  getIndexOfRow(row: T): string {
    return this.value()
      .indexOf(row)
      .toString();
  }

  isRowInvalid(row: T): boolean {
    console.log(this.invalidRows(), row);
    console.log(this.invalidRows()
      .has(row));
    return this.invalidRows()
      .has(row);
  }
}


