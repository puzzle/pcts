import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CrudButtonComponent } from './crud-button.component';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';

describe('CrudButtonComponent', () => {
  let component: CrudButtonComponent;
  let fixture: ComponentFixture<CrudButtonComponent>;
  let routerMock: Partial<Router>;
  let activatedRouteMock: any;

  const MOCK_CURRENT_URL = '/members/list';
  const MOCK_ID = '12345';

  beforeEach(async() => {
    routerMock = {
      navigate: jest.fn(),
      url: MOCK_CURRENT_URL
    };

    activatedRouteMock = {
      snapshot: {
        paramMap: convertToParamMap({ id: MOCK_ID })
      }
    };

    await TestBed.configureTestingModule({
      imports: [CrudButtonComponent],
      providers: [{ provide: Router,
        useValue: routerMock },
      { provide: ActivatedRoute,
        useValue: activatedRouteMock },
      provideTranslateService()]
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

  it('should navigate to the add path when mode is add', () => {
    component.mode = 'add';
    component.handleClick();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith([MOCK_CURRENT_URL,
        'add']);
  });

  it('should navigate to the edit path with ID when mode is edit', () => {
    component.mode = 'edit';
    component.handleClick();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith([MOCK_CURRENT_URL,
        'edit',
        MOCK_ID]);
  });

  it('should navigate to the delete path with ID when mode is delete', () => {
    component.mode = 'delete';
    component.handleClick();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith([MOCK_CURRENT_URL,
        'delete',
        MOCK_ID]);
  });

  it('should correctly grab the ID from the route snapshot', () => {
    expect(component.idFromRoute)
      .toBe(MOCK_ID);
  });
});
