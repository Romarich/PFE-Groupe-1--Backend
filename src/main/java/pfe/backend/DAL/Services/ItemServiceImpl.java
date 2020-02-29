package pfe.backend.DAL.Services;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.DAL.Models.Exchange;
import pfe.backend.DAL.Models.Item;
import pfe.backend.DAL.Repositories.ItemRepository;
import pfe.backend.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class in charge of the actions on the item.
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private Algolia algolia;

    @Override
    public List<Item> findAll() {
        return this.itemRepository.findAll();
    }

    @Override
    public List<ItemDTO> findAllValidated() {
        List<Item> itemList = this.itemRepository.findAllByValidateIsTrue();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        Utils.fillItemDTOList(itemList, itemDTOList);
        return itemDTOList;
    }

    /**
     * Find an item by it's ID.
     *
     * @param objectId : ID of the object
     * @return true if found, false otherwise
     */
    @Override
    public boolean findItemByCodeBool(String objectId) {
        Item item = itemRepository.findByObjectID(objectId);
        return item == null;
    }

    /**
     * Find an item by it's ID.
     *
     * @param objectId : ID of the object
     * @return the found object, null otherwise
     */
    @Override
    public Item findItemByCode(String objectId) {
        return itemRepository.findByObjectID(objectId);
    }

    @Override
    public Item insertItem(ItemDTO itemDTO) {
        Item newItem = new Item();
        newItem.setObjectID(itemDTO.getObjectID());
        newItem.setTitle(itemDTO.getTitle());
        byte[] imageByte = Base64.decodeBase64(itemDTO.getPicture());
        newItem.setPicture(imageByte);
        newItem.setStock(1);
        newItem.setDescription(itemDTO.getDescription());
        newItem.setType(itemDTO.getType());
        newItem.setValidate(false);
        itemRepository.save(newItem);
        newItem.setPicture(null);
        algolia.addItem(newItem);
        return itemRepository.findByObjectID(newItem.getObjectID());
    }

    @Override
    public Item takeItem(String objectID) {
        Item item = this.itemRepository.findByObjectID(objectID);
        item.setStock(item.getStock() - 1);
        return this.itemRepository.save(item);
    }

    @Override
    public Item giveItem(String objectID) {
        Item item = this.itemRepository.findByObjectID(objectID);
        item.setStock(item.getStock() + 1);
        return this.itemRepository.save(item);
    }

    @Override
    public boolean validateItem(String idItem) {
        Item item = itemRepository.findByObjectID(idItem);
        item.setValidate(true);
        itemRepository.save(item);
        item.setPicture(null);
        algolia.updateItem(item);
        List<Exchange> exchanges = exchangeService.findByItem(idItem);
        if (exchanges.size() != 1) {
            return false;
        }

        Exchange exchange = exchanges.get(0);

        if (exchange.isValidate()) {
            return false;
        }
        exchange.setValidate(true);
        if (null == giveItem(item.getObjectID())) {
            return false;
        }
        if (null == userService.addGivenItem(exchange.getGiver())) {
            return false;
        }
        return exchangeService.modifierExchange(exchange);
    }

    @Override
    public boolean deleteItem(String idItem) {
        Item item = itemRepository.findByObjectID(idItem);
        this.itemRepository.delete(item);
        algolia.deleteItem(item);
        return itemRepository.findById(idItem) == null;
    }
}
