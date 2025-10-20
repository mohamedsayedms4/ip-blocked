package org.example.testhosting.vist;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {
    private final VisitRepository repo;

    public VisitService(VisitRepository repo) { this.repo = repo; }

    @Transactional
    public Visit saveVisit(Visit visit) {
        return repo.save(visit);
    }
}
