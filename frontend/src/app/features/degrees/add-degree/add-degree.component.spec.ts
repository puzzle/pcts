import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddDegreeComponent } from './add-degree.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { degree1, degreeType1, degreeType2 } from '../../../shared/test/test-data';
import { provideTranslateService } from '@ngx-translate/core';
import { DegreeTypeService } from '../degree-type/degree-type.service';
import { of } from 'rxjs';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';

describe('AddDegree', () => {
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

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should filter DegreeType', () => {
    expect(component.filterDegreeType('bachelor', [degreeType2,
      degreeType1]))
      .toStrictEqual([degreeType1]);
  });

  it('should filter DegreeType', () => {
    /*
     * Arrange
     * - nothing
     */
    // Act
    const filteredTypes = component.filterDegreeType('', [degreeType2,
      degreeType1]);
    // Assert
    expect(filteredTypes)
      .toStrictEqual([degreeType2,
        degreeType1]);
  });

  it('should close the dialog with form values and save mode', () => {
    component.formGroup.patchValue(degree1);
    component.onSubmit(ModalSubmitMode.SAVE);
    expect(dialogRefMock.close)
      .toHaveBeenCalledWith({
        modalSubmitMode: ModalSubmitMode.SAVE,
        submittedModel: degree1
      });
  });

  it('should close the dialog without saving the data', () => {
    component.onCancel();
    expect(dialogRefMock.close)
      .toHaveBeenCalledWith();
  });

  it('should set the correct value from formGroup', () => {
    const formGroupPatchValueSpy = jest.spyOn(component.formGroup, 'patchValue');
    component.ngOnInit();
    expect(formGroupPatchValueSpy)
      .toHaveBeenCalled();
  });

  it('should load degreeTypes', () => {
    expect(component['degreeTypeOptions']())
      .toEqual([degreeType1,
        degreeType2]);
    expect(degreeTypeServiceMock.getAllDegreeTypes)
      .toHaveBeenCalled();
  });

  it('should set the value from degreeType', () => {
    expect(degreeTypeServiceMock.getAllDegreeTypes);
  });

  it('should get the value from getAllDegreeTypes', () => {
    degreeTypeServiceMock.getAllDegreeTypes.mockReturnValue(of([degreeType2]));
    const degreeTypeOptionsSpy = jest.spyOn(component['degreeTypeOptions'], 'set');
    component.ngOnInit();
    expect(degreeTypeOptionsSpy)
      .toHaveBeenCalledWith([degreeType2]);
  });

  it('should return the name of the type', () => {
    expect(component['displayDegreeTypes'](degreeType2))
      .toBe('Master');
  });

  it('should return an empty string if type is null/undefined', () => {
    expect(component['displayDegreeTypes'](undefined as any))
      .toBe('');
  });
});
