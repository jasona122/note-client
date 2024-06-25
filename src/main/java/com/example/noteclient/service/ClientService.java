package com.example.noteclient.service;

import com.example.noteclient.model.Note;
import com.example.noteclient.model.NoteResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientService {

    private final WebClient webClient;

    @Autowired
    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Note> createNote(Note note) {
        return webClient.post()
                .uri("/notes")
                .bodyValue(note)
                .retrieve()
                .bodyToMono(NoteResponse.class)
                .map(NoteResponse::getNote);
    }

    public Flux<Note> getAllNotes(String tz, String order) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/notes")
                        .queryParam("tz", tz)
                        .queryParam("order", order)
                        .build())
                .retrieve()
                .bodyToMono(NoteResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getNotes()));
    }

    public Mono<Note> getNoteById(Long id) {
        return webClient.get()
                .uri("/notes/{id}", id)
                .retrieve()
                .bodyToMono(NoteResponse.class)
                .map(NoteResponse::getNote);
    }

    public Mono<Boolean> deleteAllNotes() {
        return webClient.delete()
            .uri("/notes/deleteAll")
            .retrieve()
            .bodyToMono(NoteResponse.class)
            .map(NoteResponse::isSuccess);
    }
    
}