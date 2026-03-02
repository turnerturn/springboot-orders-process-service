package cafe.shop.model.dto;

import lombok.Data;

@Data
public class RequestCreateAdminUser {

    private String username;
    private String password;
}
