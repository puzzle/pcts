import { Component, inject, input } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';

import { CalculationService } from '../calculation.service';
import { CalculationModel } from '../calculationDto';
import { CalculationFormComponent } from '../calculation-form/calculation-form.component';

@Component({
  selector: 'app-calculation-detail-view',
  standalone: true,
  imports: [
    CalculationFormComponent,
    MatButton,
    MatIcon,
    ScopedTranslationPipe,
    DatePipe
  ],
  templateUrl: './calculation-detail-view.component.html',
  styleUrl: 'calculation-detail-view.component.scss'
})
export class CalculationDetailViewComponent {
  private readonly calculationService = inject(CalculationService);

  private readonly router = inject(Router);

  calculation = input.required<CalculationModel>();

  isAdd() {
    return !this.calculation().id;
  }

  routeToCV() {
    this.router.navigate(['member',
      this.calculation().member.id]);
  }

  saveForm(formComponent: CalculationFormComponent) {
    const calcForm = formComponent.calculationForm;

    if (!calcForm()
      .invalid()) {
      const formData = calcForm()
        .controlValue();

      if (!this.isAdd()) {
        this.calculationService.updateCalculation(this.calculation().id, formData)
          .subscribe(() => this.routeToCV());
      } else {
        this.calculationService.addCalculation(formData)
          .subscribe(() => this.routeToCV());
      }
    } else {
      calcForm()
        .markAsTouched();
    }
  }
}
