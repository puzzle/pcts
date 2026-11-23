import { Component, effect, input, linkedSignal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { disabled, form, FormField, SchemaPath, validate, ValidationError } from '@angular/forms/signals';

import { GenericRadioFormTableComponent } from '../../../shared/generic-table/generic-radio-form-table/generic-radio-form-table.component';
import { GenericCheckboxFormTableComponent } from '../../../shared/generic-table/generic-checkbox-form-table/generic-checkbox-form-table.component';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';

import { RelevancyWeightsRow } from '../../../shared/generic-table/generic-checkbox-form-table/RelevancyWeightsRow';
import { RelevancyModel } from '../../../shared/generic-table/generic-radio-form-table/RelevancyModel';
import { CalculationModel } from '../calculationDto';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { DegreeCalculationModel } from '../degree-calculation/degree-calculation.dto';
import { CertificateTypeModel } from '../../certificates/certificate-type/certificate-type.model';
import { LeadershipExperienceTypeModel } from '../../leadership-experiences/leadership-experiences-type/leadership-experience-type.model';
import { Relevancy } from '../relevancy.enum';
import { CalculationState } from '../calculation-state.enum';

import { MatFormField } from '@angular/material/form-field';
import { MatSelect } from '@angular/material/select';
import { MatOption } from '@angular/material/autocomplete';
import { InvalidTotalError } from '../../../shared/generic-table/error/invalid-total-error';

export interface NoRelevancyError<T extends RelevancyModel> extends ValidationError { kind: 'noRelevancy';
  row: T; }

export function validateWeightsAddingUpTo100<T extends RelevancyWeightsRow>(path: SchemaPath<T[]>) {
  validate(path, ({ value }) => value()
    .reduce<InvalidTotalError<T>[]>((errors, row) => {
      const total = Object.values(row.relevancies)
        .filter((rel) => rel.checked)
        .reduce((sum, rel) => sum + rel.weight, 0);
      if (total !== 100) {
        errors.push({ kind: 'invalidTotal',
          row,
          total });
      }
      return errors;
    }, []));
}

export function validateRelevancySelected<T extends RelevancyModel>(path: SchemaPath<T[]>) {
  validate(path, ({ value }) => value()
    .filter((row) => !row.relevancy || row.relevancy === Relevancy.NONE)
    .map((row): NoRelevancyError<T> => ({ kind: 'noRelevancy',
      row })));
}

const getCertificateColumns = (): GenCol<CertificateTypeModel>[] => [GenCol.fromAttr('name'),
  GenCol.fromAttr('points')];

const getLeadershipExperienceColumns = (): GenCol<LeadershipExperienceTypeModel>[] => [GenCol.fromAttr('name'),
  GenCol.fromAttr('points')];

const getExperienceColumns = (): GenCol<ExperienceCalculationModel>[] => [
  GenCol.fromCalculated('name', (e: ExperienceCalculationModel) => e.experience.name),
  GenCol.fromCalculated('highlyRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.highlyRelevantPoints),
  GenCol.fromCalculated('limitedRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.limitedRelevantPoints),
  GenCol.fromCalculated('littleRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.littleRelevantPoints),
  GenCol.fromCalculated('points', (e: ExperienceCalculationModel) => {
    switch (e.relevancy) {
      case Relevancy.STRONGLY: return e.experience.experienceType.highlyRelevantPoints;
      case Relevancy.NORMAL: return e.experience.experienceType.limitedRelevantPoints;
      case Relevancy.POORLY: return e.experience.experienceType.littleRelevantPoints;
      default: return 0;
    }
  }),
  GenCol.fromCalculated('errorCol', () => {})
];

const getDegreeColumns = (): GenCol<DegreeCalculationModel>[] => [
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
        case Relevancy.STRONGLY: points += factor * (e.degree.type.highlyRelevantPoints || 0); break;
        case Relevancy.NORMAL: points += factor * (e.degree.type.limitedRelevantPoints || 0); break;
        case Relevancy.POORLY: points += factor * (e.degree.type.littleRelevantPoints || 0); break;
      }
    }
    return Number.parseFloat(String(points))
      .toFixed(2);
  }),
  GenCol.fromCalculated('errorCol', () => {})
];

@Component({
  selector: 'app-calculation-form',
  standalone: true,
  imports: [
    GenericRadioFormTableComponent,
    GenericCheckboxFormTableComponent,
    GenericTableComponent,
    ReactiveFormsModule,
    FormField,
    ScopedTranslationPipe,
    MatFormField,
    MatSelect,
    MatOption
  ],
  templateUrl: './calculation-form.component.html'
})
export class CalculationFormComponent {
  isReadOnly = input<boolean>(false);

  calculation = input.required<CalculationModel>();

  calculationWritableSignal = linkedSignal(() => this.calculation());

  calculationForm = form(this.calculationWritableSignal, (calc) => {
    disabled(calc.state, { when: () => this.isReadOnly() });
    disabled(calc.experienceCalculations, { when: () => this.isReadOnly() });
    disabled(calc.degreeCalculations, { when: () => this.isReadOnly() });

    validateRelevancySelected(calc.experienceCalculations);
    validateWeightsAddingUpTo100(calc.degreeCalculations);
  });

  certificateTableDataSource = new GenericTableDataSource(getCertificateColumns());

  leadershipExperienceTableDataSource = new GenericTableDataSource(getLeadershipExperienceColumns());

  experienceTableDataSource = new GenericTableDataSource(getExperienceColumns());

  degreeTableDataSource = new GenericTableDataSource(getDegreeColumns())
    .withDetailViewLink();

  calculationState = [CalculationState.DRAFT,
    CalculationState.ACTIVE,
    CalculationState.ARCHIVED];

  constructor() {
    effect(() => {
      this.certificateTableDataSource.data = this.calculation().certificateCalculations.map((c) => c.certificate.certificateType);
      this.leadershipExperienceTableDataSource.data = this.calculation().leadershipExperienceCalculations.map((l) => l.leadershipExperience.leadershipExperienceType);
    });
  }
}
