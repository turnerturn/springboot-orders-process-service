package cafe.shop.repository;

import cafe.shop.model.entities.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TerminalRepository extends JpaRepository<Terminal, UUID> {

    @Query("SELECT t FROM Terminal t LEFT JOIN FETCH t.queues WHERE t.id = :id")
    Optional<Terminal> findByIdWithQueues(@Param("id") UUID id);
}
