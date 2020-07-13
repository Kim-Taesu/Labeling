package smu.datalab.homepage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import smu.datalab.homepage.dto.Account;
import smu.datalab.homepage.repository.AccountRepository;
import smu.datalab.homepage.vo.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final static String USER = "USER";
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        final Optional<Account> byId = accountRepository.findById(id);
        if (!byId.isPresent()) throw new UsernameNotFoundException(id);
        return new UserAccount(byId.get());
    }

    public List<String> getAllUsers() {
        return accountRepository.findAll().stream()
                .map(Account::getId).collect(Collectors.toList());
    }

    public void signUp(Account account) {
        account.encodePassword(passwordEncoder);
        account.setRole(USER);
        accountRepository.save(account);
    }
}
