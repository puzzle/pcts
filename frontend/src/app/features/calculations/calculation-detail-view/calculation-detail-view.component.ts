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
import { form, FormField, SchemaPath, validate, ValidationError } from '@angular/forms/signals';
import {
  GenericCheckboxFormTableComponent
} from '../../../shared/generic-table/generic-checkbox-form-table/generic-checkbox-form-table.component';
import { DegreeCalculationModel } from '../degree-calculation/degree-calculation.dto';
import { RelevancyWeightsRow } from '../../../shared/generic-table/generic-checkbox-form-table/RelevancyWeightsRow';


export interface InvalidTotalError<T extends RelevancyWeightsRow> extends ValidationError {
  kind: 'invalidTotal';
  row: T;
  total: number;
}

export function validateWeightsAddingUpTo100<T extends RelevancyWeightsRow>(path: SchemaPath<T[]>) {
  validate(path, ({ value }) => value()
    .reduce<InvalidTotalError<T>[]>((errors, row) => {
      const total = Object.values(row.relevancies)
        .filter((rel) => rel.checked)
        .reduce((sum, rel) => sum + rel.weight, 0);

      if (total !== 100) {
        errors.push({
          kind: 'invalidTotal',
          row,
          total
        });
      }

      return errors;
    }, []));
}

@Component({
  imports: [
    GenericRadioFormTableComponent,
    ReactiveFormsModule,
    TranslationScopeDirective,
    FormField,
    GenericCheckboxFormTableComponent
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

  degreeCalculationForm = form(this.degreeCalculations, (degreeCalculation) => validateWeightsAddingUpTo100(degreeCalculation));

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
    GenCol.fromCalculated('points', (e: DegreeCalculationModel) => {
      let points = 0;
      for (const [key,
        value] of Object.entries(e.relevancies || {})) {
        const factor = value.weight / 100;

        switch (key as Relevancy) {
          case Relevancy.STRONGLY:
            points += factor * (e.degree.type.highlyRelevantPoints || 0);
            break;
          case Relevancy.NORMAL:
            points += factor * (e.degree.type.limitedRelevantPoints || 0);
            break;
          case Relevancy.POORLY:
            points += factor * (e.degree.type.littleRelevantPoints || 0);
            break;
          default:
            break;
        }
      }

      return Number.parseFloat(String(points))
        .toFixed(2);
    }),
    GenCol.fromCalculated('errorCol', () => {})
  ];
}
