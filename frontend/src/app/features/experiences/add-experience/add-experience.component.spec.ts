import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddExperienceComponent } from './add-experience.component';
import { experience1, experienceType1, experienceType2 } from '../../../shared/test/test-data';
import { of } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { provideTranslateService } from '@ngx-translate/core';
import { ExperienceTypeService } from '../experience-type/experience-type.service';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';

describe('AddExperience', () => {
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

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should load ExperienceType', () => {
    expect(component['experienceTypeOptions']())
      .toEqual([experienceType1,
        experienceType2]);
    expect(experienceTypeServiceMock.getAllExperienceTypes)
      .toHaveBeenCalled();
  });

  describe('filter ExperienceType', () => {
    [{ ...experienceType1,
      name: '' },
    '',
    null].forEach((value) => {
      it(`should return default experiences when value is ${value}`, () => {
        const result = component.filterExperienceType(value);
        expect(result)
          .toEqual([experienceType1,
            experienceType2]);
      });
    });

    it('should return ', () => {
      const result = component.filterExperienceType('internship');
      expect(result)
        .toEqual([experienceType1]);
    });
  });

  it('should close the dialog with form values and save mode', () => {
    component.formGroup.patchValue(experience1);
    component.onSubmit(ModalSubmitMode.SAVE);
    expect(dialogRefMock.close)
      .toHaveBeenCalledWith({
        modalSubmitMode: ModalSubmitMode.SAVE,
        submittedModel: {
          ...experience1
        }
      });
  });

  it('should close the dialog without saving the data', () => {
    component.onCancel();
    expect(dialogRefMock.close)
      .toHaveBeenCalledWith();
  });

  it('should get the value from getAllExperienceTypes', () => {
    experienceTypeServiceMock.getAllExperienceTypes.mockReturnValue(of([experienceType2]));
    const experienceTypeOptionsSpy = jest.spyOn(component['experienceTypeOptions'], 'set');
    component.ngOnInit();
    expect(experienceTypeOptionsSpy)
      .toHaveBeenCalledWith([experienceType2]);
  });

  it('should return the name of the type', () => {
    expect(component['displayExperienceTypes'](experienceType2))
      .toBe('Work experience');
  });

  it('should return an empty string if type is null/undefined', () => {
    expect(component['displayExperienceTypes'](undefined as any))
      .toBe('');
  });
});
