import { Component, input, InputSignal } from '@angular/core';

@Component({
  selector: 'app-experience-type-pill',
  imports: [],
  templateUrl: './experience-type-pill.component.html',
  styleUrl: './experience-type-pill.component.scss'
})
export class ExperienceTypePillComponent {
  experienceType: InputSignal<string> = input.required();

  getExperienceBadgeClass(type: string): string {
    switch (type) {
      case 'Praktikum':
        return 'badge-internship';
      case 'Berufserfahrung':
        return 'badge-work-experience';
      case 'Nebenerwerb':
        return 'badge-work-part-time';
      default:
        return 'badge-default';
    }
  }
}
