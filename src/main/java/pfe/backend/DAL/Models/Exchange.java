package pfe.backend.DAL.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pfe.backend.DAL.Models.Item;

import java.util.Date;

@Document(collection = "Exchanges")
public class Exchange {
    @Id
    private String exchangeId;
    private Item item;
    private boolean relais;
    private User giver;
    private User taker;
    private boolean validate;
    private Date date;

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isRelais() {
        return relais;
    }

    public void setRelais(boolean relais) {
        this.relais = relais;
    }

    public User getGiver() {
        return giver;
    }

    public void setGiver(User giver) {
        this.giver = giver;
    }

    public User getTaker() {
        return taker;
    }

    public void setTaker(User taker) {
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
