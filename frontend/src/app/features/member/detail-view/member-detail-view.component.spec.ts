import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberDetailViewComponent } from './member-detail-view.component';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { member1, member2 } from '../../../shared/test/test-data';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';

describe('MemberDetailViewComponent', () => {
  let component: MemberDetailViewComponent;
  let fixture: ComponentFixture<MemberDetailViewComponent>;
  let memberServiceMock: Partial<MemberService>;

  const membersMock: MemberModel[] = [member1,
    member2];

  beforeEach(async() => {
    memberServiceMock = {
      getAllMembers: jest.fn()
        .mockReturnValue(of(membersMock))
    };

    await TestBed.configureTestingModule({
      imports: [MemberDetailViewComponent],
      providers: [
        provideRouter([{ path: 'member',
          component: MemberDetailViewComponent },
        { path: 'member/:id',
          component: MemberDetailViewComponent }]),
        provideTranslateService(),
        { provide: MemberService,
          useValue: memberServiceMock },
        DatePipe
      ]
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
