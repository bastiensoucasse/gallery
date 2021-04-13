package pdl.backend.mysqldb;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    
    @Query(value = "SELECT * FROM users u WHERE u.username = :username", nativeQuery = true)
    public Optional<User> findByUsername(@Param("username") String username);

    Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
