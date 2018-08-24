import {Component, OnInit} from '@angular/core';
import {ChatService} from '../chat.service';

@Component({
  selector: 'app-send-field',
  templateUrl: './send-field.component.html',
  styleUrls: ['./send-field.component.css']
})
export class SendFieldComponent implements OnInit {

  public content: string;

  constructor(private service: ChatService) {
  }

  ngOnInit() {
    this.content = '';
  }

  send() {
    switch (this.content) {
      case ':enter-cs':
        this.service.sendEnterCS();
        break;
      case ':exit-cs':
        this.service.sendExitCS();
        break;
      default:
        this.service.sendNewMessage(this.content);
    }
    this.content = '';
  }
}