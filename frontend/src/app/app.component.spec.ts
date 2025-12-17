import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideRouter } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { UserService } from './core/auth/user.service';

jest.mock('@puzzleitc/puzzle-shell', () => jest.fn());

describe('AppComponent', () => {
  beforeEach(async() => {
    const userServiceMock = {
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
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app)
      .toBeTruthy();
  });
});
