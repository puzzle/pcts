import { Component, effect, inject, Signal, signal, viewChild, WritableSignal } from '@angular/core';
import { MemberService } from '../member.service';
import { Member } from '../member.model';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { DatePipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatFabButton } from '@angular/material/button';
@Component({
  selector: 'app-member-overview.component',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    DatePipe,
    MatIcon,
    MatButton,
    MatFabButton
  ],
  templateUrl: './member-overview.component.html',
  styleUrl: './member-overview.component.css'
})
export class MemberOverviewComponent {
  private service = inject(MemberService);

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

  constructor() {
    this.service.getAllMembers()
      .subscribe((members: Member[]) => {
        this.members.set(members);
      });

    effect(() => {
      this.dataSource = new MatTableDataSource(this.members());
      this.dataSource.sort = this.sort();

      this.dataSource.filterPredicate = (member: Member, filter: string) => {
        if (filter === 'alle') {
          return true;
        }
        if (filter === 'member') {
          return member.employment_state?.toLowerCase() === 'member';
        }
        if (filter === 'bewerber') {
          return member.employment_state?.toLowerCase() === 'bewerber';
        }
        if (filter === 'member+bewerber') {
          return member.employment_state?.toLowerCase() === 'member' ||
            member.employment_state?.toLowerCase() === 'bewerber';
        }
        return true;
      };

      this.applyStatusFilter();
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim()
      .toLowerCase();
  }

  private applyStatusFilter() {
    if (this.alleSelected) {
      this.dataSource.filter = 'alle';
    } else if (this.memberSelected && this.bewerberSelected) {
      this.dataSource.filter = 'member+bewerber';
    } else if (this.memberSelected) {
      this.dataSource.filter = 'member';
    } else if (this.bewerberSelected) {
      this.dataSource.filter = 'bewerber';
    } else {
      this.dataSource.filter = 'none';
    }
  }

  toggleAlle() {
    this.alleSelected = !this.alleSelected;
    if (this.alleSelected) {
      this.memberSelected = false;
      this.bewerberSelected = false;
    }
    this.applyStatusFilter();
  }

  toggleMember() {
    this.memberSelected = !this.memberSelected;
    if (this.memberSelected) {
      this.alleSelected = false;
    }
    this.applyStatusFilter();
  }

  toggleBewerber() {
    this.bewerberSelected = !this.bewerberSelected;
    if (this.bewerberSelected) {
      this.alleSelected = false;
    }
    this.applyStatusFilter();
  }
}
