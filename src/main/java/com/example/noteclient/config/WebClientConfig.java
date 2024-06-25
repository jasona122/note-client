package com.example.noteclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class WebClientConfig {

    private final String serverURL = "http://localhost:8080";

    @Bean
    public WebClient webClient() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(formatter);

        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        mapper.registerModule(module);

        return WebClient.builder()
                .baseUrl(serverURL)
                .codecs(clientCodecConfigurer -> {
                    ClientCodecConfigurer.ClientDefaultCodecs codecs = clientCodecConfigurer.defaultCodecs();
                    codecs.jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
                })
                .build();
    }
    
}
