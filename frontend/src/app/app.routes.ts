import { Routes } from '@angular/router';
import { ExampleComponent } from './features/example/example.component';
import { MemberOverviewComponent } from './features/member/overview/member-overview.component';

export const routes: Routes = [{
  path: 'example',
  component: ExampleComponent
},
{
  path: '',
  component: MemberOverviewComponent
}];
