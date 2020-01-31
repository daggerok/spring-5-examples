import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <h1>
      {{title}}
    </h1>
    <button mat-raised-button="">click me!</button>
    <router-outlet></router-outlet>
  `,
  styles: [],
})
export class AppComponent {
  title = 'app works!';

  constructor() {
    console.log('hey!');
  }
}
