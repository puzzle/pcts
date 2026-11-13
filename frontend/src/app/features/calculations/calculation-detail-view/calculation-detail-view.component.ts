import { Component, computed, inject, input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';

import { CalculationService } from '../calculation.service';
import { CalculationModel } from '../calculationDto';
import { CalculationFormComponent } from '../calculation-form/calculation-form.component';
import { SubmitButtonComponent } from '../../../shared/submit-button/submit-button.component';
import { CalculationState } from '../calculation-state.enum';
import { CancelButtonComponent } from '../../../shared/cancel-button/cancel-button.component';
import { FieldTree, submit } from '@angular/forms/signals';

@Component({
  selector: 'app-calculation-detail-view',
  standalone: true,
  imports: [
    CalculationFormComponent,
    MatButton,
    MatIcon,
    ScopedTranslationPipe,
    DatePipe,
    SubmitButtonComponent,
    CancelButtonComponent
  ],
  templateUrl: './calculation-detail-view.component.html',
  styleUrl: 'calculation-detail-view.component.scss'
})
export class CalculationDetailViewComponent {
  private readonly route = inject(ActivatedRoute);

  private readonly calculationService = inject(CalculationService);

  private readonly router = inject(Router);

  calculation = input.required<CalculationModel>();

  isAdd = computed(() => this.route.snapshot.data['mode'] === 'create');

  routeToCV() {
    this.router.navigate(['member',
      this.calculation().member.id]);
  }

  save(calculationForm: FieldTree<CalculationModel>) {
    submit(calculationForm, {
      ignoreValidators: 'none',
      action: (form) => new Promise((resolve) => {
        if (this.isAdd()) {
          this.calculationService.addCalculation(form()
            .value())
            .subscribe({
              next: () => this.routeToCV(),
              error: () => resolve(undefined)
            });
        } else {
          this.calculationService.updateCalculation(this.calculation().id, form()
            .value())
            .subscribe({
              next: () => this.routeToCV(),
              error: () => resolve(undefined)
            });
        }
      })
    });
  }

  isArchived(): boolean {
    return this.calculation().state === CalculationState.ARCHIVED;
  }

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

  protected readonly HintBoxState = HintBoxState;
}
