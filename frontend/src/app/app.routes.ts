import { Routes } from '@angular/router';
import { MemberFormComponent } from './features/member/form/member-form.component';
import { memberDataResolver } from './features/member/member-data-resolver';
import { memberOverviewResolver } from './features/member/overview/member-overview-resolver';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { provideI18nPrefix } from './shared/i18n-prefix.provider';
import { MemberDetailViewComponent } from './features/member/detail-view/member-detail-view.component';

export const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  redirectTo: 'member'
},
{
  path: 'member',
  providers: [provideI18nPrefix('MEMBER')],
  children: [
    {
      path: '',
      component: MemberOverviewComponent,
      resolve: { filters: memberOverviewResolver },
      providers: [provideI18nPrefix('OVERVIEW')]
    },
    {
      path: 'add',
      component: MemberFormComponent,
      providers: [provideI18nPrefix('FORM.ADD')]
    },
    {
      path: ':id/edit',
      component: MemberFormComponent,
      resolve: { member: memberDataResolver },
      providers: [provideI18nPrefix('FORM.EDIT')]
    },
    {
      path: ':id',
      component: MemberDetailViewComponent,
      resolve: { member: memberDataResolver }
    }
  ]
}];
