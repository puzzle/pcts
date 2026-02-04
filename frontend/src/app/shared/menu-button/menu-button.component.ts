import { Component, input, output } from '@angular/core';
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';

@Component({
  selector: 'app-menu-button',
  imports: [
    MatIcon,
    MatMenuTrigger,
    MatButton,
    MatMenu,
    MatMenuItem,
    ScopedTranslationPipe
  ],
  templateUrl: './menu-button.component.html'
})
export class MenuButtonComponent {
  selected = output<ModalSubmitMode>();

  isValid = input.required<boolean>();

  functions = input.required<ModalSubmitMode[]>();
}
