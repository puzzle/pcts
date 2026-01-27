import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MenuButtonComponent } from './menu-button.component';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';

describe('MenuButtonComponent', () => {
  let component: MenuButtonComponent;
  let fixture: ComponentFixture<MenuButtonComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [MenuButtonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MenuButtonComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('isValid', true);
    fixture.componentRef.setInput('functions', [ModalSubmitMode.SAVE]);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
