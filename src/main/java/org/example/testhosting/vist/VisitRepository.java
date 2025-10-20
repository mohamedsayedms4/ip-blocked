package org.example.testhosting.vist;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    Page<Visit> findByDeviceIdAndDeletedFalseOrderByTimestampDesc(String deviceId, Pageable pageable);
}
