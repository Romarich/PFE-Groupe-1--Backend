package pfe.backend.utils;

import pfe.backend.Bizz.DTO.ItemDTO;
import pfe.backend.DAL.Models.Item;

import java.util.Base64;
import java.util.List;

/**
 * Class with utilitaries functions.
 */
public interface Utils {

    String REGEX_EMPTY_STRING = "^$";
    String REGEX_EMAIL = "^(\\w\\.?)+@\\w+(\\.\\w+)+$";

    /**
     * Check if the string is null or empty.
     *
     * @param stringToTest - String to test.
     * @return true if the string is valid, else false
     */
    static boolean validString(String stringToTest) {
        return stringToTest != null && !stringToTest.equals("")
                && !stringToTest.matches(REGEX_EMPTY_STRING);
    }

    /**
     * Check if a object is null and throw a IllegalArgumentException.
     *
     * @param obj - Object to check.
     */
    static void checkObject(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Null Object");
        }
    }

    /**
     * Convert the Item of the itemList into an ItemDTO,
     * then add ite the itemDTOList.
     *
     * @param itemList    : List of Item to be added
     * @param itemDTOList : List of ItemDTO to be filled
     */
    static void fillItemDTOList(List<Item> itemList, List<ItemDTO> itemDTOList) {
        ItemDTO itemDTO;
        for (Item item : itemList) {
            itemDTO = new ItemDTO();
            itemDTO.setObjectID(item.getObjectID());
            itemDTO.setTitle(item.getTitle());
            itemDTO.setType(item.getType());
            itemDTO.setDescription(item.getDescription());
            itemDTO.setStock(item.getStock());
            String encodedString = Base64.getEncoder()
                    .encodeToString(item.getPicture());
            itemDTO.setPicture(encodedString);
            itemDTOList.add(itemDTO);
        }
    }
}
