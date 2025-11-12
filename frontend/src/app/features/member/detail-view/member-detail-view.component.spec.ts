import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberDetailViewComponent } from './member-detail-view.component';

describe('MemberDetailViewComponent', () => {
  let component: MemberDetailViewComponent;
  let fixture: ComponentFixture<MemberDetailViewComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [MemberDetailViewComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberDetailViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
