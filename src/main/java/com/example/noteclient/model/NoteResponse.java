package com.example.noteclient.model;

import java.time.LocalDateTime;
import java.util.List;

public class NoteResponse {

    private LocalDateTime timestamp;
    private boolean success;
    private String errors;
    private Note note;
    private List<Note> notes;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrors() {
        return errors;
    }

    public Note getNote() {
        return note;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

}
