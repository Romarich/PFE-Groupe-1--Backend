package pfe.backend.DAL.Services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.Bizz.DTO.UserDTO;
import pfe.backend.DAL.Models.Item;
import pfe.backend.DAL.Models.User;
import pfe.backend.DAL.Repositories.UserRepository;
import pfe.backend.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class in charge of the actions on the user.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Algolia algolia;

    @Override
    public UserDTO createNewUser(UserDTO user) {
        User userToInsert = new User();
        userToInsert.setEmail(user.getEmail());

        userToInsert
                .setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userToInsert.setLogin(user.getLogin());
        userToInsert.setRole("USER");

        userToInsert = userRepository.save(userToInsert);
        userToInsert.setPassword("");

        algolia.addUser(userToInsert);
        return user;
    }

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<ItemDTO> getFavorites(String login) {
        User user = userRepository.findByLogin(login);
        List<Item> itemList = user.getFavoriteItems();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        if (null != itemList) {
            Utils.fillItemDTOList(itemList, itemDTOList);
        }
        return itemDTOList;
    }

    @Override
    public String updateUser(UserDTO user, String login) {
        User userToUpdate = userRepository.findByLogin(login);
        if (!"".equals(user.getEmail())) {
            if (!existUserWithEmail(user.getEmail())) {
                return "email";
            }
            userToUpdate.setEmail(user.getEmail());
        }
        if (!"".equals(user.getFirstname())) {
            userToUpdate.setFirstname(user.getFirstname());
        }
        if (!"".equals(user.getName())) {
            userToUpdate.setName(user.getName());
        }
        if (!"".equals(user.getAddress())) {
            System.out.println(user.getFirstname());
            userToUpdate.setAddress(user.getAddress());
        }
        if (!"".equals(user.getPassword())) {
            userToUpdate
                    .setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        userRepository.save(userToUpdate);
        algolia.updateUser(userToUpdate);
        return "OK";
    }

    @Override
    public boolean existUserWithLogin(String login) {
        User user = userRepository.findByLogin(login);
        return user == null;
    }

    @Override
    public boolean existUserWithEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        return users.isEmpty();
    }

    @Override
    public UserDTO signInUser(UserDTO user) {
        User userFind = userRepository.findByLogin(user.getLogin());
        if (userFind == null) {
            return null;
        }
        user.setLogin(userFind.getLogin());
        user.setRole(userFind.getRoles());
        user.setEmail(userFind.getEmail());
        user.setUserId(userFind.getObjectID());
        user.setBanned(userFind.isBanned());

        System.out.println("Role du user dans service : " + userFind);
        if (userFind != null) {
            boolean pwdSimilar = BCrypt.checkpw(user.getPassword(), userFind.getPassword());
            if (pwdSimilar) {
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<ItemDTO> removeFavorite(String login, String objectID) {
        if (null == login || null == objectID) {
            return null;
        }
        User user = userRepository.findByLogin(login);
        if (null == user) {
            return null;
        }
        List<Item> itemList = user.getFavoriteItems();
        if (itemList.stream()
                .anyMatch(item -> objectID.equals(item.getObjectID()))) {
            itemList = itemList.stream().filter(item -> !objectID
                    .equals(item.getObjectID())).collect(Collectors.toList());
            user.setFavoriteItems(itemList);
            userRepository.save(user);
        }
        List<ItemDTO> itemDTOList = new ArrayList<>();
        Utils.fillItemDTOList(itemList, itemDTOList);
        return itemDTOList;
    }

    @Override
    public List<ItemDTO> addFavorite(String objectID, String login) {
        User userToUpdate = userRepository.findByLogin(login);
        Item itemDb = itemService.findItemByCode(objectID);

        if (itemDb == null || null == userToUpdate) {
            return null;
        }

        List<Item> result = userToUpdate.getFavoriteItems();
        if (null == result) {
            result = new ArrayList<>();
        }
        if (result.stream()
                .noneMatch(item -> objectID.equals(item.getObjectID()))) {
            result.add(itemDb);
            userToUpdate.setFavoriteItems(result);
            userRepository.save(userToUpdate);
        }
        List<ItemDTO> itemDTOList = new ArrayList<>();
        Utils.fillItemDTOList(result, itemDTOList);
        return itemDTOList;
    }

    @Override
    public User addTakenItem(User user) {
        user.setNbPoints(user.getNbPoints() - 1);
        return userRepository.save(user);
    }

    @Override
    public User addGivenItem(User user) {
        User newUser = findUserByLogin(user.getLogin());
        newUser.setNbPoints(newUser.getNbPoints() + 1);
        return userRepository.save(newUser);
    }

    @Override
    public String getPoint(String login) {
        return findUserByLogin(login).getNbPoints() + "";
    }

    @Override
    public User banAUser(User user) {
        user.setBanned(true);
        user = userRepository.save(user);
        algolia.updateUser(user);
        return user;
    }

    @Override
    public User upgradeUserRole(User user) {
        user.setRole("ADMIN");
        user = userRepository.save(user);
        algolia.updateUser(user);
        return user;
    }
}
