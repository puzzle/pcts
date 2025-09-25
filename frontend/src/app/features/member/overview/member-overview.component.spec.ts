import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MemberOverviewComponent } from './member-overview.component';
import { provideRouter } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MemberModel } from '../member.model';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { of } from 'rxjs';
import { DatePipe } from '@angular/common';
import { MemberService } from '../member.service';

describe('MemberOverviewComponent', () => {
  let component: MemberOverviewComponent;
  let fixture: ComponentFixture<MemberOverviewComponent>;
  let memberServiceMock: Partial<MemberService>;

  const membersMock: MemberModel[] = [{ id: 1,
    name: 'Ja',
    lastName: 'Morant',
    birthday: new Date(2018, 0, 1),
    abbreviation: 'JM',
    employmentState: EmploymentState.MEMBER,
    organisationUnit: { id: 1,
      name: '/mem' },
    dateOfHire: new Date(2019, 0, 1) },
  { id: 2,
    name: 'Bane',
    lastName: 'Desmond',
    birthday: new Date(2016, 5, 15),
    abbreviation: 'BD',
    employmentState: EmploymentState.EXMEMBER,
    organisationUnit: { id: 2,
      name: '/ww' },
    dateOfHire: new Date(2017, 3, 1) }];

  beforeEach(async() => {
    memberServiceMock = {
      getAllMembers: jest.fn()
        .mockReturnValue(of(membersMock))
    };

    await TestBed.configureTestingModule({
      imports: [MemberOverviewComponent,
        TranslateModule.forRoot()],
      providers: [provideRouter([]),
        { provide: MemberService,
          useValue: memberServiceMock },
        DatePipe]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
    expect(component.members())
      .toEqual(membersMock);
  });

  describe('applyFilter', () => {
    it.each([['Bane'],
      ['ja Morant'],
      ['']])('should update searchText to "%s" and apply filter', (searchText) => {
      const spy = jest.spyOn(component as any, 'applyCombinedFilter');
      component.applyFilter(searchText);
      expect(component.searchText)
        .toBe(searchText);
      expect(spy)
        .toHaveBeenCalled();
    });
  });

  describe('toggleFilter', () => {
    it.each([EmploymentState.MEMBER,
      EmploymentState.EXMEMBER,
      EmploymentState.APPLICANT])('should toggle filter %s', (status) => {
      component.toggleFilter(status);
      expect(component.isFilterActive(status))
        .toBe(true);

      component.toggleFilter(status);
      expect(component.isFilterActive(status))
        .toBe(false);
    });

    it('should clear all filters when ALL is active', () => {
      component.toggleFilter(EmploymentState.MEMBER);
      component.toggleFilter(EmploymentState.EXMEMBER);
      component.toggleFilter(EmploymentState.APPLICANT);

      component.activeFilters.clear();
      component.applyCombinedFilter();

      expect(component.activeFilters.size)
        .toBe(0);
    });
  });

  describe('isAllFilterActive', () => {
    it('returns true if no filters active', () => {
      component.activeFilters.clear();
      expect(component.isAllFilterActive())
        .toBe(true);
    });

    it('returns true if all filters active', () => {
      component.employmentStateValues.forEach((s) => component.activeFilters.add(s));
      expect(component.isAllFilterActive())
        .toBe(true);
      expect(component.activeFilters.size)
        .toBe(0);
    });

    it('returns false if only some filters active', () => {
      component.activeFilters.add(EmploymentState.MEMBER);
      expect(component.isAllFilterActive())
        .toBe(false);
    });
  });

  describe('createFilterPredicate', () => {
    const testCases = [
      { searchText: 'ja',
        status: EmploymentState.EXMEMBER,
        expected: false },
      { searchText: 'Morant',
        status: '',
        expected: true },
      { searchText: 'Bane',
        status: EmploymentState.MEMBER,
        expected: false },
      { searchText: 'NoMember',
        status: '',
        expected: false }
    ];

    it.each(testCases)('should filter member correctly for %#', ({ searchText, status, expected }) => {
      const predicate = component.createFilterPredicate();
      const member: MemberModel = membersMock[0];

      const filter = JSON.stringify({ text: searchText,
        status });
      expect(predicate(member, filter))
        .toBe(expected);
    });
  });
});
