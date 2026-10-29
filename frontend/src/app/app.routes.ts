import { Routes } from '@angular/router';
import { MemberFormComponent } from './features/member/form/member-form.component';
import { memberDataResolver } from './features/member/member-data-resolver';
import { memberOverviewResolver } from './features/member/overview/member-overview-resolver';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { provideI18nPrefix } from './shared/i18n-prefix.provider';
import { MemberDetailViewComponent } from './features/member/detail-view/member-detail-view.component';
import { tabResolver } from './features/member/detail-view/tab-resolver';
import { authGuard } from './core/auth/guard/auth.guard';
import { memberIdResolver } from './features/member/member-id-resolver';
import { CertificateOverviewComponent } from './features/certificates/certificate-overview/certificate-overview.component';
import { certificateTypeResolver } from './features/certificates/certificate-type.resolver';
import {
  CalculationDetailViewComponent
} from './features/calculations/calculation-detail-view/calculation-detail-view.component';
import {
  filterResolver
} from './features/certificates/certificate-overview/filter.resolver';
import { calculationResolver } from './features/calculations/calculation-resolver';
import { calculationTemplateResolver } from './features/calculations/calculation-template-resolver';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'member'
  },
  {
    path: 'member',
    providers: [provideI18nPrefix('MEMBER')],
    canActivate: [authGuard],
    children: [
      {
        path: '',
        component: MemberOverviewComponent,
        canActivate: [authGuard('admin')],
        resolve: { filters: memberOverviewResolver },
        providers: [provideI18nPrefix('OVERVIEW')]
      },
      {
        path: 'add',
        component: MemberFormComponent,
        canActivate: [authGuard('admin')],
        providers: [provideI18nPrefix('FORM.ADD')]
      },
      {
        path: ':id',
        canActivate: [authGuard('selfOrAdmin')],
        children: [{
          path: '',
          component: MemberDetailViewComponent,
          resolve:
              { memberId: memberIdResolver,
                tabIndex: tabResolver }
        },
        {
          path: 'calculation/:calculationId',
          providers: [provideI18nPrefix('CALCULATION')],
          component: CalculationDetailViewComponent,
          resolve: {
            memberId: memberIdResolver,
            calculation: calculationResolver
          }
        },
        {
          path: 'calculation/new/:roleId',
          providers: [provideI18nPrefix('CALCULATION')],
          component: CalculationDetailViewComponent,
          resolve: {
            calculation: calculationTemplateResolver
          }
        }]
      },
      {
        path: ':id/edit',
        component: MemberFormComponent,
        canActivate: [authGuard('admin')],
        resolve: { member: memberDataResolver },
        providers: [provideI18nPrefix('FORM.EDIT')]
      }
    ]
  },
  {
    path: 'certificate',
    providers: [provideI18nPrefix('CERTIFICATE.OVERVIEW')],
    component: CertificateOverviewComponent,
    canActivate: [authGuard('user')],
    runGuardsAndResolvers: 'paramsOrQueryParamsChange',
    resolve: {
      certificates: certificateTypeResolver,
      filter: filterResolver
    }
  },
  {
    path: '**',
    redirectTo: 'member'
  }
];
