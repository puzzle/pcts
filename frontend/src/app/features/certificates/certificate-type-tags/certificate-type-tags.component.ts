import { Component, input } from '@angular/core';

@Component({
  imports: [],
  standalone: true,
  selector: 'app-certificate-type-tags',
  styleUrl: './certificate-type-tags.component.scss',
  templateUrl: './certificate-type-tags.component.html'
})
export class CertificateTypeTagsComponent {
  tags = input.required<string[], string>({
    transform: (tags) => [...tags].sort((a, b) => a.localeCompare(b))
  });
}
