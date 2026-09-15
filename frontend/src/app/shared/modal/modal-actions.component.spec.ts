import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModalActionsComponent } from './modal-actions.component';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';

describe('ModalActionsComponent', () => {
  let component: ModalActionsComponent;
  let fixture: ComponentFixture<ModalActionsComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ModalActionsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ModalActionsComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('submitModes', [ModalSubmitMode.SAVE]);
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
      const mode = ModalSubmitMode.DELETE;

      component.onDelete(mode);

      expect(deleteSpy)
        .toHaveBeenCalledTimes(1);
      expect(deleteSpy)
        .toHaveBeenCalledWith(mode);
    });
  });

  describe('hasDeleteSubmitMode()', () => {
    it('should return true when DELETE is included in submitModes', () => {
      fixture.componentRef.setInput('submitModes', [ModalSubmitMode.SAVE,
        ModalSubmitMode.DELETE]);

      fixture.detectChanges();

      expect(component.hasDeleteSubmitMode())
        .toBe(true);
    });

    it('should return false when DELETE is not included in submitModes', () => {
      fixture.componentRef.setInput('submitModes', [ModalSubmitMode.SAVE]);

      fixture.detectChanges();

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
