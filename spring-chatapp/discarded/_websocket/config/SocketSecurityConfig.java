package com.gencode.issuetool._websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected boolean sameOriginDisabled() {
    	return true;
    }

    /**
     * 보안이 필요한 websocket dest mapping을 적용.
     * authenticated()를 적용하면 JwtTokenProvider가 적용된다.
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
        .simpDestMatchers("/secured/**", "/secured/**/**").permitAll()
        .anyMessage().permitAll();
//                .simpDestMatchers("/secured/**", "/secured/**/**").authenticated()
//                .anyMessage().authenticated();
    }
}