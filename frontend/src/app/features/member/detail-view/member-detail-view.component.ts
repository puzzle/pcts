import { Component, OnInit, WritableSignal, signal, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { NullFallbackPipe } from '../../../shared/pipes/null-fallback.pipe';
import { TranslationScope } from '../../../shared/directives/translation-scope';

@Component({
  selector: 'app-member-detail-view',
  standalone: true,
  providers: [DatePipe],
  imports: [
    CommonModule,
    DatePipe,
    ScopedTranslationPipe,
    CrudButtonComponent,
    NullFallbackPipe,
    TranslationScope
  ],
  templateUrl: './member-detail-view.component.html'
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  readonly member: WritableSignal<MemberModel | null> = signal<MemberModel | null>(null);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.router.navigate(['/member']);
      return;
    }

    this.service.getMemberById(Number(id))
      .subscribe({
        next: (member) => this.member.set(member)
      });
  }
}
