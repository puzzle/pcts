import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import "@puzzleitc/puzzle-shell";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class App {
  protected title = 'pcts';
}

