import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddDegreeComponent } from './add-degree.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { degree1, degreeType1, degreeType2 } from '../../../shared/test/test-data';
import { provideTranslateService } from '@ngx-translate/core';
import { DegreeTypeService } from '../degree-type/degree-type.service';
import { of } from 'rxjs';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';

describe('AddDegreeComponent', () => {
  let component: AddDegreeComponent;
  let fixture: ComponentFixture<AddDegreeComponent>;

  const dialogData = degree1;
  const dialogRefMock = { close: jest.fn() };
  const degreeTypeServiceMock = {
    getAllDegreeTypes: jest.fn()
      .mockReturnValue(of([degreeType1,
        degreeType2]))
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddDegreeComponent],
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
          provide: DegreeTypeService,
          useValue: degreeTypeServiceMock
        },
        provideTranslateService()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddDegreeComponent);
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
    it('should load degreeTypes on init', () => {
      expect(component['degreeTypeOptions']())
        .toEqual([degreeType1,
          degreeType2]);
      expect(degreeTypeServiceMock.getAllDegreeTypes)
        .toHaveBeenCalled();
    });

    it('should get the value from getAllDegreeTypes and set them', () => {
      degreeTypeServiceMock.getAllDegreeTypes.mockReturnValue(of([degreeType2]));
      const degreeTypeOptionsSpy = jest.spyOn(component['degreeTypeOptions'], 'set');

      component.ngOnInit();

      expect(degreeTypeOptionsSpy)
        .toHaveBeenCalledWith([degreeType2]);
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
    it('should be invalid if type is missing', () => {
      component.formGroup.controls.type.setValue(null);
      expect(component.formGroup.controls.type.hasError('required'))
        .toBeTruthy();
      expect(component.formGroup.valid)
        .toBeFalsy();
    });
  });

  describe('degreeTypeFilteredOptions (Computed Signal)', () => {
    it('should handle string value correctly (user typing in autocomplete)', () => {
      component.formGroup.controls.type.setValue('Master' as any);
      fixture.detectChanges();

      const filtered = component['degreeTypeFilteredOptions']();
      expect(Array.isArray(filtered))
        .toBeTruthy();
    });

    it('should handle object value correctly (user selected an option)', () => {
      component.formGroup.controls.type.setValue(degreeType1);
      fixture.detectChanges();

      const filtered = component['degreeTypeFilteredOptions']();
      expect(Array.isArray(filtered))
        .toBeTruthy();
    });
  });

  describe('displayDegreeTypes', () => {
    it('should return the name of the type', () => {
      expect(component['displayDegreeTypes'](degreeType2))
        .toBe(degreeType2.name);
    });

    it('should return an empty string if type is null/undefined', () => {
      expect(component['displayDegreeTypes'](undefined as any))
        .toBe('');
      expect(component['displayDegreeTypes'](null))
        .toBe('');
    });
  });

  describe('Submit and Cancel', () => {
    it('should close the dialog with form values and SAVE mode', () => {
      component.formGroup.patchValue(degree1);

      component.onSubmit(ModalSubmitMode.SAVE);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.SAVE,
          submittedModel: component.formGroup.getRawValue()
        });
    });

    it('should close the dialog with form values and ENTER_ANOTHER mode', () => {
      component.formGroup.patchValue(degree1);

      component.onSubmit(ModalSubmitMode.ENTER_ANOTHER);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.ENTER_ANOTHER,
          submittedModel: component.formGroup.getRawValue()
        });
    });

    it('should close the dialog without saving the data on cancel', () => {
      component.onCancel();
      expect(dialogRefMock.close)
        .toHaveBeenCalledWith();
    });
  });
});
