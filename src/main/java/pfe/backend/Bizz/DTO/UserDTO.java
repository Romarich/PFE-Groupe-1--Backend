package pfe.backend.Bizz.DTO;



import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class UserDTO {
    @Id
    private String userId;
    private String email;
    private String password;
    private String name;
    private String firstname;
    private String address;
    private String role;
    private String login;
    private int nbPoints;
    private boolean banned;
    private List<ItemDTO> favoriteItems;
    

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) { this.userId = userId; }

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

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public List<ItemDTO> getFavoriteItems() {
    	return favoriteItems;
	}

	public void setFavoriteItems(List<ItemDTO> favoriteItems) {
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
}
