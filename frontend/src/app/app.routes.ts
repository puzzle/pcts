import { Routes } from '@angular/router';
import { MemberFormComponent } from './features/member/form/member-form.component';
import { memberDataResolver } from './features/member/member-data-resolver';
import { memberOverviewResolver } from './features/member/overview/member-overview-resolver';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { provideI18nPrefix } from './shared/i18n-prefix.provider';

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
    resolve: { filters: memberOverviewResolver },
    providers: [provideI18nPrefix('MEMBER.OVERVIEW')]
  },
  {
    path: 'add',
    component: MemberFormComponent,
    providers: [provideI18nPrefix('MEMBER.FORM.ADD')]
  },
  {
    path: ':id/edit',
    component: MemberFormComponent,
    resolve: { member: memberDataResolver },
    providers: [provideI18nPrefix('MEMBER.FORM.EDIT')]
  }]
}];
