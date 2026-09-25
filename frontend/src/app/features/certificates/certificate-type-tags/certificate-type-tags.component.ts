import { Component, effect, input } from '@angular/core';

@Component({
  imports: [],
  standalone: true,
  selector: 'app-certificate-type-tags',
  styleUrl: './certificate-type-tags.component.scss',
  templateUrl: './certificate-type-tags.component.html'
})
export class CertificateTypeTagsComponent {
  tags = input.required<string[]>();

  sortedTags: string[] = [];

  constructor() {
    effect(() => {
      let sortedTags = this.tags();

      sortedTags = sortedTags.sort((a, b) => b.localeCompare(a));
      sortedTags.reverse();

      this.sortedTags = sortedTags;
    });
  }
}
