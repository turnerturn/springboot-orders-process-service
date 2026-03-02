package cafe.shop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQueueDto {
    private UUID orderId;
    private int queuePosition;
    private int expectedWaitingTime;
    private String orderNumber;
    private String customerName;
    private String customerPhone;
    private UUID customerQueueId;
    private String recipeName;
    private double volume;
    private String destinationId;
}
