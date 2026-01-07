import { Component, input } from '@angular/core';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';

@Component({
  selector: 'app-generic-cv-content',
  imports: [
    ScopedTranslationPipe,
    MatButton,
    MatIcon,
    GenericTableComponent
  ],
  templateUrl: './generic-cv-content.component.html',
  styleUrl: './generic-cv-content.component.scss'
})
export class GenericCvContentComponent {
  title = input.required<string>();
}
