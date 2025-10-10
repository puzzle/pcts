import { Routes } from '@angular/router';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { MemberOverviewResolver } from './features/member/overview/member-overview-resolver';

export const routes: Routes = [{
  path: '',
  component: MemberOverviewComponent,
  resolve: { filters: MemberOverviewResolver }
}];
