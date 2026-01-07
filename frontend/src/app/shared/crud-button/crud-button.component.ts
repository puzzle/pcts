import { Component, inject, input } from '@angular/core';
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

  // This logic gets the model name from the URL. It assumes that the URL structure is 'root/modelName/'
  protected modelName: string = this.router.url.split('/')[1] ?? '';

  // @Input() mode: 'add' | 'edit' | 'delete' = 'add';
  mode = input<'add' | 'edit' | 'delete'>('add');

  handleClick() {
    switch (this.mode()) {
      case 'edit':
        if (this.idFromRoute) {
          this.router.navigate([this.router.url,
            'edit']);
        }
        break;

      case 'delete':
        if (this.idFromRoute) {
          this.router.navigate([this.router.url,
            'delete']);
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

  get idFromRoute(): string | null {
    return this.route.snapshot.paramMap.get('id');
  }
}
