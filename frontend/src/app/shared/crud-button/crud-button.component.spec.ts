import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudButtonComponent } from './crud-button.component';

describe('CrudButtonComponent', () => {
  let component: CrudButtonComponent;
  let fixture: ComponentFixture<CrudButtonComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CrudButtonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CrudButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
