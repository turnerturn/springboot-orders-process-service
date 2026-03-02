package cafe.shop.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUser extends User {

    private String id;
    private String displayName;
    private boolean isVerify2Fa;
    private boolean isUsing2FA;
    private boolean isLogin2FA;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public boolean isVerify2Fa() {
        return isVerify2Fa;
    }

    public void setVerify2Fa(boolean verify2Fa) {
        isVerify2Fa = verify2Fa;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean using2FA) {
        isUsing2FA = using2FA;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLogin2FA() {
        return isLogin2FA;
    }

    public void setLogin2FA(boolean login2FA) {
        isLogin2FA = login2FA;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
