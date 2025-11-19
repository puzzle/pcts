import { Component, inject, Input } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-crud-button',
  imports: [MatButton,
    MatIcon,
    ScopedTranslationPipe],
  templateUrl: './crud-button.component.html'
})
export class CrudButtonComponent {
  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  private readonly id = this.idFromRoute;

  @Input() mode: 'add' | 'edit' | 'delete' = 'add';

  handleClick() {
    switch (this.mode) {
      case 'edit':
        if (this.id) {
          this.router.navigate([this.router.url,
            'edit',
            this.id]);
        }
        break;

      case 'delete':
        if (this.id) {
          this.router.navigate([this.router.url,
            'delete',
            this.id]);
        }
        break;

      case 'add':
        this.router.navigate([this.router.url,
          'add']);
        break;

      default:
        break;
    }
  }

  get idFromRoute() {
    return this.route.snapshot.paramMap.get('id');
  }
}
