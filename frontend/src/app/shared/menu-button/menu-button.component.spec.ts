import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MenuButtonComponent } from './menu-button.component';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { By } from '@angular/platform-browser';

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

  it('should emit selected value when a menu item is clicked', async() => {
    const emitSpy = jest.spyOn(component.selected, 'emit');

    const triggerButton = fixture.debugElement.query(By.css('button[matButton]'));
    expect(triggerButton)
      .toBeTruthy();
    triggerButton.nativeElement.click();
    fixture.detectChanges();

    await fixture.whenStable();

    const menuItems = fixture.debugElement.queryAll(By.css('button[mat-menu-item]'));
    expect(menuItems.length)
      .toBeGreaterThan(0);

    menuItems[0].nativeElement.click();

    expect(emitSpy)
      .toHaveBeenCalledWith(ModalSubmitMode.SAVE);
  });
});
