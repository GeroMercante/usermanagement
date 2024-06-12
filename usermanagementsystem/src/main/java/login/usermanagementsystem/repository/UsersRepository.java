package login.usermanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import login.usermanagementsystem.entity.OurUsers;
import java.util.Optional;


public interface UsersRepository extends JpaRepository<OurUsers, Long> {
  Optional<OurUsers> findByEmail(String email);
}
