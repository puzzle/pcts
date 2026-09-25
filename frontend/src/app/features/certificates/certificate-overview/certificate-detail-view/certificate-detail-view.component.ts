import { Component, input } from '@angular/core';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { CertificateTypeModel } from '../../certificate-type/certificate-type.model';

@Component({
  imports: [ScopedTranslationPipe],
  standalone: true,
  selector: 'app-certificate-detail-view',
  styleUrl: './certificate-detail-view.component.scss',
  templateUrl: './certificate-detail-view.component.html'
})
export class CertificateDetailViewComponent {
  public readonly certificate = input.required<CertificateTypeModel>();
}
