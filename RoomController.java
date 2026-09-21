package com.dsu.helloserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Locale;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final List<Room> rooms = new ArrayList<>(List.of(
            new Room(1L, "Seminar A", 8),
            new Room(2L, "Study Pod", 4),
            new Room(3L, "Rooftop Room", 12)));

    private final AtomicLong nextId = new AtomicLong(4);

    @GetMapping
    public ResponseEntity<List<Room>> list(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(defaultValue = "") String keyword) {

        return ResponseEntity.ok(
                rooms.stream()
                        .filter(room -> minCapacity == null
                                || room.capacity() >= minCapacity)
                        .filter(room -> room.name().toLowerCase(Locale.ROOT)
                                .contains(keyword.toLowerCase(Locale.ROOT)))
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody RoomCreateRequest request) {
        Room room = new Room(
                nextId.getAndIncrement(),
                request.name(),
                request.capacity()
        );
        rooms.add(room);
        return ResponseEntity
                .created(URI.create("/api/rooms/" + room.id()))
                .body(room);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") Long id) {
        return rooms.stream()
                .filter(room -> room.id().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> replace(
            @PathVariable("id") Long id,
            @RequestBody RoomCreateRequest request) {

        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).id().equals(id)) {
                Room updated = new Room(
                        id, request.name(), request.capacity()
                );

                rooms.set(i, updated);
                return ResponseEntity.ok(updated);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean removed = rooms.removeIf(room -> room.id().equals(id));

        return removed
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }



}
