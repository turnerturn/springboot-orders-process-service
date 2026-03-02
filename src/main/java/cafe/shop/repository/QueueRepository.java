package cafe.shop.repository;

import cafe.shop.model.entities.Queue;
import cafe.shop.model.entities.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueRepository extends JpaRepository<Queue, UUID> {

    Optional<Queue> findByIdAndTerminalId(UUID queueId, UUID terminalId);

    List<Queue> findAllByTerminal(Terminal terminal);
}
