import { Directive, inject, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { UserService } from '../user.service';

@Directive({
  selector: '[appShowIfAdmin]'
})
export class ShowIfAdminDirective implements OnInit {
  private userService = inject(UserService);

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
