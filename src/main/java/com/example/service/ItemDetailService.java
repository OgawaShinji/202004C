package com.example.service;

import java.util.List;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.repository.ItemsRepository;
import com.example.repository.ToppingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemDetailService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private ToppingsRepository toppingsRepository;

    
    /** 
     * @param itemId
     * @return Item
     */
    public Item load(String itemId){
        Item item=itemsRepository.load(Integer.parseInt(itemId));
        return item;
    }

    
    /** 
     * @return List<Topping>
     */
    public List<Topping> showAll() {
        List<Topping> toppingList=toppingsRepository.findAll();
        return toppingList;
    }
}
