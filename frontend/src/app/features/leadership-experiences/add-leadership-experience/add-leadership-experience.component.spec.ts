import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { AddLeadershipExperienceComponent } from './add-leadership-experience.component';
import { LeadershipExperienceTypeService } from '../leadership-experiences-type/leadership-experience-type.service';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import {
  leadershipExperience1,
  leadershipExperienceType1,
  leadershipExperienceType2
} from '../../../shared/test/test-data';

describe('AddLeadershipExperienceComponent', () => {
  let component: AddLeadershipExperienceComponent;
  let fixture: ComponentFixture<AddLeadershipExperienceComponent>;

  const dialogRefMock = { close: jest.fn() };
  const leadershipExperienceTypeServiceMock = {
    getAllLeadershipExperienceTypes: jest.fn()
      .mockReturnValue(of([leadershipExperienceType1,
        leadershipExperienceType2]))
  };

  const dialogData = leadershipExperience1;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddLeadershipExperienceComponent],
      providers: [
        { provide: MAT_DIALOG_DATA,
          useValue: dialogData },
        { provide: MatDialogRef,
          useValue: dialogRefMock },
        { provide: LeadershipExperienceTypeService,
          useValue: leadershipExperienceTypeServiceMock },
        provideTranslateService()
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddLeadershipExperienceComponent);
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

  it('should load leadershipExperienceTypes on init', () => {
    expect(component['leadershipExperienceTypeOptions']())
      .toEqual([leadershipExperienceType1,
        leadershipExperienceType2]);
    expect(leadershipExperienceTypeServiceMock.getAllLeadershipExperienceTypes)
      .toHaveBeenCalledTimes(1);
  });

  it('should set formgroup value correctly from data provided in constructor', () => {
    expect(component.formGroup.getRawValue())
      .toEqual(leadershipExperience1);
  });

  describe('onSubmit', () => {
    it('should close the dialog with form values and selected mode', () => {
      component.formGroup.patchValue(leadershipExperience1);

      component.onSubmit(ModalSubmitMode.SAVE);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.SAVE,
          submittedModel: leadershipExperience1
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

  describe('displayLeadershipExperienceTypes', () => {
    it('should return the name of the type', () => {
      expect(component['displayLeadershipExperienceTypes'](leadershipExperienceType2))
        .toBe('Officer');
    });

    it('should return an empty string if type is null/undefined', () => {
      expect(component['displayLeadershipExperienceTypes'](null as any))
        .toBe('');
      expect(component['displayLeadershipExperienceTypes'](undefined as any))
        .toBe('');
    });
  });

  describe('filterLeadershipExperienceType', () => {
    [
      { ...leadershipExperienceType1,
        name: '' },
      '',
      null,
      undefined
    ].forEach((value) => {
      it(`should return default leadership experience types when value is ${JSON.stringify(value)}`, () => {
        const result = component.filterLeadershipExperienceType(value as any);
        expect(result)
          .toEqual([leadershipExperienceType1,
            leadershipExperienceType2]);
      });
    });

    it('should return filtered list based on string input', () => {
      const result = component.filterLeadershipExperienceType('Officer');
      expect(result)
        .toEqual([leadershipExperienceType2]);
    });
  });
});
