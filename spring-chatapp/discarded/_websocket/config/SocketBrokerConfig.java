package com.gencode.issuetool._websocket.config;

import static com.gencode.issuetool._websocket.Constants.APP_DEST;
import static com.gencode.issuetool._websocket.Constants.DEST_PREFIX;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_GROUP;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_GROUP_USER;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_HISTORY;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_ROOM;
import static com.gencode.issuetool._websocket.Constants.SECURED_CHAT_SPECIFIC_USER;
import static com.gencode.issuetool._websocket.Constants.SECURED_MAPPING_TEST;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(SECURED_CHAT_HISTORY, SECURED_CHAT_SPECIFIC_USER
		 , SECURED_CHAT_GROUP_USER );
        config.setApplicationDestinationPrefixes(APP_DEST);
        config.setUserDestinationPrefix(DEST_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(SECURED_MAPPING_TEST).withSockJS();
        registry.addEndpoint(SECURED_CHAT_ROOM).withSockJS();
        registry.addEndpoint(SECURED_CHAT).withSockJS();
        registry.addEndpoint(SECURED_CHAT_GROUP).withSockJS();
    }

}


