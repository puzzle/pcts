import { Routes } from '@angular/router';
import { MemberFormComponent } from './features/member/form/member-form.component';
import { memberDataResolver } from './features/member/member-data-resolver';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { MemberOverviewResolver } from './features/member/overview/member-overview-resolver';

export const routes: Routes = [{ path: '',
  component: MemberOverviewComponent,
  resolve: { filters: MemberOverviewResolver } },
{
  path: 'member',
  children: [{ path: 'add',
    component: MemberFormComponent },
  { path: ':id/edit',
    component: MemberFormComponent,
    resolve: {
      member: memberDataResolver
    } }]
}];
