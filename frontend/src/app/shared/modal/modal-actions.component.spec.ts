import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModalActionsComponent } from './modal-actions.component';
import {ModalSubmitMode} from '../enum/modal-submit-mode.enum';

describe('ModalActionsComponent', () => {
  let component: ModalActionsComponent;
  let fixture: ComponentFixture<ModalActionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalActionsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ModalActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Outputs (Events)', () => {

    it('should trigger the cancel event when onCancel() is called', () => {
      const cancelSpy = jest.spyOn(component.cancel, 'emit');

      component.onCancel();

      expect(cancelSpy).toHaveBeenCalledTimes(1);
    });

    it('The submitAction event should fire with the correct mode when onSubmit() is called', () => {
      const submitSpy = jest.spyOn(component.submitAction, 'emit');
      const testMode = ModalSubmitMode.SAVE;

      component.onSubmit(testMode);

      expect(submitSpy).toHaveBeenCalledTimes(1);
      expect(submitSpy).toHaveBeenCalledWith(testMode);
    });
  });
});
