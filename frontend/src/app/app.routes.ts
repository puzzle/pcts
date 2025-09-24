import { Routes } from '@angular/router';
import { MemberFormComponent } from './features/member/form/member-form.component';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { MemberOverviewResolver } from './features/member/overview/member-overview-resolver';

export const routes: Routes = [{
  path: '',
  component: MemberOverviewComponent,
  resolve: { filters: MemberOverviewResolver }
},
{
  path: 'member',
  children: [{ path: 'add',
    component: MemberFormComponent },
  { path: 'edit/:id',
    component: MemberFormComponent }]
}];
