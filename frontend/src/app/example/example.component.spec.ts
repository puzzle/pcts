import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExampleComponent } from './example.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TranslateModule } from '@ngx-translate/core';

describe('Example', () => {
  let component: ExampleComponent;
  let fixture: ComponentFixture<ExampleComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      providers: [provideHttpClient(),
        provideHttpClientTesting()],
      imports: [ExampleComponent,
        TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExampleComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
