import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class CaseFormatter {
  camelToSnake(camelString: string) {
    return camelString.replace(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`)
      .toUpperCase();
  }
}
