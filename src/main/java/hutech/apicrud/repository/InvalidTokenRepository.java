package hutech.apicrud.repository;

import hutech.apicrud.entities.InvalidToken;
import hutech.apicrud.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken,String> {
}
