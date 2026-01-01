import { Directive, inject, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../auth.service';

@Directive({
  selector: '[appShowIfAdmin]'
})
export class ShowIfAdminDirective implements OnInit {
  private userService = inject(AuthService);

  private templateRef = inject(TemplateRef<any>);

  private viewContainer = inject(ViewContainerRef);

  ngOnInit(): void {
    if (this.userService.isAdmin()) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}
