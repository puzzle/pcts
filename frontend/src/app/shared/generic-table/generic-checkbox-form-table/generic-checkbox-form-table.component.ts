import { Component, effect, input, model, output, WritableSignal } from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { applyEach, disabled, FieldTree, form, FormValueControl, max, min } from '@angular/forms/signals';
import { GenericTableDataSource } from '../generic-table-data-source';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RowDetailTemplateDirective } from '../rowDetailTemplate.directive';
import { TypedTemplateDirective } from '../type-template/typed-template.directive';
import { addsUpToTarget, isBetweenUpperAndLower } from '../../form/form-validators';
import { RelevancyWeightsRow } from './RelevancyWeightsRow';

@Component({
  imports: [
    MatCheckbox,
    GenericTableComponent,
    ScopedTranslationPipe,
    ColumnTemplateDirective,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    RowDetailTemplateDirective,
    TypedTemplateDirective,
    MatFormField,
    MatLabel
  ],
  selector: 'app-generic-checkbox-form-table',
  styleUrl: './generic-checkbox-form-table.component.scss',
  templateUrl: './generic-checkbox-form-table.component.html'
})
export class GenericCheckboxFormTableComponent<T extends { id: number;
  relevancies: Map<Relevancy, number>; }> implements FormValueControl<T[]> {
  RELEVANCY_COLUMNS: { relevancy: Relevancy }[] = [{ relevancy: Relevancy.POORLY },
    { relevancy: Relevancy.NORMAL },
    { relevancy: Relevancy.STRONGLY }];

  protected readonly Relevancy = Relevancy;

  protected readonly Number = Number;

  value = model<T[]>([]);

  table = input.required<GenericTableDataSource<T>>();

  readonly touch = output<void>();

  relevancyForm = new FormGroup({
    comment: new FormControl(''),

    highlyRelevant: new FormControl('', isBetweenUpperAndLower(0, 100)),

    limitedRelevant: new FormControl('', isBetweenUpperAndLower(0, 100)),

    littleRelevant: new FormControl('', isBetweenUpperAndLower(0, 100))
  }, {
    validators: addsUpToTarget(100)
  });

  relevancyMap = new Map<Relevancy, {
    enabled: boolean;
    formControl: FormControl;
  }>([[Relevancy.STRONGLY,
    { enabled: true,
      formControl: this.relevancyForm.controls.highlyRelevant }],
  [Relevancy.NORMAL,
    { enabled: true,
      formControl: this.relevancyForm.controls.limitedRelevant }],
  [Relevancy.POORLY,
    { enabled: true,
      formControl: this.relevancyForm.controls.littleRelevant }]]);

  constructor() {
    effect(() => {
      const value = this.value();

      if (value) {
        this.table().data = value;
      }
    });
  }

  shouldBeChecked(): boolean {
    return true;
  }

  updateInputFields(relevancy: Relevancy) {
    const smth = this.relevancyMap.get(relevancy);

    if (smth) {
      smth.enabled ? smth.formControl.disable() : smth.formControl.enable();
      smth.enabled = !smth.enabled;
    }
  }

  protected readonly rows: FieldTree<RelevancyWeightsRow[]> = form(this.value as unknown as WritableSignal<RelevancyWeightsRow[]>, (rows) => applyEach(rows, (row) => {
    this.RELEVANCY_COLUMNS.forEach(({ relevancy }) => {
      min(row.relevancies[relevancy], 1);
      max(row.relevancies[relevancy], 100);
      // a single selection is an implicit 100, the user only types weights once there is something to split
      disabled(row.relevancies[relevancy], { when: ({ value, valueOf }) => value() === null || this.checkedRelevancies().length < 2 });
    });
  }));

  checkedRelevancies() {
    const checked: Relevancy[] = [];
    for (const entry of this.relevancyMap.keys()) {
      if (this.relevancyMap.get(entry)?.enabled) {
        checked.push(entry);
      }
    }
    return checked;
  }
}
