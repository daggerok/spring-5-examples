import { Component } from '@angular/core';
import { environment } from '../environments/environment';

interface MessageEvent {
  sessionId: string;
  body: string;
}

interface WebSocketData {
  data: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'web-socket app';
  messages: Array<MessageEvent> = [];

  constructor() {
    const ws = new WebSocket(`${environment.webSocketUrl}/ws/messages`);
    ws.onmessage = (e: WebSocketData) => {
      const data = JSON.parse(e.data) as MessageEvent;
      this.messages = [
        data,
        ...this.messages,
      ];
    };
  }
}
