package pdl.backend.mysqldb;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends CrudRepository<Image, Integer> {

    @Query(value = "SELECT * FROM image i WHERE i.fk_user_id = :id ", nativeQuery = true)
    List<Image> findAllByUserId(@Param("id") Integer id);

    @Query(value = "SELECT * FROM image i WHERE i.fk_user_id is null", nativeQuery = true)
    List<Image> findAllPublic();
    @Query(value = "SELECT * FROM image i where i.name = :nameImage", nativeQuery = true)
    List<Image> findByName(@Param("nameImage") String nameImage);

    @Query(value = "DELETE FROM image i WHERE i.id = :id", nativeQuery = true)
    Boolean deleteImageBoolean(@Param("id") Integer id);

}
