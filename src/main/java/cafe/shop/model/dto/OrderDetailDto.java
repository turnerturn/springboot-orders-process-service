package cafe.shop.model.dto;

import cafe.shop.model.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private UUID orderId;
    private String orderNumber;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private OrderStatus status;
    private String recipeName;
    private double volume;
    private String destinationId;
}
