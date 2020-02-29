package pfe.backend.Bizz.Controllers;

import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.Bizz.DTO.UserDTO;
import pfe.backend.DAL.Models.User;
import pfe.backend.DAL.Repositories.UserRepository;
import pfe.backend.DAL.Services.UserService;
import pfe.backend.security.jwt.JwtTokenProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/users")
@CrossOrigin
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository users;
    @Autowired
    private UserService userService;

    /**
     * Give all of the users inside the DB.
     *
     * @return the list of users or null if there is no user
     */
    @GetMapping("/all")
    public List<User> getAll() {
        return userService.findAll();
    }

    /**
     * Provide the favorite items for a user.
     *
     * @param authorizationHeader : request emit by the front-end, contains the header of this request. Specifically the
     *                            jwt who contains the user
     * @return a list of all of favourite items of the user. Or a empty list
     */
    @GetMapping("/getFavorites")
    public List<ItemDTO> getFavorites(@RequestHeader(value = "Authorization") String authorizationHeader) {
        String login = jwtTokenProvider.getUsername(authorizationHeader.substring(7));
        return userService.getFavorites(login);
    }

    /**
     * Check if the user login and his email exist in DB.
     *
     * @param user : an user DTO
     * @return a request with a status OK and the body
     */
    @PostMapping("/verificationLoginAndEmail")
    public ResponseEntity<String> checkLoginExist(@RequestBody UserDTO user) {
        Genson genson = new Genson();
        Map<String, String> mapVerificationExist = new HashMap<>();
        if (userService.existUserWithLogin(user.getLogin())) {
            mapVerificationExist.put("login", "true");
        }
        if (userService.existUserWithEmail(user.getEmail())) {
            mapVerificationExist.put("email", "true");
        }
        return ResponseEntity.status(HttpStatus.OK).body(genson.serialize(mapVerificationExist));
    }

    /**
     * Create a new user to insert in the DB.
     *
     * @param userToInsert : the user to insert
     * @return this user or null
     */
    @PostMapping("/create")
    public UserDTO createNewUser(@RequestBody UserDTO userToInsert) {
        return userService.createNewUser(userToInsert);
    }

    /**
     * Check if the user exist into the DB.
     *
     * @param userToConnect : the user inside the jwt from the request emit by the front-end
     * @return a request with a header status 200 if the user exist, else return 403 forbidden access because not in DB
     */
    @PostMapping("/login")
    public ResponseEntity signInUser(@RequestBody UserDTO userToConnect) {
        try {
            String username = userToConnect.getLogin();
            //BCrypt.hashpw(userToConnect.getPassword(), BCrypt.gensalt());

            //User user = authenticationService.signup(request);
            UserDTO userDto = userService.signInUser(userToConnect);
            if (userDto == null) {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, BCrypt.hashpw(userToConnect.getPassword(), BCrypt.gensalt()) ));

            String token = jwtTokenProvider.createToken(userDto.getLogin(), userDto.getRole(), userDto.getUserId(), userDto.getLogin());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            model.put("role", this.users.findByLogin(username).getRoles());
            model.put("banned", userDto.isBanned());
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    /**
     * Give information about the curentUser.
     *
     * @param userDetails : a userDetails
     * @return a response with this user details
     */
    @GetMapping("/me")
    public ResponseEntity currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Passe dans currentUser");
        Map<Object, Object> model = new HashMap<>();
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(toList())
        );
        return ok(model);
    }

    /**
     * Update the informations about the user.
     *
     * @param userToUpdate        : user to update
     * @param authorizationHeader : token
     * @return a string "ok" or "email"
     */
    @PostMapping("/modify")
    public String updateUser(@RequestBody UserDTO userToUpdate,
                             @RequestHeader(value = "Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String userName = jwtTokenProvider.getUsername(token);
        return userService.updateUser(userToUpdate, userName);
    }

    /**
     * Remove a favorite item of the user.
     *
     * @param authorizationHeader : header request, contains jwt, inside the username
     * @param objectID            : item to remove
     * @return a list of item dto
     */
    @PostMapping("/removeFavorite")
    public List<ItemDTO> removeFavorite(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody String objectID) {
        String login = jwtTokenProvider.getUsername(authorizationHeader.substring(7));
        return userService.removeFavorite(login, objectID);
    }

    /**
     * Add a favourite item for a user.
     *
     * @param authorizationHeader : header request, contains jwt, inside the username
     * @param objectID            : item to add
     * @return a list of item dto
     */
    @PostMapping("/addFavorite")
    public List<ItemDTO> addFavorite(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody String objectID) {
        String login = jwtTokenProvider.getUsername(authorizationHeader.substring(7));
        return userService.addFavorite(objectID, login);
    }

    /**
     * Ban an user, set the user to true for banned inside the DB.
     *
     * @param userLogin : the user to ban
     */
    @PostMapping("/banUser")
    public void banAUser(@RequestBody String userLogin) {
        userService.banAUser(userService.findUserByLogin(userLogin));
    }

    /**
     * Give the current point number.
     *
     * @param authorizationHeader : request front, contains the username
     * @return the number of points
     */
    @GetMapping("/nbPoint")
    public String getPoint(
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.equals("Bearer")) {
            return "0";
        }
        String token = authorizationHeader.substring(7);
        String userName = jwtTokenProvider.getUsername(token);
        return userService.getPoint(userName);

    }

    /**
     * Upgrade a user's role to ADMIN.
     *
     * @param userLogin : user to be upgraded
     */
    @PostMapping("/upgradeUserRole")
    public void upgradeUserRole(@RequestBody String userLogin) {
        userService.upgradeUserRole(userService.findUserByLogin(userLogin));
    }
    
    /**
     * Give the current userName.
     *
     * @param authorizationHeader : request front, contains the username
     * @return the current username
     */
    @GetMapping("/loginUserName")
    public String getLoginUserName(
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.equals("Bearer")) {
            return "0";
        }
        String token = authorizationHeader.substring(7);
        String userName = jwtTokenProvider.getUsername(token);
        return userName;

    }
}
