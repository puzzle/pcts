import { Component, OnInit } from '@angular/core';
import { ExampleService } from './example.service';
import { Observable } from 'rxjs';
import { ExampleDto } from './dto/example.dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-example',
  imports: [CommonModule],
  templateUrl: './example.component.html',
  styleUrl: './example.component.css',
})
export class Example implements OnInit {
  examples$!: Observable<ExampleDto[]>;

  constructor(private service: ExampleService) {}

  ngOnInit(): void {
    this.examples$ = this.service.getAllExamples();
  }

  createNewExample(): void {
    this.service
      .createExample({
        text: 'Example: Another cool example!',
      })
      .subscribe({
        next: () => {
          this.examples$ = this.service.getAllExamples();
        },
      });
  }
}
