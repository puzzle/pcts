import { Routes } from '@angular/router';
import { ExampleComponent } from './example/example.component';
import { MemberFormComponent } from './features/member/form/member-form.component';

export const routes: Routes = [{ path: 'example',
  component: ExampleComponent },
{
  path: 'member',
  children: [{ path: 'add',
    component: MemberFormComponent },
  { path: 'edit/:id',
    component: MemberFormComponent }]
}];
