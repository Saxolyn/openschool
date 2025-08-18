package com.openschool.common.file.command;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StreamFileCommandTest {

    @Test
    void builder_ShouldCreateCommandWithAllFields() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();
        long rangeStart = 100L;
        long rangeEnd = 199L;
        boolean supportPartialContent = true;
        String accessToken = "token123";

        // When
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(fileId)
                .requestedBy(requestedBy)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .supportPartialContent(supportPartialContent)
                .accessToken(accessToken)
                .build();

        // Then
        assertEquals(fileId, command.getFileId());
        assertEquals(requestedBy, command.getRequestedBy());
        assertEquals(rangeStart, command.getRangeStart());
        assertEquals(rangeEnd, command.getRangeEnd());
        assertTrue(command.isSupportPartialContent());
        assertEquals(accessToken, command.getAccessToken());
    }

    @Test
    void simpleConstructor_ShouldCreateCommandWithDefaults() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();

        // When
        StreamFileCommand command = new StreamFileCommand(fileId, requestedBy);

        // Then
        assertEquals(fileId, command.getFileId());
        assertEquals(requestedBy, command.getRequestedBy());
        assertEquals(0L, command.getRangeStart());
        assertEquals(-1L, command.getRangeEnd()); // -1 means end of file
        assertTrue(command.isSupportPartialContent());
        assertNull(command.getAccessToken());
    }

    @Test
    void hasRange_ShouldReturnTrue_WhenRangeStartGreaterThanZero() {
        // Given
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(UUID.randomUUID())
                .requestedBy(UUID.randomUUID())
                .rangeStart(100L)
                .rangeEnd(-1L)
                .build();

        // When
        boolean hasRange = command.hasRange();

        // Then
        assertTrue(hasRange);
    }

    @Test
    void hasRange_ShouldReturnTrue_WhenRangeEndGreaterThanZero() {
        // Given
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(UUID.randomUUID())
                .requestedBy(UUID.randomUUID())
                .rangeStart(0L)
                .rangeEnd(199L)
                .build();

        // When
        boolean hasRange = command.hasRange();

        // Then
        assertTrue(hasRange);
    }

    @Test
    void hasRange_ShouldReturnTrue_WhenBothRangeValuesGreaterThanZero() {
        // Given
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(UUID.randomUUID())
                .requestedBy(UUID.randomUUID())
                .rangeStart(100L)
                .rangeEnd(199L)
                .build();

        // When
        boolean hasRange = command.hasRange();

        // Then
        assertTrue(hasRange);
    }

    @Test
    void hasRange_ShouldReturnFalse_WhenNoRangeSpecified() {
        // Given
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(UUID.randomUUID())
                .requestedBy(UUID.randomUUID())
                .rangeStart(0L)
                .rangeEnd(-1L)
                .build();

        // When
        boolean hasRange = command.hasRange();

        // Then
        assertFalse(hasRange);
    }

    @Test
    void hasRange_ShouldReturnFalse_WhenDefaultConstructorUsed() {
        // Given
        StreamFileCommand command = new StreamFileCommand(UUID.randomUUID(), UUID.randomUUID());

        // When
        boolean hasRange = command.hasRange();

        // Then
        assertFalse(hasRange);
    }

    @Test
    void builder_ShouldHandleNullAccessToken() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();

        // When
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(fileId)
                .requestedBy(requestedBy)
                .accessToken(null)
                .build();

        // Then
        assertEquals(fileId, command.getFileId());
        assertEquals(requestedBy, command.getRequestedBy());
        assertNull(command.getAccessToken());
    }

    @Test
    void builder_ShouldHandleNegativeRangeValues() {
        // Given
        UUID fileId = UUID.randomUUID();
        UUID requestedBy = UUID.randomUUID();

        // When
        StreamFileCommand command = StreamFileCommand.builder()
                .fileId(fileId)
                .requestedBy(requestedBy)
                .rangeStart(-1L)
                .rangeEnd(-1L)
                .build();

        // Then
        assertEquals(-1L, command.getRangeStart());
        assertEquals(-1L, command.getRangeEnd());
        assertFalse(command.hasRange());
    }
}
