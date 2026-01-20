import { Component, input, InputSignal, computed, Signal } from '@angular/core';
import { ExperienceType } from '../../features/experiences/experience-type/experience-type.enum';

@Component({
  selector: 'app-experience-type-pill',
  templateUrl: './experience-type-pill.component.html',
  styleUrls: ['./experience-type-pill.component.scss']
})
export class ExperienceTypePillComponent {
  experienceType: InputSignal<string> = input.required();

  experienceBadgeClass: Signal<string> = computed(() => {
    switch (this.experienceType()) {
      case ExperienceType.INTERNSHIP:
        return 'badge-internship';
      case ExperienceType.WORK_EXPERIENCE:
        return 'badge-work-experience';
      case ExperienceType.WORK_PART_TIME:
        return 'badge-work-part-time';
      default:
        return 'badge-default';
    }
  });
}
