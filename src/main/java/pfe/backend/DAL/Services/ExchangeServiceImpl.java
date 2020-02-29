package pfe.backend.DAL.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.backend.Bizz.DTO.ExchangeDTO;
import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.DAL.Models.Exchange;
import pfe.backend.DAL.Models.Item;
import pfe.backend.DAL.Models.User;
import pfe.backend.DAL.Repositories.ExchangeRepository;

import java.util.Date;
import java.util.List;

/**
 * Class in charge of the actions on the exchange.
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Override
    public List<Exchange> findAll() {
        return this.exchangeRepository.findAll();
    }

    @Override
    public String createExchange(ExchangeDTO exchange, String login) {
        Exchange newExchange = new Exchange();
        Item newItem = itemService
                .insertItem(exchange.getItem());
        newExchange.setItem(newItem);
        newExchange.setRelais(exchange.isRelais());
        newExchange.setValidate(false);
        Date date = new Date();
        newExchange.setDate(date);
        User user = userService.findUserByLogin(login);
        if (user == null) {
            return "KO";
        }
        newExchange.setGiver(user);
        exchangeRepository.save(newExchange);
        return "OK";
    }

    /**
     * Find the exchange which contains this exchange.
     *
     * @param objectID : the exchange who contains this item
     * @return the found exchange, null otherwise
     */
    @Override
    public List<Exchange> findByItem(String objectID) {
        return this.exchangeRepository.findByObjectID(objectID);
    }

    /**
     * Update an exchange.
     *
     * @param exchange : the exchange to update
     * @return the udpated exchange, null otherwise
     */
    @Override
    public boolean modifierExchange(Exchange exchange) {
        return this.exchangeRepository.save(exchange) != null;
    }

    @Override
    public ItemDTO takeItem(ExchangeDTO exchangeDTO, String login) {
        if (exchangeDTO.getItem().getStock() <= 0) {
            return null;
        }

        User user = userService.findUserByLogin(login);
        if (user == null) {
            return null;
        }
        Exchange exchange = new Exchange();
        Item item = itemService
                .takeItem(exchangeDTO.getItem().getObjectID());

        exchange.setItem(item);
        exchange.setRelais(exchangeDTO.isRelais());
        exchange.setTaker(user);
        exchange.setValidate(true);
        Date date = new Date();
        exchange.setDate(date);
        exchangeRepository.save(exchange);

        userService.addTakenItem(user);
        return convertIntoItemDTO(item);
    }

    @Override
    public ItemDTO giveItem(ExchangeDTO exchangeDTO, String login) {
        if (exchangeDTO.getItem().getStock() >= 200) {
            return null;
        }

        User user = userService.findUserByLogin(login);
        if (user == null) {
            return null;
        }
        Exchange exchange = new Exchange();
        Item item = itemService
                .giveItem(exchangeDTO.getItem().getObjectID());

        exchange.setItem(item);
        exchange.setRelais(exchangeDTO.isRelais());
        exchange.setGiver(user);
        exchange.setValidate(true);
        Date date = new Date();
        exchange.setDate(date);
        exchangeRepository.save(exchange);
        userService.addGivenItem(user);

        return convertIntoItemDTO(item);
    }

    @Override
    public List<Exchange> getExchangedItems(String login) {
        return exchangeRepository.findExchangesByUser(login);
    }

    /**
     * Convert an Item into an ItemDTO.
     *
     * @param item : item to be converted.
     * @return the ItemDTO converted, null otherwise
     */
    private ItemDTO convertIntoItemDTO(Item item) {
        if (null == item) {
            return null;
        }
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setObjectID(item.getObjectID());
        itemDTO.setTitle(item.getTitle());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setType(item.getType());
        itemDTO.setPicture(item.getPicture().toString());
        itemDTO.setPicture("");
        itemDTO.setStock(item.getStock());
        return itemDTO;
    }
}
