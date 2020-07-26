<template>
  <div>
    <div id="main-content" class="container">
      <div class="row">
        <div class="col-md-6">
          <form class="form-inline">
            <div class="form-group">
              <label for="connect">WebSocket connection:</label>
              <button id="connect" class="btn btn-default" type="submit" :disabled="connected == true" @click.prevent="connect">Connect</button>
              <button id="disconnect" class="btn btn-default" type="submit" :disabled="connected == false" @click.prevent="disconnect">Disconnect
              </button>
            </div>
          </form>
        </div>
        <div class="col-md-6">
          <form class="form-inline">
            <div class="form-group">
              <label for="name">What is your name?</label>
              <input type="text" id="name" class="form-control" v-model="send_message" placeholder="Your name here...">
            </div>
            <button id="send" class="btn btn-default" type="submit" @click.prevent="send">Send</button>
          </form>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <table id="conversation" class="table table-striped">
            <thead>
              <tr>
                <th>Greetings</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in received_messages" :key="item">
                <td>{{ item }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';


export default {

    name: "websocketdemo",
    queueId: "A01",
    token: "",
    socket: null,
    stompClient: null,
    data() {
        return {
            received_messages: [],
            send_message: null,
            connected: false
        };
    },

    methods: {
        setqueue(_quequeId) {
            this.queueId = _quequeId
        },
        send() {
            console.log("Send message:" + this.send_message);
            if (this.stompClient && this.stompClient.connected) {
              const msg = { name: this.send_message };
              console.log(JSON.stringify(msg));
              this.stompClient.send("/app/sendmsg", JSON.stringify(msg), {});
            }
        },
        connect() {
            this.setqueue("A01")
            const token = localStorage.getItem('accessToken')
            console.log
            if (!token) {
                console.log("stomp connect fail no token")
                return
            }
            console.log("stomp connect")
            const STOMP_URL=(process.env.NODE_ENV === 'production')?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL
            this.socket = new SockJS(STOMP_URL+"/any-socket?token="+token);
            this.stompClient = Stomp.over(this.socket);
            this.stompClient.connect({},
                frame => {
                this.connected = true;
                console.log(frame);
                this.stompClient.subscribe(`/queue/${this.queueId}/chat`, tick => {
                    console.log(tick.body);
                    var tickBoady = JSON.parse(tick.body)
                    var msgType = tickBoady.type
                    console.log(`msgType: ${msgType}`);
                    var msgPayload =JSON.parse(tickBoady.payload)
                    console.log(`msgPayload: ${msgPayload}`);
                    var lastMsg = msgPayload.lastMessage
                    console.log(`lastMsg: ${lastMsg}`);
                    var agentId = msgPayload.agentId
                    console.log(`agentId: ${agentId}`);
                    
                    


                    this.received_messages.push(JSON.parse(tick.body).content);
                });
              },
              error => {
                console.log(error);
                this.connected = false;
              }
            );
        },
        disconnect() {
            if (this.stompClient) {
              this.stompClient.disconnect();
            }
            this.connected = false;
        },
        tickleConnection() {
            this.connected ? this.disconnect() : this.connect();
        }
    },
    mounted() {
        this.connect()
    }


}
</script>
<style scoped="">
</style>