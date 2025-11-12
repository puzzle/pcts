import { Component, OnInit, WritableSignal, signal, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { GLOBAL_DATE_FORMAT } from '../../../shared/format/date-format';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-member-detail-view',
  standalone: true,
  providers: [DatePipe],
  imports: [
    CommonModule,
    TranslatePipe,
    DatePipe,
    ScopedTranslationPipe,
    MatButton,
    MatIcon
  ],
  templateUrl: './member-detail-view.component.html',
  styleUrls: ['./member-detail-view.component.scss']
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly translate = inject(TranslateService);

  private readonly datePipe: DatePipe = inject(DatePipe);

  protected readonly GLOBAL_DATE_FORMAT = GLOBAL_DATE_FORMAT;

  protected readonly member: WritableSignal<MemberModel | null> = signal<MemberModel | null>(null);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.router.navigate(['/member']);
      return;
    }

    this.service.getMemberById(Number(id))
      .subscribe({
        next: (member) => this.member.set(member),
        error: () => this.router.navigate(['/member'])
      });
  }

  handleBackClick(): void {
    this.router.navigate(['/member']);
  }

  handleEditClick(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.router.navigate(['/member',
        id,
        'edit']);
    }
  }
}
