import { Component, EventEmitter, input, Input, Output } from '@angular/core';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { MenuButtonComponent } from '../menu-button/menu-button.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-modal-actions',
  imports: [
    MatButtonModule,
    ScopedTranslationPipe,
    MenuButtonComponent,
    MatIcon
  ],
  templateUrl: './modal-actions.component.html'
})
export class ModalActionsComponent {
  @Input() isValid = false;

  @Input() cancelKey = 'BUTTONS.CANCEL';

  @Input() submitKey = 'BUTTONS.ACTION';

  submitModes = input.required<ModalSubmitMode[]>();

  @Input() deleteKey = 'BUTTONS.DELETE';

  @Output() cancelAction = new EventEmitter<void>();

  @Output() submitAction = new EventEmitter<ModalSubmitMode>();

  @Output() deleteAction = new EventEmitter<ModalSubmitMode>();

  public readonly ModalSubmitMode = ModalSubmitMode;

  onCancel(): void {
    this.cancelAction.emit();
  }

  onSubmit(mode: ModalSubmitMode): void {
    this.submitAction.emit(mode);
  }

  onDelete(mode: ModalSubmitMode): void {
    this.deleteAction.emit(mode);
  }

  hasDeleteSubmitMode() {
    return this.submitModes()
      .includes(ModalSubmitMode.DELETE);
  }

  menuButtonFunctions() {
    return this.submitModes()
      .filter((modelSubmitMode) => modelSubmitMode !== ModalSubmitMode.DELETE);
  }
}
