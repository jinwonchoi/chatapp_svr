import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import store from '@/store/store.js'

class StompService {
    queueId = "A01" //테스트용
    token = ""
    socket = null
    stompClient = null
    
    //테스트용
    setqueue(_quequeId) {
            this.queueId = _quequeId
    }
    send(_msg) {
        console.log("Send message:" + _msg);
        if (this.stompClient && this.stompClient.connected) {
            const msg = { name: _msg };
            console.log(JSON.stringify(msg));
            this.stompClient.send("/app/sendmsg", JSON.stringify(msg), {});
        }
    }

    //테스트용
    start(queueId) {
        this.queueId = queueId
        console.log("stomp start :" + this.queueId)
    }
    //테스트용
    end() {
        console.log("stomp end :" + this.queueId)
    }
    //테스트용
    call() {
        console.log("stomp call :" + this.queueId)
    }

    connect() {
        console.log("stomp connect")
        
        //this.setqueue("A01")
        const token = localStorage.getItem('accessToken')
        if (!token) {
            console.log("stomp connect fail no token")
            return
        }
        if (this.connected) {
            console.log("stomp already connected. stompClient:"+this.stompClient)
            return
        }
        const socUrl = ((process.env.NODE_ENV === 'production')?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL)+"/any-socket?token="+token
        console.log("calling socUrl:"+socUrl)
        this.socket = new SockJS(socUrl);
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.connect({},
            frame => {
                this.connected = true;
                console.log(frame);
/*
{"type":"MessageQueue.Insert",
  "sendDtm":"20200606225857148",
  "payload":"{"id":26,"customerId":"7253632591","chatId":6,"direction":"I","bizId":"A01","country":"kr","lang":"ko","status":null,"message":"It's worth noting that the keyHolder object will contain the auto-generated key return from th","createdDtm":"2020-06-07 07:58:57.130"}"}
{"type":"ChatSessionStatus.Update",
"sendDtm":"20200606225857145",
"payload":"{"id":1,"chatId":6,"customerId":"7253632591","agentId":"P060113","lastMessageId":26,"lastMessage":"It's worth noting that the keyHolder object will contain the auto-generated key return from th","unreadCnt":1,"direction":"I","bizId":"A01","country":"kr","lang":"ko","updatedDtm":"2020-05-31 21:39:43.677000000","createdDtm":"2020-05-04 10:21:20.838000000"}"}
 */
                //법인용
                const queueBiz = store.state.AppActiveUser.bizId
                this.stompClient.subscribe(`/queue/${queueBiz}/chat`, tick => {
                    console.log(tick.body);
                    var tickBoady = JSON.parse(tick.body)
                    var msgType = tickBoady.type
                    console.log(`msgType: ${msgType}`);
                    var msgPayload =JSON.parse(tickBoady.payload)
                    console.log(`msgPayload: ${msgPayload}`);
                    var lastMsg = msgPayload.lastMessage
                    console.log(`lastMsg: ${lastMsg}`);
                    if (msgType==='ChatSessionStatus.Insert') { // chat session add
                        const chatSession =JSON.parse(tickBoady.payload)
                        store.commit('chat/ADD_CHAT_SESSION', chatSession)
                    } else if (msgType==='ChatSessionStatus.Update') { // chat session update
                        const chatSession =JSON.parse(tickBoady.payload)
                        store.commit('chat/UPDATE_CHAT_SESSION', { 'chatSession':chatSession })
                    } else if (msgType==='ChatSessionStatus.Delete') { // chat session remove
                        const chatSessionId =JSON.parse(tickBoady.payload)
                        store.commit('chat/REMOVE_CHAT_SESSION', chatSessionId)
                    } else if (msgType==='MessageLog.Insert') {//message 수신
                        const message =JSON.parse(tickBoady.payload)
                        if (message.chatId) {
                            const payload = {
                                'chatData': store.getters['chat/chatMessagesOfChatId'](message.chatId),
                                'msg': message,
                                'chatId': message.chatId
                            }
                            store.commit('chat/ADD_CHAT_MESSAGE', payload)
                        } else {
                            //
                        }
                    }                     

                })
                //개인용
                const queueAgent = store.state.AppActiveUser.agentId
                this.stompClient.subscribe(`/queue/${queueAgent}/chat`, tick => {
                    console.log(tick.body);
                    var tickBoady = JSON.parse(tick.body)
                    var msgType = tickBoady.type
                    console.log(`msgType: ${msgType}`);
                    var msgPayload =JSON.parse(tickBoady.payload)
                    console.log(`msgPayload: ${msgPayload}`);
                    if (msgType==='MessageLog.Insert') {//message 수신
                        const message =JSON.parse(tickBoady.payload)
                        if (message.chatId) {
                            const payload = {
                                'chatData': store.getters['chat/chatMessagesOfChatId'](message.chatId),
                                'msg': message,
                                'chatId': message.chatId
                            }
                            store.commit('chat/ADD_CHAT_MESSAGE', payload)
                        } else {
                            //
                        }
                    }                     
                })

            },
            error => {
                console.log(error);
                this.connected = false;
            }
        )
    }
    disconnect() {
        console.log("stomp disconnect")
        if (this.stompClient) {
            this.stompClient.disconnect();
        }
        this.connected = false;
    }
    tickleConnection() {
        this.connected ? this.disconnect() : this.connect();
    }
}

export default new StompService()