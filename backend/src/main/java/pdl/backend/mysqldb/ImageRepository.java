package pdl.backend.mysqldb;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends CrudRepository<Image, Integer> {

    @Query(value = "SELECT * FROM image i where i.name = :nameImage", nativeQuery = true)
    List<Image> findByName(@Param("nameImage") String nameImage);
}
