import { Component, input, Input, output } from '@angular/core';
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

  cancelAction = output<void>();

  submitAction = output<ModalSubmitMode>();

  deleteAction = output<ModalSubmitMode>();

  public readonly ModalSubmitMode = ModalSubmitMode;

  hasDeleteSubmitMode() {
    return this.submitModes()
      .includes(ModalSubmitMode.DELETE);
  }

  menuButtonFunctions() {
    return this.submitModes()
      .filter((modelSubmitMode) => modelSubmitMode !== ModalSubmitMode.DELETE);
  }

  handleCancel() {
    this.cancelAction.emit();
  }

  handleSubmit(mode: ModalSubmitMode) {
    this.submitAction.emit(mode);
  }

  handleDelete() {
    this.deleteAction.emit(ModalSubmitMode.DELETE);
  }
}
