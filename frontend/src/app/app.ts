import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import "@puzzleitc/puzzle-shell";

@Component({
  selector: 'app-root',
  imports: [RouterLink, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class App {
  protected title = 'pcts';
}

