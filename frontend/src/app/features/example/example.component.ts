import { Component, computed, inject, signal, WritableSignal } from '@angular/core';
import { ExampleService } from './example.service';
import { ExampleDto } from './dto/example.dto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-example',
  imports: [CommonModule,
    FormsModule,
    TranslatePipe],
  templateUrl: './example.component.html',
  styleUrl: './example.component.css'
})
export class ExampleComponent {
  private service = inject(ExampleService);

  exampleId = signal<number>(1);

  example = computed(() => this.service.getExampleById(this.exampleId()));

  examples: WritableSignal<ExampleDto[]> = signal([]);

  constructor() {
    this.service.getAllExamples()
      .subscribe((examples: ExampleDto[]) => {
        this.examples.set(examples);
      });
  }

  createNewExample(): void {
    const example = {
      text: 'Another cool Example!'
    };
    this.service.createExample(example)
      .subscribe((example: ExampleDto) => {
        this.examples.update((examples) => examples.concat(example));
      });
  }
}
