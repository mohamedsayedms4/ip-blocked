package org.example.testhosting.vist;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    private final VisitService service;

    public VisitController(VisitService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> createVisit(@RequestBody Visit visit) {
        Visit saved = service.saveVisit(visit);
        return ResponseEntity.ok().body(saved);
    }
}
