import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddDegreeComponent } from './add-degree.component';


describe('AddDegree', () => {
  let component: AddDegreeComponent;
  let fixture: ComponentFixture<AddDegreeComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddDegreeComponent]
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
});
