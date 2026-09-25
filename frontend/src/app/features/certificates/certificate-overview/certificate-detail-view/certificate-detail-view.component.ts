import { Component, inject, input } from '@angular/core';
import { CertificateTypeService } from '../../certificate-type/certificate-type.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';

@Component({
  imports: [ScopedTranslationPipe],
  standalone: true,
  selector: 'app-certificate-detail-view',
  styleUrl: './certificate-detail-view.component.scss',
  templateUrl: './certificate-detail-view.component.html'
})
export class CertificateDetailViewComponent {
  private readonly certificateType = inject(CertificateTypeService);

  public readonly id = input.required<number>();

  certificate = toSignal(toObservable(this.id)
    .pipe(switchMap((currentId) => this.certificateType.getCertificateById(currentId))));
}
