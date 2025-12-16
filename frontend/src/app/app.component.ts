import { Component, CUSTOM_ELEMENTS_SCHEMA, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import '@puzzleitc/puzzle-shell';
import { TranslatePipe } from '@ngx-translate/core';
import { NgOptimizedImage } from '@angular/common';
import { UserService } from './core/auth/user.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,
    TranslatePipe,
    NgOptimizedImage],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppComponent {
  protected title = 'pcts';

  private readonly router = inject(Router);

  private readonly userService = inject(UserService);

  protected userName: string | null;

  constructor() {
    this.userName = this.userService.getName();
  }

  protected visitRoot(): void {
    this.router.navigate(['/member']);
  }

  protected handleLogout() {
    this.userService.logout();
  }
}

