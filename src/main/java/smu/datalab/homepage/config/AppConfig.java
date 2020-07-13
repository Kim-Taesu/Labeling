package smu.datalab.homepage.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import smu.datalab.homepage.dto.Account;
import smu.datalab.homepage.dto.Labeling;
import smu.datalab.homepage.repository.AccountRepository;
import smu.datalab.homepage.repository.LabelingRepository;

import java.util.Date;
import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AppConfig implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final LabelingRepository labelingRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ADMIN = "admin";

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
        addSample();
    }

    private void addSample() {
        for (int i = 0; i < 11; i++) {
            labelingRepository.save(Labeling.builder()
                    .content(String.valueOf(i))
                    .date(new Date().toString())
                    .build());
        }
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
