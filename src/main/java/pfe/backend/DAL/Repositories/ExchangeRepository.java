package pfe.backend.DAL.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pfe.backend.DAL.Models.Exchange;

import java.util.List;

/**
 * Class in charge of the interactions with the exchanges in the database.
 */
public interface ExchangeRepository extends MongoRepository<Exchange, String> {

    /**
     * Find an item in the DB due to his id.
     *
     * @param objectID : id item to find
     * @return a list of item find
     */
    @Query("{'item.objectID': ?0}")
    List<Exchange> findByObjectID(String objectID);

    /**
     * Find the exchange, inside the DB, for a user.
     *
     * @param login : user
     * @return list of exchange
     */
    @Query("{$and:[{'validate': true}, {$or:[{'giver.login': ?0},{'taker.login': ?0}]}]}")
    List<Exchange> findExchangesByUser(String login);
}
