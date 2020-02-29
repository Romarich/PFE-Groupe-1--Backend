package pfe.backend.DAL.Services;

import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.DAL.Models.Item;

import java.util.List;

/**
 * Interface of the services in relation with the item.
 */
public interface ItemService {

    /**
     * Find all items from the itemDTO's stock.
     *
     * @return : a list of all items
     */
    List<Item> findAll();

    /**
     * Find one item from the itemDTO's stock.
     *
     * @param s : the item's ID which will be find
     * @return true if is find, false otherwise
     */
    boolean findItemByCodeBool(String s);

    /**
     * Find all the validated item.
     *
     * @return A list of validatedItem
     */
    List<ItemDTO> findAllValidated();

    /**
     * Find one item from the itemDTO's stock.
     *
     * @param s : the item's ID which will be find
     * @return : the find Item, null otherwise
     */
    Item findItemByCode(String s);

    /**
     * Insert one item from the itemDTO's stock.
     *
     * @param itemDTO : the item's ID which will be insert
     * @return the insert Item, null otherwise
     */
    Item insertItem(ItemDTO itemDTO);

    /**
     * Take one item from the itemDTO's stock.
     *
     * @param objectID : the item's ID which will be taken
     * @return the taken Item, null otherwise
     */
    Item takeItem(String objectID);

    /**
     * Give one item to the itemDTO's stock.
     *
     * @param objectID : the item's ID which will be given
     * @return the given item, null otherwise
     */
    Item giveItem(String objectID);

    /**
     * Validate one item to the itemDTO's stock.
     *
     * @param idItem : the item's ID which will be validate
     * @return true if validated, false otherwise
     */
    boolean validateItem(String idItem);

    /**
     * Delete one item to the itemDTO's stock.
     *
     * @param idItem : the item's ID which will be delete.
     * @return true if deleted, false otherwise
     */
    boolean deleteItem(String idItem);
}
