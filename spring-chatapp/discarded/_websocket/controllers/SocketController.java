package com.gencode.issuetool._websocket.controllers;

import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_GROUP;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_GROUP2;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_GROUP_USER;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_GROUP_USER2;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_HISTORY;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_ROOM;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_SPECIFIC_USER;
import static com.gencode.issuetool._websocket.Constants.SECURED_MAPPING_TEST;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.gencode.issuetool._websocket.transfer.socket.Message;
import com.gencode.issuetool._websocket.transfer.socket.OutputMessage;
import com.gencode.issuetool.obj.GameState;
import com.gencode.issuetool.obj.Move;
import com.gencode.issuetool.obj.User;
import com.gencode.issuetool.service.GameService;
import com.gencode.issuetool.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final Logger log = LoggerFactory.getLogger(SocketController.class);
    
    @Autowired UserService userService;
    
    @Autowired
    GameService gameService;
//    public void setGameService(GameService gameService) {
//        this.gameService = gameService;
//    }
    
    @MessageMapping(SECURED_CHAT_GROUP)
    @SendTo(SECURED_CHAT_GROUP_USER)
    public GameState createGame(@DestinationVariable String uuid) {
        GameState gameState = gameService.createGame(UUID.fromString(uuid));

        return gameState;
    }

    @MessageMapping(SECURED_CHAT_GROUP2)
    @SendTo(SECURED_CHAT_GROUP_USER2)
    public GameState makeMove(@DestinationVariable String uuid, Move move) {
        GameState gameState = gameService.move(UUID.fromString(uuid), move);

        return gameState;
    }
    
    //SECURED_MAPPING_TEST
    @MessageMapping(SECURED_MAPPING_TEST)
    @SendTo(SECURED_CHAT_HISTORY)
    public void sendAll2(String msg) throws Exception {
    	System.out.println("OutputMessage sendAll2"+msg);
    }
    
    @MessageMapping(SECURED_CHAT)
    @SendTo(SECURED_CHAT_HISTORY)
    public OutputMessage sendAll(Message msg) throws Exception {
    	System.out.println("OutputMessage sendAll"+msg.getText());
    	log.info("OutputMessage sendAll"+msg.getText());
        OutputMessage out = new OutputMessage(msg.getFrom(), msg.getText(), new SimpleDateFormat("HH:mm").format(new Date()));
        return out;
    }

    /**
     * Example of sending message to specific user using 'convertAndSendToUser()' and '/queue'
     */
    @MessageMapping(SECURED_CHAT_ROOM)
    public void sendSpecific(@Payload Message msg, Principal user, @Header("simpSessionId") String sessionId) throws Exception {
    	log.info("sendSpecific"+msg.getText());
        OutputMessage out = new OutputMessage(msg.getFrom(), msg.getText(), new SimpleDateFormat("HH:mm").format(new Date()));

        simpMessagingTemplate.convertAndSendToUser(msg.getTo(), SECURED_CHAT_SPECIFIC_USER, out);
        
        Optional<User> returnUser = userService.findUser("admin");
        log.info(returnUser.toString());
    }
}
