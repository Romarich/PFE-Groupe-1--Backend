package pfe.backend.DAL.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pfe.backend.DAL.Models.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document(collection = "Users")
public class User {
    @Id
    private String objectID;
    private String email;
    private String password;
    private String name;
    private String firstname;
    private String address;
    // Roles can contains several roles, this is a string because MongoDB stock just String, boolean and integer
    private String roles = "";
    @Field(value = "login")
    private String login;
    private int nbPoints;
    private boolean banned;
    private List<Item> favoriteItems;

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    // Give a String of roles, e.g : ' "ADMIN","USER" '
    public String getRoles() {
        return roles;
    }

    public void setRole(String roles) {
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public int getNbPoints() {
        return nbPoints;
    }

    public void setNbPoints(int nbPoints) {
        this.nbPoints = nbPoints;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public List<Item> getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(List<Item> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Allow to know all the roles stocked in the string roles.
     * Transform this string roles into an ArrayList
     *
     * @return the roles
     */
    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

}
