package pfe.backend.Bizz.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pfe.backend.Bizz.DTO.ExchangeDTO;
import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.DAL.Models.Exchange;
import pfe.backend.DAL.Services.ExchangeService;
import pfe.backend.security.jwt.JwtTokenProvider;

import java.util.List;

@RestController
@RequestMapping("api/exchanges")
@CrossOrigin
public class ExchangeController {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/all")
    public List<Exchange> getAll() {
        return exchangeService.findAll();
    }

    /**
     * Allow to create an exchange and the object associated.
     *
     * @param exchange            : an exchangeDto
     * @param authorizationHeader : the request emit by the front-end, contain the header
     * @return a string with value "OK" or "KO"
     */
    @PostMapping("/create")
    public String createExchange(@RequestBody ExchangeDTO exchange,
                                 @RequestHeader(value = "Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userName = jwtTokenProvider.getUsername(token);
        return exchangeService.createExchange(exchange, userName);
    }

    /**
     * Assign an object to an user.
     *
     * @param authorizationHeader :the request emit by the front-end, contain the header
     * @param exchangeDTO         : an exchange dto send from the front-end
     * @return a null object or a dtoItem
     */
    @PostMapping("/takeItem")
    public ItemDTO takeItem(@RequestHeader(value = "Authorization") String authorizationHeader,
                            @RequestBody ExchangeDTO exchangeDTO) {
        String token = authorizationHeader.substring(7);
        String userName = jwtTokenProvider.getUsername(token);
        return this.exchangeService.takeItem(exchangeDTO, userName);
    }

    /**
     * Associate the user who gives an item.
     *
     * @param authorizationHeader :the request emit by the front-end, contain the header
     * @param exchangeDTO         : an exchange dto send from the front-end
     * @return a null object or a dtoItem
     */
    @PostMapping("/giveItem")
    public ItemDTO giveItem(@RequestHeader(value = "Authorization") String authorizationHeader,
                            @RequestBody ExchangeDTO exchangeDTO) {
        String token = authorizationHeader.substring(7);
        String userName = jwtTokenProvider.getUsername(token);
        return this.exchangeService.giveItem(exchangeDTO, userName);
    }

    /**
     * Returns exchanges list of a specific user.
     *
     * @param authorizationHeader : the request emit by the front-end, contain the header
     * @return : list of ExchangeDto
     */
    @GetMapping("/exchanged")
    public List<Exchange> getExchanged(@RequestHeader(value = "Authorization") String authorizationHeader) {
        String login = jwtTokenProvider.getUsername(authorizationHeader.substring(7));
        return exchangeService.getExchangedItems(login);
    }
}
