package com.enesincekara.pulseops.incident.service;

import com.enesincekara.pulseops.incident.api.dto.IncidentCloseRequest;
import com.enesincekara.pulseops.incident.api.dto.IncidentUpdateRequest;
import com.enesincekara.pulseops.incident.api.exception.IncidentClosedException;
import com.enesincekara.pulseops.incident.api.exception.IncidentNotFoundException;
import com.enesincekara.pulseops.incident.api.exception.IncidentVersionMismatchException;
import com.enesincekara.pulseops.incident.domain.enums.IncidentSeverity;
import com.enesincekara.pulseops.incident.domain.enums.IncidentStatus;
import com.enesincekara.pulseops.incident.domain.model.Incident;
import com.enesincekara.pulseops.incident.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock IncidentRepository repo;
    @InjectMocks IncidentService service;

    private Incident incident(UUID id, long version, IncidentStatus status) {
        Incident i = new Incident();
        ReflectionTestUtils.setField(i, "id", id);
        ReflectionTestUtils.setField(i, "version", version);
        i.setTitle("t");
        i.setDescription("d");
        i.setSeverity(IncidentSeverity.LOW);
        i.setStatus(status);
        return i;
    }

    @Test
    void update_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(IncidentNotFoundException.class,
                () -> service.update(id, new IncidentUpdateRequest("x", null, null, 1L)));
    }

    @Test
    void update_versionMismatch_throws() {
        UUID id = UUID.randomUUID();
        Incident i = incident(id, 2L, IncidentStatus.OPEN);
        when(repo.findById(id)).thenReturn(Optional.of(i));

        assertThrows(IncidentVersionMismatchException.class,
                () -> service.update(id, new IncidentUpdateRequest("x", null, null, 1L)));
    }

    @Test
    void update_closed_throws() {
        UUID id = UUID.randomUUID();
        Incident i = incident(id, 1L, IncidentStatus.CLOSED);
        when(repo.findById(id)).thenReturn(Optional.of(i));

        assertThrows(IncidentClosedException.class,
                () -> service.update(id, new IncidentUpdateRequest("x", null, null, 1L)));
    }

    @Test
    void update_setsFields_andSaves() {
        UUID id = UUID.randomUUID();
        Incident i = incident(id, 1L, IncidentStatus.OPEN);
        when(repo.findById(id)).thenReturn(Optional.of(i));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Incident updated = service.update(id, new IncidentUpdateRequest("new", "new-desc", IncidentSeverity.HIGH, 1L));

        assertEquals("new", updated.getTitle());
        assertEquals("new-desc", updated.getDescription());
        assertEquals(IncidentSeverity.HIGH, updated.getSeverity());
        verify(repo).save(i);
    }

    @Test
    void close_success_closes_andSaves() {
        UUID id = UUID.randomUUID();
        Incident i = incident(id, 5L, IncidentStatus.OPEN);
        when(repo.findById(id)).thenReturn(Optional.of(i));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Incident closed = service.close(id, new IncidentCloseRequest(5L));

        assertEquals(IncidentStatus.CLOSED, closed.getStatus());
        verify(repo).save(i);
    }
}
