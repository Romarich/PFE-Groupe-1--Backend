package pfe.backend.DAL.Services;

import pfe.backend.Bizz.DTO.ExchangeDTO;
import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.DAL.Models.Exchange;

import java.util.List;

/**
 * Interface of the services in relation with the exchange.
 */
public interface ExchangeService {

    /**
     * Find all exchange.
     *
     * @return a list of exchange
     */
    List<Exchange> findAll();

    /**
     * Find an the exchange for a item.
     *
     * @param objectID : the exchange who contains this item
     * @return a list of exchange
     */
    List<Exchange> findByItem(String objectID);

    /**
     * Create an exchange.
     *
     * @param exchangeInformation : the exchange to create
     * @param login               : associate the exchange to this user
     * @return a string "OK", otherwise "KO"
     */
    String createExchange(ExchangeDTO exchangeInformation, String login);

    /**
     * Update an exchange.
     *
     * @param exchange : the exchange to update
     * @return ture if the upadate succed, false otherwise
     */
    boolean modifierExchange(Exchange exchange);

    /**
     * Take an item from the stock and create a new exchange.
     *
     * @param exchangeDTO : DTO containing the data needed
     * @param login       : user's login
     * @return the ItemDTO taken, null otherwise
     */
    ItemDTO takeItem(ExchangeDTO exchangeDTO, String login);

    /**
     * Add an item to the stock and create a new exchange.
     *
     * @param exchangeDTO : the new Exchange to be added
     * @param login       : user's login giving the item
     * @return the ItemDTO added, null otherwise
     */
    ItemDTO giveItem(ExchangeDTO exchangeDTO, String login);

    /**
     * Returns exchanges list of a specific user.
     *
     * @param login user login
     * @return list of ExchangeDto
     */
    List<Exchange> getExchangedItems(String login);
}
