import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import {TokenStorage} from "../help/TokenStorage";


const SERVER_URL = 'http://localhost:8080/ws'
const webSocket = new SockJS(SERVER_URL);
const stompClient = Stomp.over(webSocket);


export const WebSocketAPI = {

    _send: (student: any) => {
        const token = TokenStorage.getToken();
        console.log("calling logout api via web socket");
        stompClient.send('/app/send/hand/', {'Authorization': token}, JSON.stringify(student));
    },

    _disconnect: () => {
        if (stompClient !== null) {
            stompClient.disconnect(() => {
                console.log("Disconnected");
            });
        }
    }
}