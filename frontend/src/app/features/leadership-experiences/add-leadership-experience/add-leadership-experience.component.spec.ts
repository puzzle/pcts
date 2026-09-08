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

  describe('ngOnInit / Initialization', () => {
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

    it('should initialize empty form if no data is provided', () => {
      (component as any).data = undefined;
      component.formGroup.reset();

      component.ngOnInit();

      expect(component.formGroup.getRawValue().id)
        .toBeNull();
    });
  });

  describe('Form Validation', () => {
    it('should be invalid if leadershipExperienceType is null', () => {
      component.formGroup.controls.leadershipExperienceType.setValue(null);

      expect(component.formGroup.controls.leadershipExperienceType.hasError('required'))
        .toBeTruthy();
      expect(component.formGroup.valid)
        .toBeFalsy();
    });
  });

  describe('leadershipExperienceTypeFilteredOptions (Computed Signal)', () => {
    it('should sort by kind then by name, and group by kind', () => {
      component['leadershipExperienceTypeOptions'].set([leadershipExperienceType1,
        leadershipExperienceType2]);

      component.formGroup.controls.leadershipExperienceType.setValue('' as any);
      fixture.detectChanges();

      const groupedMap = component['leadershipExperienceTypeFilteredOptions']();

      expect(groupedMap.has(leadershipExperienceType1.leadershipExperienceKind))
        .toBeTruthy();
      expect(groupedMap.has(leadershipExperienceType2.leadershipExperienceKind))
        .toBeTruthy();

      const kindBArray = groupedMap.get(leadershipExperienceType2.leadershipExperienceKind)!;
      expect(kindBArray.length)
        .toBe(1);
      expect(kindBArray[0].name)
        .toBe('Officer');
    });

    it('should handle object value correctly (when option is selected)', () => {
      component['leadershipExperienceTypeOptions'].set([leadershipExperienceType1]);

      component.formGroup.controls.leadershipExperienceType.setValue(leadershipExperienceType1);
      fixture.detectChanges();

      const groupedMap = component['leadershipExperienceTypeFilteredOptions']();
      expect(groupedMap.has(leadershipExperienceType1.leadershipExperienceKind))
        .toBeTruthy();
      expect(groupedMap.get(leadershipExperienceType1.leadershipExperienceKind)![0].name)
        .toBe('Expert');
    });
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

    it('should close the dialog with form values and ENTER_ANOTHER mode', () => {
      component.formGroup.patchValue(leadershipExperience1);

      component.onSubmit(ModalSubmitMode.ENTER_ANOTHER);

      expect(dialogRefMock.close)
        .toHaveBeenCalledWith({
          modalSubmitMode: ModalSubmitMode.ENTER_ANOTHER,
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
        .toBe(leadershipExperienceType2.name);
    });

    it('should return an empty string if type is null/undefined', () => {
      expect(component['displayLeadershipExperienceTypes'](null as any))
        .toBe('');
      expect(component['displayLeadershipExperienceTypes'](undefined as any))
        .toBe('');
    });
  });
});
