import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddDegreeComponent } from './add-degree.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { degree1, degreeType1, degreeType2 } from '../../../shared/test/test-data';
import { provideTranslateService } from '@ngx-translate/core';
import { DegreeTypeService } from '../degree-type/degree-type.service';
import { of } from 'rxjs';


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
});


