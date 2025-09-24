import { Routes } from '@angular/router';
import { ExampleComponent } from './example/example.component';
import { MemberFormComponent } from './features/member/form/member-form.component';
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
export const routes: Routes = [{ path: 'example',
  component: ExampleComponent },
{
  path: 'member',
  children: [{ path: 'add',
    component: MemberFormComponent },
  { path: 'edit/:id',
    component: MemberFormComponent }]
}];
