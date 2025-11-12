import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-detail-view',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './detail-view.component.html',
  styleUrl: './detail-view.component.scss'
})
export class DetailViewComponent {
  private readonly router = inject(Router);

  handleMemberDetailClick(): void {
    this.router.navigate(['/member/add']);
  }
}
