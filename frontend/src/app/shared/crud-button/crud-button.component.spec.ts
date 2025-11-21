import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CrudButtonComponent } from './crud-button.component';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';

describe('CrudButtonComponent', () => {
  let component: CrudButtonComponent;
  let fixture: ComponentFixture<CrudButtonComponent>;
  let routerMock: Partial<Router>;
  let activatedRouteMock: any;

  const MOCK_ID = '12345';
  const MOCK_DEFAULT_URL = '/members';
  const MOCK_DEFAULT_URL_WITH_ID = `${MOCK_DEFAULT_URL}/${MOCK_ID}`;

  let currentUrlState = MOCK_DEFAULT_URL;

  beforeEach(async() => {
    currentUrlState = MOCK_DEFAULT_URL;

    routerMock = {
      navigate: jest.fn()
    };

    Object.defineProperty(routerMock, 'url', {
      get: () => currentUrlState
    });

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
      .toHaveBeenCalledWith([MOCK_DEFAULT_URL,
        'add']);
  });

  it('should navigate to the edit path when mode is edit', () => {
    currentUrlState = MOCK_DEFAULT_URL_WITH_ID;

    component.mode = 'edit';
    component.handleClick();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith([MOCK_DEFAULT_URL_WITH_ID,
        'edit']);
  });

  it('should navigate to the delete path when mode is delete', () => {
    currentUrlState = MOCK_DEFAULT_URL_WITH_ID;

    component.mode = 'delete';
    component.handleClick();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith([MOCK_DEFAULT_URL_WITH_ID,
        'delete']);
  });

  it('should correctly grab the ID from the route snapshot', () => {
    expect(component.idFromRoute)
      .toBe(MOCK_ID);
  });
});
