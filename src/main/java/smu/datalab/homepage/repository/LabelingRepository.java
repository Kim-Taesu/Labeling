package smu.datalab.homepage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import smu.datalab.homepage.dto.Labeling;

import java.util.Optional;

public interface LabelingRepository extends JpaRepository<Labeling, Long> {
    Optional<Labeling> findFirstByOwnerAndEmotionIsNull(String owner);

    Long countAllByEmotionIsNullAndOwnerIsNull();

    Long countByOwnerAndEmotionIsNull(String owner);

    Long countByOwner(String owner);

    Page<Labeling> findAllByOwnerIsNull(Pageable pageable);
}
