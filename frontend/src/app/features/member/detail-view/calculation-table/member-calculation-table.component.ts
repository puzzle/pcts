import { Component, computed, inject, input } from '@angular/core';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { MemberService } from '../../member.service';
import { getCalculationTable } from '../cv/member-detail-cv-table-definition';
import { CrudButtonComponent } from '../../../../shared/crud-button/crud-button.component';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-member-calculation-table',
  imports: [GenericTableComponent,
    CrudButtonComponent,
    ScopedTranslationPipe],
  templateUrl: './member-calculation-table.component.html'
})
export class MemberCalculationTableComponent {
  private readonly memberService = inject(MemberService);

  memberId = input.required<number>();

  roleId = input<number>();

  private readonly calculationsRequest$ = toObservable(computed(() => ({
    memberId: this.memberId(),
    roleId: this.roleId()
  })));

  calculations = toSignal(this.calculationsRequest$.pipe(switchMap((params) => this.memberService.getCalculationsByMemberIdAndOptionalRoleId(params.memberId, params.roleId))), { initialValue: [] });

  calculationTable = computed(() => {
    const table = getCalculationTable();
    table.data = this.calculations();
    return table;
  });
}
