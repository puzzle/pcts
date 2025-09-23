import { Component, effect, inject, Signal, signal, viewChild, WritableSignal } from '@angular/core';
import { MemberService } from '../member.service';
import { EmploymentState, Member } from '../member.model';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { DatePipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-member-overview.component',
  providers: [DatePipe],
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    DatePipe,
    MatIcon,
    MatButton
  ],
  templateUrl: './member-overview.component.html',
  styleUrl: './member-overview.component.css'
})
export class MemberOverviewComponent {
  private service: MemberService = inject(MemberService);

  private datePipe: DatePipe = inject(DatePipe);

  displayedColumns: string[] = [
    'name',
    'birthday',
    'organisation_unit',
    'status'
  ];

  dataSource = new MatTableDataSource<Member>();

  sort: Signal<MatSort> = viewChild.required(MatSort);

  members: WritableSignal<Member[]> = signal([]);

  alleSelected = true;

  memberSelected = false;

  bewerberSelected = false;

  private searchText = '';

  constructor() {
    this.service.getAllMembers()
      .subscribe((members: Member[]) => {
        this.members.set(members);
      });

    this.dataSource.filterPredicate = this.createFilterPredicate();

    effect(() => {
      this.dataSource.data = this.members();
      this.dataSource.sort = this.sort();
      this.applyCombinedFilter();
    });
  }


  createFilterPredicate(): (data: Member, filter: string) => boolean {
    return (member: Member, filter: string): boolean => {
      const filterValues = JSON.parse(filter);
      const searchTxt = filterValues.text.toLowerCase();
      const status = filterValues.status;

      let statusMatch = false;
      const employmentState = member.employmentState?.toLowerCase() || '';
      if (status === 'alle') {
        statusMatch = true;
      } else if (status === 'member' && employmentState === EmploymentState.ACTIVE) {
        statusMatch = true;
      } else if (status === 'bewerber' && employmentState === EmploymentState.APPLICANT) {
        statusMatch = true;
      } else if (status === 'member+bewerber' && (employmentState === EmploymentState.ACTIVE || employmentState === EmploymentState.APPLICANT)) {
        statusMatch = true;
      }

      const formattedBirthday = this.datePipe.transform(member.birthday, 'dd.MM.yyyy');

      const memberDataString = (
        member.name +
        member.lastName +
        formattedBirthday +
        member.organisationUnit +
        member.employmentState
      ).toLowerCase();

      console.log('memberDataString' + memberDataString);
      console.log('searchTxt' + searchTxt);

      const searchTerms: string[] = searchTxt.toLowerCase()
        .split(' ')
        .filter(Boolean);

      const isTextMatch: boolean = searchTerms.every((term) => memberDataString.includes(term));

      return isTextMatch && statusMatch;
    };
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.searchText = filterValue;
    this.applyCombinedFilter();
  }


  private applyCombinedFilter() {
    let statusFilterValue = 'none';

    if (this.alleSelected) {
      statusFilterValue = 'alle';
    } else if (this.memberSelected && this.bewerberSelected) {
      statusFilterValue = 'member+bewerber';
    } else if (this.memberSelected) {
      statusFilterValue = 'member';
    } else if (this.bewerberSelected) {
      statusFilterValue = 'bewerber';
    }

    const combinedFilter = {
      text: this.searchText,
      status: statusFilterValue
    };
    this.dataSource.filter = JSON.stringify(combinedFilter);
  }

  toggleAlle() {
    this.alleSelected = true;
    this.memberSelected = false;
    this.bewerberSelected = false;
    this.applyCombinedFilter();
  }

  toggleMember() {
    this.memberSelected = !this.memberSelected;
    this.alleSelected = false;
    if (!this.memberSelected && !this.bewerberSelected) {
      this.alleSelected = true;
    }
    this.applyCombinedFilter();
  }

  toggleBewerber() {
    this.bewerberSelected = !this.bewerberSelected;
    this.alleSelected = false;
    if (!this.memberSelected && !this.bewerberSelected) {
      this.alleSelected = true;
    }
    this.applyCombinedFilter();
  }
}
