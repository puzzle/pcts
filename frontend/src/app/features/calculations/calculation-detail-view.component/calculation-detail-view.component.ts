import { Component, inject } from '@angular/core';
import {
  GenericRadioFormTableComponent
} from '../../../shared/generic-table/generic-radio-form-table/generic-radio-form-table.component';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { MemberService } from '../../member/member.service';
import {
  RadioFormTableResponse
} from '../../../shared/generic-table/generic-radio-form-table/radio-form-table-response.model';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { Relevancy } from '../relevancy.enum';

@Component({
  imports: [GenericRadioFormTableComponent,
    ReactiveFormsModule],
  selector: 'app-calculation-detail-view.component',
  templateUrl: './calculation-detail-view.component.html'
})
export class CalculationDetailViewComponent {
  private memberService = inject(MemberService);

  experienceCalculationModels: ExperienceCalculationModel[] = [];

  protected formControl = new FormControl();

  constructor() {
    this.memberService.getCalculationsByMemberIdAndOptionalRoleId(1)
      .subscribe((response) => {
        response.forEach((result) => {
          this.experienceCalculationModels.push(...result.experienceCalculations);
        });
        const map = this.experienceCalculationModels.map((experienceCalculation) => {
          return {
            row: experienceCalculation,
            relevancy: experienceCalculation.relevancy
          } as RadioFormTableResponse<ExperienceCalculationModel>;
        });
        this.formControl.setValue(map);
      });
  }

  getTableData = () => new GenericTableDataSource(this.getColumns())
    .withLimit(10)
    .withDetailViewLink();

  getColumns = (): GenCol<ExperienceCalculationModel>[] => [
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

  changeData(object: { row: ExperienceCalculationModel;
    value: Relevancy; }) {
    const row = object.row;

    const value = object.value;

    this.experienceCalculationModels.forEach((experienceCalculation) => {
      if (experienceCalculation.id === row.id) {
        experienceCalculation.relevancy = value;
      }
    });
  }
}
