import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { AddCertificateComponent } from './add-certificate.component';
import { CertificateTypeService } from '../certificate-type/certificate-type.service';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { certificate1, certificateType1, certificateType2 } from '../../../shared/test/test-data';

describe('AddCertificateComponent', () => {
  let component: AddCertificateComponent;
  let fixture: ComponentFixture<AddCertificateComponent>;

  const dialogRefMock = { close: jest.fn() };
  const certificateTypeServiceMock = {
    getAllCertificateTypes: jest.fn()
      .mockReturnValue(of([certificateType1,
        certificateType2]))
  };

  const dialogData = certificate1;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddCertificateComponent],
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
          provide: CertificateTypeService,
          useValue: certificateTypeServiceMock
        },
        provideTranslateService()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  describe('ngOnInit / Initialization', () => {
    it('should load certificateTypes', () => {
      expect(component['certificateTypeOptions']())
        .toEqual([certificateType1,
          certificateType2]);
      expect(certificateTypeServiceMock.getAllCertificateTypes)
        .toHaveBeenCalledTimes(1);
    });

    it('should set formgroup value correctly from data', () => {
      expect(component.formGroup.getRawValue())
        .toEqual(certificate1);
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
    it('should be invalid if certificateType is null', () => {
      component.formGroup.controls.certificateType.setValue(null);

      expect(component.formGroup.controls.certificateType.hasError('required'))
        .toBeTruthy();
      expect(component.formGroup.valid)
        .toBeFalsy();
    });

    it('should be invalid if completedAt is null', () => {
      component.formGroup.controls.completedAt.setValue(null);

      expect(component.formGroup.controls.completedAt.hasError('required'))
        .toBeTruthy();
      expect(component.formGroup.valid)
        .toBeFalsy();
    });
  });

  describe('displayCertificateTypes', () => {
    it('should return certificate type name if object is provided', () => {
      expect(component['displayCertificateTypes'](certificateType1))
        .toBe('GitLab & AWS Certificate');
    });

    it('should return empty string if certificate type is null or has no name', () => {
      expect(component['displayCertificateTypes'](null as any))
        .toBe('');
      expect(component['displayCertificateTypes']({} as any))
        .toBe('');
    });
  });

  describe('certificateTypeFilteredOptions (Computed Signal)', () => {
    it('should handle string value (user typing in autocomplete)', () => {
      component.formGroup.controls.certificateType.setValue('Some Search String' as any);
      fixture.detectChanges();

      const filtered = component['certificateTypeFilteredOptions']();
      expect(Array.isArray(filtered))
        .toBeTruthy();
    });

    it('should handle object value (user selected an option)', () => {
      component.formGroup.controls.certificateType.setValue(certificateType1);
      fixture.detectChanges();

      const filtered = component['certificateTypeFilteredOptions']();
      expect(Array.isArray(filtered))
        .toBeTruthy();
    });
  });

  describe('onSubmit', () => {
    it('should close the dialog with form values and SAVE mode', () => {
      component.formGroup.patchValue(certificate1);

      component.onSubmit(ModalSubmitMode.SAVE);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.SAVE,
          submittedModel: certificate1
        });
    });

    it('should close the dialog with form values and ENTER_ANOTHER mode', () => {
      component.formGroup.patchValue(certificate1);

      component.onSubmit(ModalSubmitMode.ENTER_ANOTHER);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.ENTER_ANOTHER,
          submittedModel: certificate1
        });
    });
  });

  describe('onCancel', () => {
    it('should close modal without data', () => {
      component.onCancel();
      expect(dialogRefMock.close)
        .toHaveBeenCalledWith();
    });
  });
});
