package pfe.backend.Bizz.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.DAL.Models.Item;
import pfe.backend.DAL.Services.ItemService;
import pfe.backend.security.jwt.JwtTokenProvider;

import java.util.List;

@RestController
@RequestMapping("api/items")
@CrossOrigin
public class ItemController {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ItemService itemService;

    /**
     * Give all the items inside the DataBase.
     *
     * @return a list of all items find in the DB
     */
    @GetMapping("/all")
    public List<Item> getAllItems() {
        return this.itemService.findAll();
    }

    /**
     * Get all validate items
     *
     * @return a list of dto
     */
    @GetMapping("/allValidated")
    public List<ItemDTO> getAllValidatedItems() {
        return this.itemService.findAllValidated();
    }

    /**
     * Find a specific item inside the DB.
     *
     * @param itemDTO : the item that we search inside the DB
     * @return true if the item was found, else false
     */
    @PostMapping("/find")
    public boolean checkItemExist(@RequestBody ItemDTO itemDTO) {
        return itemService.findItemByCodeBool(itemDTO.getObjectID());
    }

    /**
     * Allow to validate an item.
     *
     * @param idItem : the id of the item that will be insert in the DB
     * @return true if the object was succesfully added in the DB or return false
     */
    @PostMapping("/validationItem")
    public boolean validateItem(@RequestBody String idItem) {
        return this.itemService.validateItem(idItem);
    }

    /**
     * Delete an item inside the DB.
     *
     * @param idItem : the item that will be deleted
     * @return true if the supression succeed, else return false
     */
    @PostMapping("/deleteItem")
    public boolean deleteItem(@RequestBody String idItem) {
        return this.itemService.deleteItem(idItem);
    }


}
