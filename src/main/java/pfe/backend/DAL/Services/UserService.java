package pfe.backend.DAL.Services;

import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.Bizz.DTO.UserDTO;
import pfe.backend.DAL.Models.User;

import java.util.List;

/**
 * Interface of the services in relation with the user.
 */
public interface UserService {

    /**
     * Create a new user.
     *
     * @param user : the user to create
     * @return the dto of this user
     */
    UserDTO createNewUser(UserDTO user);

    /**
     * Find a user via his login.
     *
     * @param login : the user with this login
     * @return the user found, null otherwise
     */
    User findUserByLogin(String login);

    /**
     * Find all users.
     *
     * @return a list of all users
     */
    List<User> findAll();

    /**
     * Get all of the user's favorite item.
     *
     * @param login : user's login
     * @return the user's list of favorite item, null otherwise
     */
    List<ItemDTO> getFavorites(String login);

    /**
     * Update the user's data.
     *
     * @param user  : the user with the new data
     * @param login : login of the user to update
     * @return the user with the new data, null otherwise
     */
    String updateUser(UserDTO user, String login);

    /**
     * Allow a user to be authentificated on the site.
     * if he exists in the database.
     *
     * @param user : the current user who tried to connect to the site
     * @return a userDto
     */
    UserDTO signInUser(UserDTO user);

    /**
     * Check if a user exist for a login.
     *
     * @param s : the login
     * @return true if it's find, false otherwise
     */
    boolean existUserWithLogin(String s);

    /**
     * Check if a user exist for a email.
     *
     * @param s : the login
     * @return true if it's find, false otherwise
     */
    boolean existUserWithEmail(String s);

    /**
     * Remove a favorite frome the user's list of favorite.
     *
     * @param login    : user's login
     * @param objectID : item's ID
     * @return the user's favorite list, null otherwise
     */
    List<ItemDTO> removeFavorite(String login, String objectID);

    /**
     * Add a new favorite to the user's list of favorite.
     *
     * @param item  : if of the new favorite item
     * @param login : the login of the user who's adding a new favorite
     * @return the user's favorite list, null otherwise
     */
    List<ItemDTO> addFavorite(String item, String login);

    /**
     * Add a new item to the user's list of taken items.
     *
     * @param user : user
     * @return The user's list of takenItem, null otherwise
     */
    User addTakenItem(User user);

    /**
     * Add a new item to the user's list of given items.
     *
     * @param user : user
     * @return The user's list of givenItem, null otherwise
     */
    User addGivenItem(User user);

    /**
     * Ban a user.
     *
     * @param user : user to ban
     * @return the user banned
     */
    User banAUser(User user);

    /**
     * Get the number of points.
     *
     * @param login : the user
     * @return a string who contains the points
     */
    String getPoint(String login);

    /**
     * Change a role of the user to ADMIN.
     *
     * @param user : user to be promoted
     * @return the upgraded user
     */
    User upgradeUserRole(User user);

}
