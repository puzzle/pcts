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
import { FieldTree, form, FormField } from '@angular/forms/signals';

@Component({
  imports: [
    GenericRadioFormTableComponent,
    ReactiveFormsModule,
    TranslationScopeDirective,
    FormField
  ],
  selector: 'app-calculation-detail-view',
  templateUrl: './calculation-detail-view.component.html'
})
export class CalculationDetailViewComponent {
  private readonly memberService = inject(MemberService);

  experienceCalculationModels: ExperienceCalculationModel[] = [];

  private experienceCalculations = signal<{ row: ExperienceCalculationModel;
    relevancy: Relevancy; }[]>([{}] as { row: ExperienceCalculationModel;
    relevancy: Relevancy; }[]);

  protected experienceCalculationForm: FieldTree<{
    row: ExperienceCalculationModel;
    relevancy: Relevancy;
  }[]>;

  constructor() {
    // Todo: Implement calculation service then fetch from calculation id instead of going over member service
    this.memberService.getCalculationsByMemberIdAndOptionalRoleId(1)
      .subscribe((response) => {
        response.forEach((result) => {
          this.experienceCalculationModels.push(...result.experienceCalculations);
        });
        const map = this.experienceCalculationModels.map((experienceCalculation) => {
          return {
            row: experienceCalculation,
            relevancy: experienceCalculation.relevancy
          } as { row: ExperienceCalculationModel;
            relevancy: Relevancy; };
        });
        this.experienceCalculations.set(map);
      });
    this.experienceCalculationForm = form(this.experienceCalculations);
  }

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

  getTableData = new GenericTableDataSource(this.getColumns)
    .withLimit(10);

  changeData(object: { row: ExperienceCalculationModel;
    relevancy: Relevancy; }) {
    const row = object.row;

    const value = object.relevancy;

    this.experienceCalculationModels.forEach((experienceCalculation) => {
      if (experienceCalculation.id === row.id) {
        experienceCalculation.relevancy = value;
      }
    });
  }
}
