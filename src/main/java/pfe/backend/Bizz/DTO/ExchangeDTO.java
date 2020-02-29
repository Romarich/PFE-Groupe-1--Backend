package pfe.backend.Bizz.DTO;


import org.springframework.data.annotation.Id;

import java.util.Date;

public class ExchangeDTO {
    @Id
    private String exchangeId;
    private ItemDTO item;
    private boolean relais;
    private UserDTO giver;
    private UserDTO taker;
    private boolean validate;
    private Date date;

    public String getExchangeId() {
        return exchangeId;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public boolean isRelais() {
        return relais;
    }

    public void setRelais(boolean relais) {
        this.relais = relais;
    }

    public UserDTO getGiver() {
        return giver;
    }

    public void setGiver(UserDTO giver) {
        this.giver = giver;
    }

    public UserDTO getTaker() {
        return taker;
    }

    public void setTaker(UserDTO taker) {
        this.taker = taker;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
