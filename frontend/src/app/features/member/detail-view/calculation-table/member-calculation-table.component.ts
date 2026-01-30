import { Component, computed, effect, inject, input } from '@angular/core';
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
  private readonly memberService: MemberService = inject(MemberService);

  private calculationsRequest$ = computed(() => ({
    memberId: this.memberId(),
    roleId: this.roleId()
  }));

  memberId = input.required<number>();

  roleId = input<number>();

  calculations = toSignal(toObservable(this.calculationsRequest$)
    .pipe(switchMap((params) => this.memberService.getCalculationsByMemberIdAndOptionalRoleId(params.memberId, params.roleId))), { initialValue: [] });

  calculationTable = getCalculationTable();

  constructor() {
    effect(() => {
      this.calculationTable.data = this.calculations();
    });
  }
}
