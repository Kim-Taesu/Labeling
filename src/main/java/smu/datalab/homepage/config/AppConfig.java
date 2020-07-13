package smu.datalab.homepage.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import smu.datalab.homepage.dto.Account;
import smu.datalab.homepage.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AppConfig implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ADMIN = "admin";

    @Value("#{'${emotions}'.split(',')}")
    private List<String> emotionList;

    @Bean
    public List<String> emotions() {
        return this.emotionList;
    }

    private Account buildAdmin(String id) {
        Account account = new Account();
        account.setId(id);
        account.setPassword(id);
        if (id.equals(ADMIN)) account.setRole(ADMIN.toUpperCase());
        else account.setRole("USER");
        account.encodePassword(passwordEncoder);
        return account;
    }

    @Override
    public void run(ApplicationArguments args) {
        addAccount(ADMIN);
    }

    private void addAccount(String id) {
        final Optional<Account> byId = accountRepository.findById(id);
        if (!byId.isPresent()) {
            final Account account = buildAdmin(id);
            accountRepository.save(account);
            log.info(accountRepository.findById(account.getId()).get().toString());
        }
    }
}
