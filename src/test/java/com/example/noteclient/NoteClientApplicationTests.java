package com.example.noteclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.noteclient.model.Note;
import com.example.noteclient.service.ClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class NoteClientApplicationTests {

@Autowired
    private ClientService noteClient;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        note1 = new Note();
        note1.setText("First note");

        note2 = new Note();
        note2.setText("Second note");

        /* 
         * Delete all notes to refresh the server database
         * we could mock the API response, but to provide a "realistic" client experience,
         * calling the API directly is done instead
         */
        noteClient.deleteAllNotes().block();
    }

    @Test
    void testSaveNotes() {
        Mono<Note> savedNote1 = noteClient.createNote(note1);
        Mono<Note> savedNote2 = noteClient.createNote(note2);

        StepVerifier.create(savedNote1)
                .expectNextMatches(note -> note.getId() != null && note.getText().equals("First note"))
                .expectComplete()
                .verify();

        StepVerifier.create(savedNote2)
                .expectNextMatches(note -> note.getId() != null && note.getText().equals("Second note"))
                .expectComplete()
                .verify();

        System.out.println("Notes successfully saved");
    }

    @Test
    void testRetrieveAllNotesWhenEmpty() {
        Flux<Note> notes = noteClient.getAllNotes("UTC", "asc");
    
        StepVerifier.create(notes)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    
        System.out.println("Successfully verified that no notes were retrieved when DB is empty");
    }

    @Test
    void testCreateAndRetrieveAllNotes() {
        Mono<Note> savedNote1 = noteClient.createNote(note1);
        Mono<Note> savedNote2 = noteClient.createNote(note2);
    
        StepVerifier.create(savedNote1)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    
        StepVerifier.create(savedNote2)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    
        Flux<Note> notes = noteClient.getAllNotes("Asia/Tokyo", "ASC");
    
        StepVerifier.create(notes)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    
        System.out.println("Printing all notes in ascending order:");
        notes.collectList().block().forEach(note -> {
            System.out.println("----------------------------------");
            System.out.println("Note ID: " + note.getId());
            System.out.println("Text: " + note.getText());
            System.out.println("Timestamp: " + note.getTimestamp());
            System.out.println("----------------------------------");
        });
    
        System.out.println("Successfully fetched and printed all notes to console");
    }

    @Test
    void testCreateAndRetrieveAllNotesDescending() {
        Mono<Note> savedNote1 = noteClient.createNote(note1);
        Mono<Note> savedNote2 = noteClient.createNote(note2);
    
        StepVerifier.create(savedNote1)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    
        StepVerifier.create(savedNote2)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    
        Flux<Note> notes = noteClient.getAllNotes("Asia/Tokyo", "DESC");
    
        StepVerifier.create(notes)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    
        System.out.println("Printing all notes in descending order:");
        notes.collectList().block().forEach(note -> {
            System.out.println("----------------------------------");
            System.out.println("Note ID: " + note.getId());
            System.out.println("Text: " + note.getText());
            System.out.println("Timestamp: " + note.getTimestamp());
            System.out.println("----------------------------------");
        });
    
        System.out.println("Successfully fetched and printed all notes to console");
    }

}
