import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { MenuButtonComponent } from '../menu-button/menu-button.component';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-modal-actions',
  imports: [MatButtonModule,
    ScopedTranslationPipe,
    MenuButtonComponent],
  templateUrl: './modal-actions.component.html'
})
export class ModalActionsComponent {
  @Input() isValid = false;

  @Input() cancelKey = 'BUTTONS.CANCEL';

  @Input() submitKey = 'BUTTONS.ACTION';

  @Output() cancelAction = new EventEmitter<void>();

  @Output() submitAction = new EventEmitter<ModalSubmitMode>();

  public readonly ModalSubmitMode = ModalSubmitMode;

  onCancel(): void {
    this.cancelAction.emit();
  }

  onSubmit(mode: ModalSubmitMode): void {
    this.submitAction.emit(mode);
  }
}
