import { Component, computed, inject, input } from '@angular/core';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { MemberService } from '../../member.service';
import { CrudButtonComponent } from '../../../../shared/crud-button/crud-button.component';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { rxResource } from '@angular/core/rxjs-interop';
import { GenericTableDataSourceService } from '../../../../shared/generic-table/generic-table-data-source.service';

@Component({
  selector: 'app-member-calculation-table',
  imports: [GenericTableComponent,
    CrudButtonComponent,
    ScopedTranslationPipe],
  templateUrl: './member-calculation-table.component.html'
})
export class MemberCalculationTableComponent {
  private readonly memberService = inject(MemberService);

  private readonly genericTableDataSourceService = inject(GenericTableDataSourceService);

  memberId = input.required<number>();

  roleId = input<number>();

  calculationsResource = rxResource({
    params: () => ({ memberId: this.memberId(),
      roleId: this.roleId() }),

    stream: ({ params }) => this.memberService.getCalculationsByMemberIdAndOptionalRoleId(params.memberId, params.roleId),

    defaultValue: []
  });

  calculationTable = computed(() => {
    const table = this.genericTableDataSourceService.getCalculationTable();
    table.data = this.calculationsResource.value();
    return table;
  });
}
