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
    fixture.componentRef.setInput('submitModes', [ModalSubmitMode.SAVE]);
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

      component.handleCancel();

      expect(cancelSpy)
        .toHaveBeenCalledTimes(1);
    });

    it('The submitAction event should fire with the correct mode when onSubmit() is called', () => {
      const submitSpy = jest.spyOn(component.submitAction, 'emit');
      const testMode = ModalSubmitMode.SAVE;

      component.handleSubmit(testMode);

      expect(submitSpy)
        .toHaveBeenCalledTimes(1);
      expect(submitSpy)
        .toHaveBeenCalledWith(testMode);
    });

    it('should fire the correct mode when onDelete() is called', () => {
      const deleteSpy = jest.spyOn(component.deleteAction, 'emit');

      component.handleDelete();

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
