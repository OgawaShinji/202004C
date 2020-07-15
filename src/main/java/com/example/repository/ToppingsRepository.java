package com.example.repository;

import java.util.List;

import com.example.domain.Topping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ToppingsRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;
    
    private static final RowMapper<Topping> TOPPING_ROW_MAPPER
    =(rs,i)->{
        Topping topping=new Topping();
        topping.setId(rs.getInt("id"));
        topping.setName(rs.getString("name"));
        topping.setPriceM(rs.getInt("price_m"));
        topping.setPriceL(rs.getInt("price_l"));
        return topping;
    };

    
    /** 
     * @return List<Topping>
     */
    public List<Topping> findAll() {
        String sql="SELECT * FROM toppings ORDER BY name";
        List<Topping> toppingsList=template.query(sql,TOPPING_ROW_MAPPER);
        return toppingsList;
    }


}
