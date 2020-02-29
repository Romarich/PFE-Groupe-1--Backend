package pfe.backend.DAL.Services;

import com.algolia.search.APIClient;
import com.algolia.search.ApacheAPIClientBuilder;
import com.algolia.search.Index;
import com.algolia.search.exceptions.AlgoliaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pfe.backend.DAL.Models.Item;
import pfe.backend.DAL.Models.User;
import pfe.backend.DAL.Repositories.ItemRepository;
import pfe.backend.DAL.Repositories.UserRepository;

import java.util.List;

/**
 * Class in charge of Algolia.
 */
@Service
public class Algolia {
    @Autowired
    private Environment env;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private APIClient client;
    private Index<User> userIndex;
    private Index<Item> itemIndex;

    /**
     * Constructor for Algolia.
     */
    public Algolia() {

    }

    /**
     * Initialise algolia with the database data's.
     *
     * @throws AlgoliaException : thrown by algolia
     */
    @EventListener(ApplicationReadyEvent.class)
    public void InitAlgolia() throws AlgoliaException {
        client = new ApacheAPIClientBuilder(env
                .getProperty("algolia.appId", ""),
                env.getProperty("algolia.apikey", "")).build();
        itemIndex = client.initIndex("items", Item.class);
        userIndex = client.initIndex("users", User.class);

        itemIndex.clear();
        userIndex.clear();

        List<Item> listItem = itemRepository.findAll();
        for (Item item : listItem) {
            item.setPicture(null);
        }
        itemIndex.addObjects(listItem);
        List<User> listUser = userRepository.findAll();
        for (User user : listUser) {
            user.setPassword(null);
            user.setFavoriteItems(null);
        }
        userIndex.addObjects(listUser);

    }

    /**
     * Update the algolia's user.
     *
     * @param user : User to be updated
     */
    public void updateUser(User user) {
        try {
            user.setFavoriteItems(null);
            user.setPassword(null);
            userIndex.partialUpdateObject(user.getObjectID(), user);
        } catch (AlgoliaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete an Item in algolia.
     *
     * @param item : Item to be delete
     */
    public void deleteItem(Item item) {
        try {
            itemIndex.deleteObject(item.getObjectID());
        } catch (AlgoliaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the algolia's item.
     *
     * @param item : Item to be updated
     */
    public void updateItem(Item item) {
        try {
            item.setPicture(null);
            itemIndex.partialUpdateObject(item.getObjectID(), item);
        } catch (AlgoliaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add an item in algolia.
     *
     * @param item : Item that will be add
     */
    public void addItem(Item item) {
        try {
            item.setPicture(null);
            itemIndex.addObject(item);
        } catch (AlgoliaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a user in algolia.
     *
     * @param user : User that will be add
     */
    public void addUser(User user) {
        try {
            user.setFavoriteItems(null);
            user.setPassword(null);
            userIndex.addObject(user);
        } catch (AlgoliaException e) {
            e.printStackTrace();
        }
    }
}
