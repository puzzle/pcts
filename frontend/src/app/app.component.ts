import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import '@puzzleitc/puzzle-shell';

@Component({
  selector: 'app-root',
  imports: [RouterLink,
    RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppComponent {
  protected title = 'pcts';
}

