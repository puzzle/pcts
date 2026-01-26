import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { AddCertificateComponent } from './add-certificate.component';
import { CertificateTypeService } from '../../../certificates/certificate-type/certificate-type.service';
import { ModalSubmitMode } from '../../../../shared/enum/modal-submit-mode.enum';
import { certificate1, certificateType1, certificateType2 } from '../../../../shared/test/test-data';

describe('AddCertificateComponent', () => {
  let component: AddCertificateComponent;
  let fixture: ComponentFixture<AddCertificateComponent>;

  const dialogRefMock = { close: jest.fn() };
  const certificateTypeServiceMock = {
    getAllCertificateTypes: jest.fn()
      .mockReturnValue(of([certificateType1,
        certificateType2]))
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddCertificateComponent],
      providers: [
        { provide: MAT_DIALOG_DATA,
          useValue: certificate1 },
        { provide: MatDialogRef,
          useValue: dialogRefMock },
        { provide: CertificateTypeService,
          useValue: certificateTypeServiceMock },
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

  it('should load certificateTypes', () => {
    expect(component['certificateTypeOptions']())
      .toEqual([certificateType1,
        certificateType2]);
    expect(certificateTypeServiceMock.getAllCertificateTypes)
      .toHaveBeenCalledTimes(1);
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
  });

  describe('onCancel', () => {
    it('should close modal without data', () => {
      component.onCancel();
      expect(dialogRefMock.close)
        .toHaveBeenCalledWith();
    });
  });
});
