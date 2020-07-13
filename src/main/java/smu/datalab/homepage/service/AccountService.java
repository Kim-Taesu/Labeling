package smu.datalab.homepage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import smu.datalab.homepage.dto.Account;
import smu.datalab.homepage.dto.Labeling;
import smu.datalab.homepage.repository.AccountRepository;
import smu.datalab.homepage.repository.LabelingRepository;
import smu.datalab.homepage.vo.AccountStatus;
import smu.datalab.homepage.vo.EditInfo;
import smu.datalab.homepage.vo.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService implements UserDetailsService {

    private final static String USER = "USER";
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final LabelingService labelingService;
    private final LabelingRepository labelingRepository;

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

    public List<AccountStatus> getAccountStatus() {
        List<AccountStatus> result = new ArrayList<>();
        final List<String> ids = accountRepository.findAll().stream().map(Account::getId).collect(Collectors.toList());
        for (String id : ids) {
            final Long totalById = labelingService.getTotalById(id);
            final Long todoById = labelingService.getTodoById(id);
            result.add(AccountStatus.builder()
                    .id(id)
                    .todo(todoById)
                    .total(totalById)
                    .build());
        }
        return result;
    }

    public Long getDetail(String name) {
        return labelingService.getTodoById(name);
    }

    public boolean edit(EditInfo editInfo) {
        final String id = editInfo.getId();
        final String password = editInfo.getPassword();
        final Optional<Account> byId = accountRepository.findById(id);
        if (!byId.isPresent()) return false;
        final Account account = byId.get();
        account.setPassword(password);
        account.encodePassword(passwordEncoder);
        accountRepository.saveAndFlush(account);

        final int newTodo = editInfo.getTodo().intValue();
        final int todo = labelingService.getTodoById(id).intValue();
        if (newTodo > todo) return false;
        final Page<Labeling> labelings = labelingRepository.findAllByOwnerAndEmotionIsNull(id, PageRequest.of(0, todo - newTodo));
        final List<Labeling> collect = labelings.get().collect(Collectors.toList());
        collect.forEach(labeling -> labeling.setOwner(null));
        labelingRepository.saveAll(collect);
        labelingRepository.flush();
        return true;
    }
}
