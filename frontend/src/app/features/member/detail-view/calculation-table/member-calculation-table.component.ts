import { Component, effect, inject, input, signal } from '@angular/core';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { MemberService } from '../../member.service';
import { getCalculationTable } from '../cv/member-detail-cv-table-definition';
import { CrudButtonComponent } from '../../../../shared/crud-button/crud-button.component';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { CalculationModel } from '../../../calculations/calculation.model';

@Component({
  selector: 'app-member-calculation-table',
  imports: [GenericTableComponent,
    CrudButtonComponent,
    ScopedTranslationPipe],
  templateUrl: './member-calculation-table.component.html'
})
export class MemberCalculationTableComponent {
  private readonly memberService: MemberService = inject(MemberService);

  memberId = input.required<number>();

  roleId = input<number>();

  calculations = signal<CalculationModel[]>([]);

  calculationTable = getCalculationTable();

  constructor() {
    effect(() => {
      this.memberService.getCalculationsByMemberIdAndOptionalRoleId(this.memberId(), this.roleId())
        .subscribe({
          next: (calculations) => {
            this.calculations.set(calculations);
            this.calculationTable.data = calculations;
          }
        });
    });
  }
}
