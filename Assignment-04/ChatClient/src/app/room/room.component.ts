import {Component, Input, OnInit} from '@angular/core';
import {Room} from "../room";
import {ChatService} from "../chat.service";

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css']
})
export class RoomComponent implements OnInit {

  @Input()
  public room: Room;

  constructor(private service: ChatService) {
  }

  ngOnInit() {
  }

  setCurrentRoom() {
    this.service.sendLeaveRoom();
    this.service.room = this.room;
    this.service.sendJoinRoom();
  }

}
