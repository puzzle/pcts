import { Component, effect, inject, input, linkedSignal } from '@angular/core';
import {
  GenericRadioFormTableComponent
} from '../../../shared/generic-table/generic-radio-form-table/generic-radio-form-table.component';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
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
import { form, FormField } from '@angular/forms/signals';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { CalculationModel } from '../calculation.model';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CalculationService } from '../calculation.service';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { CertificateTypeModel } from '../../certificates/certificate-type/certificate-type.model';
import {
  LeadershipExperienceTypeModel
} from '../../leadership-experiences/leadership-experiences-type/leadership-experience-type.model';
import { MatFormField, MatLabel } from '@angular/material/input';
import { MatOption, MatSelect } from '@angular/material/select';
import { CalculationState } from '../calculation-state.enum';

const getCertificateTable = () => new GenericTableDataSource(getCertificateColumns())
  .withLimit(10);

const getCertificateColumns = (): GenCol<CertificateTypeModel>[] => [GenCol.fromAttr('name'),
  GenCol.fromAttr('points')];

const getLeadershipExperienceTable = () => new GenericTableDataSource(getLeadershipExperienceColumns())
  .withLimit(10);

const getLeadershipExperienceColumns = (): GenCol<LeadershipExperienceTypeModel>[] => [GenCol.fromAttr('name'),
  GenCol.fromAttr('points')];

const getExperienceTable = () => new GenericTableDataSource(getExperienceColumns())
  .withLimit(10);

const getExperienceColumns = (): GenCol<ExperienceCalculationModel>[] => [
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

@Component({
  imports: [
    GenericRadioFormTableComponent,
    ReactiveFormsModule,
    TranslationScopeDirective,
    FormField,
    GenericCheckboxFormTableComponent
    FormField,
    MatButton,
    MatIcon,
    GenericTableComponent,
    ScopedTranslationPipe,
    MatFormField,
    MatSelect,
    MatOption,
    DatePipe
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

  calculationForm = form(this.calculationWritableSignal);

  certificateTable = getCertificateTable();

  leadershipExperienceTable = getLeadershipExperienceTable();

  experienceTable = getExperienceTable();


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
    if (!this.isAdd()) {
      this.calculationService.updateCalculation(this.calculation().id, this.calculationForm()
        .controlValue())
        .subscribe(() => {
          this.routeToCV();
        });
    } else {
      this.calculationService.addCalculation(this.calculation())
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
