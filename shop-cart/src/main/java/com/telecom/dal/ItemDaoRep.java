package com.telecom.dal;

import com.telecom.domain.ItemDO;
import org.apache.commons.lang.StringUtils;
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
 * Created by chenhui on 17/5/25.
 */
@Repository
public class ItemDaoRep {

    @Resource
    private JdbcTemplate jdbcTemplate;


    public List<ItemDO> pageQuery(final int category, int page, final int pageSize) {
        final int queryPage = page <= 0 ? 0 : (page - 1) * pageSize;
        String sql = "SELECT * FROM Item where category = ? limit ?,?";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<List<ItemDO>>() {
            public List<ItemDO> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, category);
                ps.setInt(2, queryPage);
                ps.setInt(3, pageSize);
                ResultSet rs = ps.executeQuery();//执行真正的查询
                List<ItemDO> list = new ArrayList<ItemDO>();
                while (rs.next()) {
                    ItemDO itemDO = new ItemDO();
                    itemDO.setId(rs.getLong("id"));
                    itemDO.setAuthor(rs.getString("author"));
                    itemDO.setCategory(rs.getInt("category"));
                    itemDO.setImage(rs.getString("image"));
                    itemDO.setPrice(rs.getLong("price"));
                    itemDO.setDescription(rs.getString("description"));
                    itemDO.setTitle(rs.getString("title"));
                    list.add(itemDO);
                }
                return list;
            }
        });
    }


    public int total(int category) {
        String sql = "select count(*) from Item where category = " + category + " limit 1";
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }


    public ItemDO getItem(final long itemId) {
        String sql = "SELECT * FROM Item where id = ? limit 1";
        return jdbcTemplate.execute(sql, new PreparedStatementCallback<ItemDO>() {
            public ItemDO doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setLong(1, itemId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ItemDO itemDO = new ItemDO();
                    itemDO.setId(rs.getLong("id"));
                    itemDO.setAuthor(rs.getString("author"));
                    itemDO.setCategory(rs.getInt("category"));
                    itemDO.setImage(rs.getString("image"));
                    itemDO.setPrice(rs.getLong("price"));
                    itemDO.setDescription(rs.getString("description"));
                    itemDO.setTitle(rs.getString("title"));
                    return itemDO;
                }
                return null;
            }
        });
    }

    public int update(ItemDO itemDO) {
        if (null == itemDO || itemDO.getId() <= 0) {
            return -1;
        }
        List<Object> argsList = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder("UPDATE Item set");
        if (StringUtils.isNotBlank(itemDO.getAuthor())) {
            sb.append(" author = ? ");
            sb.append(",");
            argsList.add(itemDO.getAuthor());

        }
        if (StringUtils.isNotBlank(itemDO.getTitle())) {
            sb.append(" title = ? ");
            sb.append(",");
            argsList.add(itemDO.getTitle());
        }

        if (StringUtils.isNotBlank(itemDO.getDescription())) {
            sb.append(" description = ? ");
            sb.append(",");
            argsList.add(itemDO.getDescription());
        }
        if (StringUtils.isNotBlank(itemDO.getImage())) {
            sb.append(" image =? ");
            sb.append(",");
            argsList.add(itemDO.getImage());
        }
        if (itemDO.getPrice() != 0) {
            sb.append(" price = ? ");
            sb.append(",");
            argsList.add(itemDO.getPrice());
        }
        if (itemDO.getCategory() == 0) {
            sb.append(" category = ? ");
            sb.append(",");
            argsList.add(itemDO.getCategory());
        }
        String href = sb.toString();
        int last = href.lastIndexOf(",");
        if (last <= 0) {
            return -1;
        }
        String sq = href.substring(0, last);
        sq = sq + "where id = ? limit 1";
        argsList.add(itemDO.getId());
        System.out.println(sq);
        int rows = jdbcTemplate.update(sq, argsList.toArray());
        return rows;
    }

}
