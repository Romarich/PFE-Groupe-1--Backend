package pfe.backend.DAL.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pfe.backend.DAL.Models.User;

import java.util.List;


/**
 * Class in charge of the interactions with the users in the database.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find the user who is login equals of the String in parameter.
     * Be carefull, the login must be unique in the MongoDb.
     *
     * @param login : the user that we want to retrieve
     * @return the user passed in parameter, if there is not in the db return null
     */
    @Query("{ 'login' : ?0 }")
    User findByLogin(String login);

    /**
     * Find a user due to his email.
     *
     * @param email : email of the user that we search
     * @return the user corresponding to the email
     */
    @Query("{ 'email' : ?0 }")
    List<User> findByEmail(String email);


}
