import { Component, input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';

@Component({
  imports: [MatIcon,
    MatTooltip],
  selector: 'app-info-icon',
  templateUrl: './info-icon.component.html'
})
export class InfoIconComponent {
  message = input.required<string>();
}
