import { Routes } from '@angular/router';
import { MemberFormComponent } from './features/member/form/member-form.component';
import { memberDataResolver } from './features/member/member-data-resolver';
import { memberOverviewResolver } from './features/member/overview/member-overview-resolver';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';

export const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  redirectTo: 'member'
},
{
  path: 'member',
  children: [{
    path: '',
    component: MemberOverviewComponent,
    resolve: { filters: memberOverviewResolver }
  },
  {
    path: 'add',
    component: MemberFormComponent
  },
  {
    path: ':id/edit',
    component: MemberFormComponent,
    resolve: { member: memberDataResolver }
  }]
}];
