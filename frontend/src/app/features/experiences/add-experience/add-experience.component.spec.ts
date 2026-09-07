import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddExperienceComponent } from './add-experience.component';
import { experience1, experienceType1, experienceType2 } from '../../../shared/test/test-data';
import { of } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { provideTranslateService } from '@ngx-translate/core';
import { ExperienceTypeService } from '../experience-type/experience-type.service';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';

describe('AddExperienceComponent', () => {
  let component: AddExperienceComponent;
  let fixture: ComponentFixture<AddExperienceComponent>;

  const dialogData = experience1;
  const dialogRefMock = { close: jest.fn() };
  const experienceTypeServiceMock = {
    getAllExperienceTypes: jest.fn()
      .mockReturnValue(of([experienceType1,
        experienceType2]))
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddExperienceComponent],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: dialogData
        },
        {
          provide: MatDialogRef,
          useValue: dialogRefMock
        },
        {
          provide: ExperienceTypeService,
          useValue: experienceTypeServiceMock
        },
        provideTranslateService()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddExperienceComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  describe('Initialization', () => {
    it('should load ExperienceType on init', () => {
      expect(component['experienceTypeOptions']())
        .toEqual([experienceType1,
          experienceType2]);
      expect(experienceTypeServiceMock.getAllExperienceTypes)
        .toHaveBeenCalled();
    });

    it('should get the value from getAllExperienceTypes and set them', () => {
      experienceTypeServiceMock.getAllExperienceTypes.mockReturnValue(of([experienceType2]));
      const experienceTypeOptionsSpy = jest.spyOn(component['experienceTypeOptions'], 'set');

      component.ngOnInit();

      expect(experienceTypeOptionsSpy)
        .toHaveBeenCalledWith([experienceType2]);
    });

    it('should initialize empty form if no data is provided', () => {
      (component as any).data = undefined;
      component.formGroup.reset();

      component.ngOnInit();

      expect(component.formGroup.getRawValue().id)
        .toBeNull();
    });
  });

  describe('Form Validation', () => {
    it('should validate percent correctly (min, max, integer)', () => {
      const percentControl = component.formGroup.controls.percent;

      percentControl.setValue(-1);
      expect(percentControl.hasError('min'))
        .toBeTruthy();

      percentControl.setValue(121);
      expect(percentControl.hasError('max'))
        .toBeTruthy();

      percentControl.setValue(50.5);
      expect(percentControl.valid)
        .toBeFalsy();

      percentControl.setValue(100);
      expect(percentControl.valid)
        .toBeTruthy();
    });
  });

  describe('experienceTypeFilteredOptions (Computed Signal)', () => {
    it('should handle string value correctly (user typing in autocomplete)', () => {
      component.formGroup.controls.experienceType.setValue('Some Search' as any);
      fixture.detectChanges();

      const filtered = component['experienceTypeFilteredOptions']();
      expect(Array.isArray(filtered))
        .toBeTruthy();
    });

    it('should handle object value correctly (user selected an option)', () => {
      component.formGroup.controls.experienceType.setValue(experienceType1);
      fixture.detectChanges();

      const filtered = component['experienceTypeFilteredOptions']();
      expect(Array.isArray(filtered))
        .toBeTruthy();
    });
  });

  describe('displayExperienceTypes', () => {
    it('should return the name of the type', () => {
      expect(component['displayExperienceTypes'](experienceType2))
        .toBe(experienceType2.name);
    });

    it('should return an empty string if type is null/undefined', () => {
      expect(component['displayExperienceTypes'](undefined as any))
        .toBe('');
      expect(component['displayExperienceTypes'](null))
        .toBe('');
    });
  });

  describe('Submit and Cancel', () => {
    it('should close the dialog with form values and SAVE mode', () => {
      component.formGroup.patchValue(experience1);

      component.onSubmit(ModalSubmitMode.SAVE);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.SAVE,
          submittedModel: { ...experience1 }
        });
    });

    it('should close the dialog with form values and ENTER_ANOTHER mode', () => {
      component.formGroup.patchValue(experience1);

      component.onSubmit(ModalSubmitMode.ENTER_ANOTHER);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.ENTER_ANOTHER,
          submittedModel: { ...experience1 }
        });
    });

    it('should close the dialog without saving the data on cancel', () => {
      component.onCancel();
      expect(dialogRefMock.close)
        .toHaveBeenCalledWith();
    });
  });
});
