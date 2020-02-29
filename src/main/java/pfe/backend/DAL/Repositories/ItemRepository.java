package pfe.backend.DAL.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pfe.backend.DAL.Models.Item;

import java.util.List;

/**
 * Class in charge of the interactions with the items in the database.
 */
public interface ItemRepository extends MongoRepository<Item, String> {

    /**
     * Find the item with the objectID entered in parameter.
     *
     * @param objectID : objectID of the Item we are looking for
     * @return the found Item, null otherwise
     */
    @Query("{ 'objectID': ?0 }")
    Item findByObjectID(String objectID);

    /**
     * Find all the validated items.
     *
     * @return a list of validated Item, null otherwise
     */
    List<Item> findAllByValidateIsTrue();
}
