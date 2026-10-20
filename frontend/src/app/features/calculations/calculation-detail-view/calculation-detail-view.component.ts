import { Component, inject, signal } from '@angular/core';
import {
  GenericRadioFormTableComponent
} from '../../../shared/generic-table/generic-radio-form-table/generic-radio-form-table.component';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { MemberService } from '../../member/member.service';
import { ReactiveFormsModule } from '@angular/forms';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { Relevancy } from '../relevancy.enum';
import { TranslationScopeDirective } from '../../../shared/translation-scope/translation-scope.directive';
import { form, FormField } from '@angular/forms/signals';
import {
  GenericCheckboxFormTableComponent
} from '../../../shared/generic-table/generic-checkbox-form-table/generic-checkbox-form-table.component';
import { DegreeCalculationModel } from '../degree-calculation/degree-calculation.model';
import { JsonPipe } from '@angular/common';

@Component({
  imports: [
    GenericRadioFormTableComponent,
    ReactiveFormsModule,
    TranslationScopeDirective,
    FormField,
    GenericCheckboxFormTableComponent,
    JsonPipe
  ],
  selector: 'app-calculation-detail-view',
  templateUrl: './calculation-detail-view.component.html'
})
export class CalculationDetailViewComponent {
  private readonly memberService = inject(MemberService);

  private readonly experienceCalculations = signal<ExperienceCalculationModel[]>([] as ExperienceCalculationModel[]);

  degreeCalculations = signal<DegreeCalculationModel[]>([] as DegreeCalculationModel[]);

  constructor() {
    // Todo: Implement calculation service then fetch from calculation id instead of going over member service this is for the new ticket
    this.memberService.getCalculationsByMemberIdAndOptionalRoleId(1)
      .subscribe((response) => {
        response.forEach((result) => {
          this.experienceCalculations.set(result.experienceCalculations);
        });
      });

    this.memberService.getCalculationsByMemberIdAndOptionalRoleId(2)
      .subscribe((response) => {
        response.forEach((result) => {
          this.degreeCalculations.set(result.degreeCalculations);
        });
      });
  }

  experienceCalculationForm = form(this.experienceCalculations);

  degreeCalculationForm = form(this.degreeCalculations);

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

  getDegreeCalculationTableData = () => new GenericTableDataSource<DegreeCalculationModel>(this.getDegreeCalculationColumns())
    .withDetailViewLink();

  getDegreeCalculationColumns = (): GenCol<DegreeCalculationModel>[] => [
    GenCol.fromCalculated('name', (d: DegreeCalculationModel) => d.degree.name),
    GenCol.fromCalculated('highlyRelevant', () => {}),
    GenCol.fromCalculated('limitedRelevant', () => {}),
    GenCol.fromCalculated('littleRelevant', () => {}),
    GenCol.fromCalculated('points', (d: DegreeCalculationModel) => {})
  ];
}
