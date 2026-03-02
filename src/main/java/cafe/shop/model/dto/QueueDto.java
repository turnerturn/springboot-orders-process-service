package cafe.shop.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class QueueDto {

    private UUID id;
    private int queueNumber;
}
