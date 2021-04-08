package pdl.backend.mysqldb;

import org.springframework.data.repository.CrudRepository;

import pdl.backend.Image;

public interface ImageRepository extends CrudRepository<Image, Integer>{

}
