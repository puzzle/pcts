import { Component, CUSTOM_ELEMENTS_SCHEMA, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import '@puzzleitc/puzzle-shell';
import { TranslatePipe } from '@ngx-translate/core';
import { NgOptimizedImage } from '@angular/common';

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

  protected visitRoot(): void {
    this.router.navigate(['/member']);
  }
}

