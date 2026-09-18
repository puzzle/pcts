import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModalActionsComponent } from './modal-actions.component';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { inputBinding } from '@angular/core';

describe('ModalActionsComponent', () => {
  let component: ModalActionsComponent;
  let fixture: ComponentFixture<ModalActionsComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ModalActionsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ModalActionsComponent, {
      bindings: [inputBinding('submitModes', () => [ModalSubmitMode.SAVE])]
    });
    fixture = TestBed.createComponent(ModalActionsComponent);
    fixture.componentRef.setInput('submitModes', []);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  describe('Outputs (Events)', () => {
    it('should trigger the cancel event when onCancel() is called', () => {
      const cancelSpy = jest.spyOn(component.cancelAction, 'emit');

      component.onCancel();

      expect(cancelSpy)
        .toHaveBeenCalledTimes(1);
    });

    it('The submitAction event should fire with the correct mode when onSubmit() is called', () => {
      const submitSpy = jest.spyOn(component.submitAction, 'emit');
      const testMode = ModalSubmitMode.SAVE;

      component.onSubmit(testMode);

      expect(submitSpy)
        .toHaveBeenCalledTimes(1);
      expect(submitSpy)
        .toHaveBeenCalledWith(testMode);
    });

    it('should fire the correct mode when onDelete() is called', () => {
      const deleteSpy = jest.spyOn(component.deleteAction, 'emit');

      component.deleteAction.emit();

      expect(deleteSpy)
        .toHaveBeenCalledTimes(1);
      expect(deleteSpy)
        .toHaveBeenCalled();
    });
  });

  describe('hasDeleteSubmitMode()', () => {
    it('should return true when DELETE is included in submitModes', () => {
      fixture.componentRef.setInput('submitModes', [ModalSubmitMode.DELETE]);
      fixture.detectChanges();

      expect(component.hasDeleteSubmitMode())
        .toBe(true);
    });

    it('should return false when DELETE is not included in submitModes', () => {
      fixture.componentRef.destroy();
      fixture = TestBed.createComponent(ModalActionsComponent, {
        bindings: [inputBinding('submitModes', () => [ModalSubmitMode.SAVE])]
      });

      fixture.detectChanges();

      component = fixture.componentInstance;

      expect(component.hasDeleteSubmitMode())
        .toBe(false);
    });
  });

  describe('menuButtonFunctions()', () => {
    it('should not include DELETE', () => {
      fixture.detectChanges();

      expect(component.menuButtonFunctions())
        .not.toContain(ModalSubmitMode.DELETE);
    });
  });
});
