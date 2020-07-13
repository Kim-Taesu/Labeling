package smu.datalab.homepage.vo;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import smu.datalab.homepage.dto.Account;

import java.util.Collections;

@Getter
public class UserAccount extends User {

    private Account account;

    public UserAccount(Account account) {
        super(account.getId(), account.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole())));
        this.account = account;
    }
}
