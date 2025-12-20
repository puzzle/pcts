import { Routes } from '@angular/router';
import { MemberFormComponent } from './features/member/form/member-form.component';
import { memberDataResolver } from './features/member/member-data-resolver';
import { memberOverviewResolver } from './features/member/overview/member-overview-resolver';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { provideI18nPrefix } from './shared/i18n-prefix.provider';
import { MemberDetailViewComponent } from './features/member/detail-view/member-detail-view.component';
import { authGuard } from './core/auth/auth.guard';
import { tabResolver } from './features/member/detail-view/tab-resolver';
import { authGuard } from './shared/guards/auth-guard';
import { canActivateAuthRole } from './shared/guards/auth-guard';
import { authGuard } from './core/auth/guard/auth.guard';

export const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  redirectTo: 'member'
},
{
  path: 'member',
  providers: [provideI18nPrefix('MEMBER')],
  canActivate: [canActivateAuthRole,authGuard],
  children: [
    {
      path: '',
      component: MemberOverviewComponent,
      canActivate: [authGuard({ scope: 'admin' })],
      resolve: { filters: memberOverviewResolver },
      providers: [provideI18nPrefix('OVERVIEW')]
    },
    {
      path: 'add',
      component: MemberFormComponent,
      canActivate: [authGuard({ scope: 'admin' })],
      providers: [provideI18nPrefix('FORM.ADD')]
    },
    {
      path: ':id',
      component: MemberDetailViewComponent,
      canActivate: [authGuard({ scope: 'user' })],
      resolve: { member: memberDataResolver }
    },
    {
      path: ':id/edit',
      component: MemberFormComponent,
      canActivate: [authGuard({ scope: 'admin' })],
      resolve: { member: memberDataResolver },
      providers: [provideI18nPrefix('FORM.EDIT')]
    },
    {
      path: ':id',
      component: MemberDetailViewComponent,
      canActivate: [authGuard({ scope: 'user' })],
      resolve:
        { member: memberDataResolver,
          tabIndex: tabResolver }
    }
  ]
},
{
  path: '**',
  redirectTo: 'member'
}];
