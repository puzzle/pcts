import { Routes } from '@angular/router';
import { ExampleComponent } from './features/example/example.component';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';
import { MemberOverviewResolver } from './features/member/overview/member-overview-resolver';

export const routes: Routes = [{
  path: 'example',
  component: ExampleComponent
},
{
  path: '',
  component: MemberOverviewComponent,
  resolve: { filters: MemberOverviewResolver }
}];
