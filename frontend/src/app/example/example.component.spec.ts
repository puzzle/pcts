import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ExampleComponent } from './example.component';
import { ExampleService } from './example.service';
import { ExampleDto } from './dto/example.dto';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TranslateModule } from '@ngx-translate/core';

describe('ExampleComponent (with Jest)', () => {
  let component: ExampleComponent;
  let fixture: ComponentFixture<ExampleComponent>;
  let mockService: jest.Mocked<ExampleService>;

  beforeEach(async() => {
    mockService = {
      getAllExamples: jest.fn()
        .mockReturnValue(of([])),
      getExampleById: jest.fn()
        .mockReturnValue(of({ id: 1,
          text: 'First example' } as ExampleDto)),
      createExample: jest.fn()
    } as unknown as jest.Mocked<ExampleService>;

    await TestBed.configureTestingModule({
      imports: [ExampleComponent,
        TranslateModule.forRoot()],
      providers: [provideHttpClient(),
        provideHttpClientTesting(),
        { provide: ExampleService,
          useValue: mockService }]
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

  it('should add a new example when createNewExample is called', () => {
    const newExample: ExampleDto = { id: 2,
      text: 'Another cool Example!' };
    mockService.createExample.mockReturnValue(of(newExample));

    component.createNewExample();

    expect(mockService.createExample)
      .toHaveBeenCalledWith({ text: 'Another cool Example!' });
    expect(component.examples())
      .toContainEqual(newExample);
  });
});
