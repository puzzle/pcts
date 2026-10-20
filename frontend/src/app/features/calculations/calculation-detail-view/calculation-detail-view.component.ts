import { Component, input } from '@angular/core';
import {
  GenericRadioFormTableComponent
} from '../../../shared/generic-table/generic-radio-form-table/generic-radio-form-table.component';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { ReactiveFormsModule } from '@angular/forms';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { Relevancy } from '../relevancy.enum';
import { TranslationScopeDirective } from '../../../shared/translation-scope/translation-scope.directive';
import { FormField } from '@angular/forms/signals';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { CalculationModel } from '../calculation.model';

@Component({
  imports: [
    GenericRadioFormTableComponent,
    ReactiveFormsModule,
    TranslationScopeDirective,
    FormField,
    MatButton,
    MatIcon,
    RouterLink
  ],
  selector: 'app-calculation-detail-view',
  templateUrl: './calculation-detail-view.component.html'
})
export class CalculationDetailViewComponent {
  calculation = input.required<CalculationModel>();

  getColumns = [
    GenCol.fromCalculated('name', (e: ExperienceCalculationModel) => e.experience.name),
    GenCol.fromCalculated('highlyRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.highlyRelevantPoints),
    GenCol.fromCalculated('limitedRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.limitedRelevantPoints),
    GenCol.fromCalculated('littleRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.littleRelevantPoints),
    GenCol.fromCalculated('points', (e: ExperienceCalculationModel) => {
      switch (e.relevancy) {
        case Relevancy.STRONGLY:
          return e.experience.experienceType.highlyRelevantPoints;
        case Relevancy.NORMAL:
          return e.experience.experienceType.limitedRelevantPoints;
        case Relevancy.POORLY:
          return e.experience.experienceType.littleRelevantPoints;
        default:
          return 0;
      }
    })
  ];

  getTableData = () => new GenericTableDataSource(this.getColumns)
    .withLimit(10);
}
