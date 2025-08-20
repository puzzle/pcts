import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  /*
   *This is an example of a service, which should be shared over multiple different other components.
   */

  constructor() { }

  login(username: string, password: string) {
    // authentication logic here
  }
}
