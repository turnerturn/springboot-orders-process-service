package cafe.shop.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TerminalDto {

    private UUID id;
    private String name;
    private String location;
    private double distance;
}
