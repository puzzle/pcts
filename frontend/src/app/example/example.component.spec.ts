import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExampleComponent } from './example.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('Example', () => {
  let component: ExampleComponent;
  let fixture: ComponentFixture<ExampleComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      providers: [provideHttpClient(),
        provideHttpClientTesting()],
      imports: [ExampleComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExampleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
