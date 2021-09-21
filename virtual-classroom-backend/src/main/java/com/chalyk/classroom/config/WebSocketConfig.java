package com.chalyk.classroom.config;

import com.chalyk.classroom.entity.Student;
import com.chalyk.classroom.security.AuthConstants;
import com.chalyk.classroom.security.AuthenticationToken;
import com.chalyk.classroom.security.JWTTokenProvider;
import com.chalyk.classroom.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JWTTokenProvider jwtTokenProvider;
    private final StudentService studentService;


    @Autowired
    public WebSocketConfig(JWTTokenProvider jwtTokenProvider, StudentService studentService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.studentService = studentService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/queue");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> tokenList = accessor.getNativeHeader(AuthConstants.HEADER_STRING.getTitle());
                    accessor.removeNativeHeader(AuthConstants.HEADER_STRING.getTitle());
                    String jwt = null;

                    if (tokenList != null && !tokenList.isEmpty()) {
                        jwt = tokenList.get(0);
                    }
                    Long accountId = jwtTokenProvider.getStudentIdFromToken(jwt.split(" ")[1]);
                    Student student = studentService.findStudentById(accountId);
                    AuthenticationToken authenticationToken = new AuthenticationToken(student);
                    accessor.setUser(authenticationToken);
                }
                return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
            }
        });
    }
}
