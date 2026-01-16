import { Component, OnInit, WritableSignal, signal, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { GLOBAL_DATE_FORMAT } from '../../../shared/format/date-format';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { AddCertificateComponent } from '../modal-components/add-certificate.component/add-certificate.component';
import { ModalService } from '../../../shared/modal-service';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { CertificateModel } from '../../certificates/certificate.model';
import { CertificateService } from '../../certificates/certificate.service';

@Component({
  selector: 'app-member-detail-view',
  standalone: true,
  providers: [DatePipe],
  imports: [
    CommonModule,
    DatePipe,
    ScopedTranslationPipe,
    CrudButtonComponent
  ],
  templateUrl: './member-detail-view.component.html'
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly dialog = inject(ModalService);

  private readonly certificateService = inject(CertificateService);

  protected readonly GLOBAL_DATE_FORMAT = GLOBAL_DATE_FORMAT;

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

  openCertificateDialog(model?: CertificateModel) {
    this.dialog.openModal(AddCertificateComponent, { data: { model } })
      .afterClosed()
      .subscribe((result: { modalSubmitMode: ModalSubmitMode;
        submittedModel: CertificateModel; }) => {
        switch (result.modalSubmitMode) {
          case ModalSubmitMode.ENTER_ANOTHER:

            break;
          case ModalSubmitMode.COPY:
            this.openCertificateDialog(result.submittedModel);
            break;
          default:
          result.modalSubmitMode satisfies never;
        }
      // this.certificateService.addCertificate(result.submittedModel);
      });
  }
}
