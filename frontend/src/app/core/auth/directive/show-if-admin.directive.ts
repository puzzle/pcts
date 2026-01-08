import { Directive, inject, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../auth.service';

@Directive({
  selector: '[appShowIfAdmin]'
})
export class ShowIfAdminDirective implements OnInit {
  private readonly userService = inject(AuthService);

  private readonly templateRef = inject(TemplateRef<any>);

  private readonly viewContainer = inject(ViewContainerRef);

  ngOnInit(): void {
    if (this.userService.isAdmin()) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}
