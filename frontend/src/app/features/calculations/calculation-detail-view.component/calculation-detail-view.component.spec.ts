import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationDetailViewComponent } from './calculation-detail-view.component';
import { MemberService } from '../../member/member.service';
import { calculation1 } from '../../../shared/test/test-data';
import { of } from 'rxjs';

describe('CalculationDetailViewComponent', () => {
  let component: CalculationDetailViewComponent;
  let fixture: ComponentFixture<CalculationDetailViewComponent>;

  let memberServiceMock: Partial<MemberService>;

  beforeEach(async() => {
    memberServiceMock = {
      getCalculationsByMemberIdAndOptionalRoleId: jest.fn()
        .mockReturnValue(of([calculation1]))
    };

    await TestBed.configureTestingModule({
      imports: [CalculationDetailViewComponent],
      providers: [{
        provide: MemberService,
        useValue: memberServiceMock
      }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculationDetailViewComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
