import { Component, signal } from '@angular/core';
//import { RouterOutlet } from '@angular/router';
import { ConversationalAnalytics } from './components/conversational-analytics/conversational-analytics';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ConversationalAnalytics],
  template: `
    <app-conversational-analytics>
    </app-conversational-analytics>
  `
})
export class App {
  //protected readonly title = signal('conversational-analytics-ui');
}
