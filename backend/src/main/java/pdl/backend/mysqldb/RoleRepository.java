package pdl.backend.mysqldb;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface RoleRepository extends CrudRepository<Roles, Integer> {

    @Query(value = "SELECT id, name FROM roles u WHERE u.name = :roleUser", nativeQuery = true)
    Optional<Roles> findByName(@Param("roleUser") String roleUser);
    
}
