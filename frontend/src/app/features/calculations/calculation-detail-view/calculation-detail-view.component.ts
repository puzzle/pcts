import { Component, effect, inject, input, linkedSignal } from '@angular/core';
import {
  GenericRadioFormTableComponent
} from '../../../shared/generic-table/generic-radio-form-table/generic-radio-form-table.component';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { ReactiveFormsModule } from '@angular/forms';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { TranslationScopeDirective } from '../../../shared/translation-scope/translation-scope.directive';
import { form, FormField, SchemaPath, validate, ValidationError } from '@angular/forms/signals';
import {
  GenericCheckboxFormTableComponent
} from '../../../shared/generic-table/generic-checkbox-form-table/generic-checkbox-form-table.component';
import { DegreeCalculationModel } from '../degree-calculation/degree-calculation.dto';
import { RelevancyWeightsRow } from '../../../shared/generic-table/generic-checkbox-form-table/RelevancyWeightsRow';
import { CertificateTypeModel } from '../../certificates/certificate-type/certificate-type.model';
import {
  LeadershipExperienceTypeModel
} from '../../leadership-experiences/leadership-experiences-type/leadership-experience-type.model';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { MatFormField } from '@angular/material/form-field';
import { MatSelect } from '@angular/material/select';
import { MatOption } from '@angular/material/autocomplete';
import { DatePipe } from '@angular/common';
import { CalculationService } from '../calculation.service';
import { Router } from '@angular/router';
import { CalculationModel } from '../calculationDto';
import { CalculationState } from '../calculation-state.enum';
import { NoRelevancyError } from '../../../shared/generic-table/error/no-relevancy-error';
import { RelevancyModel } from '../../../shared/generic-table/generic-radio-form-table/RelevancyModel';
import { Relevancy } from '../relevancy.enum';

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

const getCertificateTable = () => new GenericTableDataSource(getCertificateColumns());

const getCertificateColumns = (): GenCol<CertificateTypeModel>[] => [GenCol.fromAttr('name'),
  GenCol.fromAttr('points')];

const getLeadershipExperienceTable = () => new GenericTableDataSource(getLeadershipExperienceColumns());

const getLeadershipExperienceColumns = (): GenCol<LeadershipExperienceTypeModel>[] => [GenCol.fromAttr('name'),
  GenCol.fromAttr('points')];

const getExperienceTable = () => new GenericTableDataSource(getExperienceColumns());

const getExperienceColumns = (): GenCol<ExperienceCalculationModel>[] => [
  GenCol.fromCalculated('name', (e: ExperienceCalculationModel) => e.experience.name),
  GenCol.fromCalculated('highlyRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.highlyRelevantPoints),
  GenCol.fromCalculated('limitedRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.limitedRelevantPoints),
  GenCol.fromCalculated('littleRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.littleRelevantPoints),
  GenCol.fromCalculated('points', () => '-'),
  GenCol.fromCalculated('errorCol', () => {})
];

const getDegreeTable = () => new GenericTableDataSource<DegreeCalculationModel>(getDegreeColumns())
  .withDetailViewLink();

const getDegreeColumns = (): GenCol<DegreeCalculationModel>[] => [
  GenCol.fromCalculated('name', (d: DegreeCalculationModel) => d.degree.name),
  GenCol.fromCalculated('highlyRelevant', () => {}),
  GenCol.fromCalculated('limitedRelevant', () => {}),
  GenCol.fromCalculated('littleRelevant', () => {}),
  GenCol.fromCalculated('points', () => '-'),
  GenCol.fromCalculated('errorCol', () => {})
];

export function validateRelevancySelected<T extends RelevancyModel>(path: SchemaPath<T[]>) {
  validate(path, ({ value }) => value()
    .filter((row) => !row.relevancy || row.relevancy === Relevancy.NONE)
    .map((row, index): NoRelevancyError<T> => ({ kind: 'noRelevancy',
      row: row })));
}

@Component({
  imports: [
    GenericRadioFormTableComponent,
    ReactiveFormsModule,
    TranslationScopeDirective,
    FormField,
    MatButton,
    MatIcon,
    GenericTableComponent,
    ScopedTranslationPipe,
    MatFormField,
    MatSelect,
    MatOption,
    DatePipe,
    GenericCheckboxFormTableComponent
  ],
  selector: 'app-calculation-detail-view',
  templateUrl: './calculation-detail-view.component.html',
  standalone: true,
  styleUrl: 'calculation-detail-view.component.scss'
})
export class CalculationDetailViewComponent {
  private readonly calculationService = inject(CalculationService);

  private readonly router = inject(Router);

  calculation = input.required<CalculationModel>();

  calculationWritableSignal = linkedSignal(() => this.calculation());

  calculationForm = form(this.calculationWritableSignal, (calculation) => {
    validateRelevancySelected(calculation.experienceCalculations);
    validateWeightsAddingUpTo100(calculation.degreeCalculations);
  });

  certificateTable = getCertificateTable();

  leadershipExperienceTable = getLeadershipExperienceTable();

  experienceTable = getExperienceTable();

  degreeTable = getDegreeTable();

  calculationState = [CalculationState.DRAFT,
    CalculationState.ACTIVE,
    CalculationState.ARCHIVED];

  constructor() {
    effect(() => {
      this.certificateTable.data = this.calculation().certificateCalculations.map((certificateCalculation) => certificateCalculation.certificate.certificateType);

      this.leadershipExperienceTable.data = this.calculation().leadershipExperienceCalculations.map((leadershipExperienceCalculations) => leadershipExperienceCalculations.leadershipExperience.leadershipExperienceType);
    });
  }

  saveForm() {
    if (this.calculationForm()
      .invalid()) {
      this.calculationForm()
        .markAsTouched();
      return;
    }

    if (this.isAdd()) {
      this.calculationService.addCalculation(this.calculationForm()
        .controlValue())
        .subscribe(() => {
          this.routeToCV();
        });
    } else {
      this.calculationService.updateCalculation(this.calculation().id, this.calculationForm()
        .controlValue())
        .subscribe(() => {
          this.routeToCV();
        });
    }
  }

  routeToCV() {
    this.router.navigate(['member',
      this.calculation().member.id]);
  }

  isAdd() {
    return !this.calculation().id;
  }
}
