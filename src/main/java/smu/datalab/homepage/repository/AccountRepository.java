package smu.datalab.homepage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.datalab.homepage.dto.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}
