package com.telecom.dal;

import com.telecom.domain.Cart;
import com.telecom.domain.CartInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */
@Repository
public class CartDaoRep {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Cart> queryCart(final int cartId) {
        String sql = " select a.id,a.item_id,a.num,b.title,b.description,b.price,b.image ";
        sql  = sql + " from cart a,item b ";
        sql  = sql + " where a.item_id = b.id ";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<java.util.List<Cart>>() {
            public List<Cart> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ResultSet rs = ps.executeQuery();//执行真正的查询
                List<Cart> list = new ArrayList<Cart>();
                while (rs.next()) {
                    Cart cart = new Cart();
                    cart.setId(rs.getLong("id"));
                    cart.setItem_id(rs.getLong("item_id"));
                    cart.setNum(rs.getLong("num"));
                    cart.setTitle(rs.getString("title"));
                    cart.setDescription(rs.getString("description"));
                    cart.setPrice(rs.getLong("price"));
                    cart.setImage(rs.getString("image"));
                    list.add(cart);
                }
                return list;
            }
        });
    }


    public int getItemNum(long itemId) {
        String sql = "select count(*) from cart where item_id = " + itemId;
        String sql2 = "select num from cart where item_id = " + itemId + " limit 1";
        if(jdbcTemplate.queryForObject(sql, Integer.class)>0){
            return jdbcTemplate.queryForObject(sql2, Integer.class);
        }else
            return 0;
    }


    public boolean insert(CartInfo ci) {
        if (null == ci || ci.getItem_id() <= 0) {
            return false;
        }

        try {
            int count = getItemNum(ci.getItem_id());
            if(count>0){
                jdbcTemplate.update("UPDATE cart SET num = ? WHERE item_id = ?",
                        new Object[]{count+ci.getNum(), ci.getItem_id()});
            }else{
                jdbcTemplate.update("INSERT INTO cart(item_id,num) VALUES(?,?)",
                        new Object[]{ci.getItem_id(), ci.getNum()});
            }


        } catch (Exception e) {

        }
        return true;
    }

    public boolean update(CartInfo ci) {
        if (null == ci || ci.getId() <= 0) {
            return false;
        }

        try {
            if(ci.getNum()>0){
                jdbcTemplate.update("UPDATE cart SET num = ? WHERE id = ?",
                        new Object[]{ci.getNum(), ci.getId()});
            }else{
                jdbcTemplate.update("DELETE from cart where id = ?",
                        new Object[]{ci.getId()});
            }

        } catch (Exception e) {

        }
        return true;
    }


    public boolean delete(CartInfo ci) {
        if (null == ci || ci.getId() <= 0) {
            return false;
        }

        try{
            jdbcTemplate.update("DELETE from cart where id = ?",
                    new Object[] {ci.getId()});
        }catch(Exception e){

        }
        return true;
    }

    public int deleteAll() {
        try {
            jdbcTemplate.update("DELETE from cart");
        } catch (Exception e) {

        }
        return 0;
    }
    public int getSum() {
        String sql = "select count(*) from cart";
        String sql2 = "select sum(num) num from cart";
        if(jdbcTemplate.queryForObject(sql, Integer.class)>0){
            return jdbcTemplate.queryForObject(sql2, Integer.class);
        }else
            return 0;
    }
}
