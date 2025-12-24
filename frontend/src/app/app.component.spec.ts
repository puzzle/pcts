import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideRouter, Router } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { UserService } from './core/auth/user.service';

jest.mock('@puzzleitc/puzzle-shell', () => jest.fn());

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let router: Router;
  let userServiceMock: Partial<UserService>;

  beforeEach(async() => {
    userServiceMock = {
      getName: jest.fn()
        .mockReturnValue('Test User'),
      logout: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideTranslateService(),
        provideRouter([]),
        {
          provide: UserService,
          useValue: userServiceMock
        }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate')
      .mockImplementation(() => Promise.resolve(true));

    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should navigate to /member when visitRoot() is called', () => {
    component.visitRoot();

    expect(router.navigate)
      .toHaveBeenCalledWith(['/member']);
  });

  it('should call logout service when handleLogout() is called', () => {
    component.handleLogout();

    expect(userServiceMock.logout)
      .toHaveBeenCalled();
  });
});
