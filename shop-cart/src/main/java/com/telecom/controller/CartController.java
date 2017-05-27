package com.telecom.controller;

import com.alibaba.fastjson.JSON;
import com.telecom.dal.CartDaoRep;
import com.telecom.dal.ItemDaoRep;
import com.telecom.domain.Cart;
import com.telecom.domain.CartInfo;
import com.telecom.domain.ItemDO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/26.
 */

@Controller
public class CartController {

    @Resource
    private CartDaoRep cartDaoRep;

    @Resource
    private ItemDaoRep itemDaoRep;

    /**
     * 同步加载页面
     *
     * @return
     */
    // 商品界面
    @RequestMapping("/goods")
    public ModelAndView getGoods(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView("goods");
        List<ItemDO> list = itemDaoRep.pageQuery(0, page, pageSize);
        int total = itemDaoRep.total(0);
        modelAndView.addObject("list", list);
        modelAndView.addObject("page", page);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    // 调用购物车界面
    @RequestMapping("/cart")
    public ModelAndView cart(@RequestParam(value = "cartId", required = false,defaultValue = "1") int cartId) {
        ModelAndView modelAndView = new ModelAndView("cart");
        List<Cart> list = cartDaoRep.queryCart(cartId);
        modelAndView.addObject("list", list);
        System.out.print(list.toString());
        return modelAndView;
    }

    // 添加进购物车
    @RequestMapping("/my/insert")
    @ResponseBody
    public String cartInsert(@RequestBody CartInfo ci) {
        cartDaoRep.insert(ci);
        return null;
    }

    // 更新购物车中商品数量
    @RequestMapping("/my/update")
    @ResponseBody
    public String cartUpdate(@RequestBody CartInfo ci) {
        cartDaoRep.update(ci);
        return null;
    }

    // 从购物车中删除
    @RequestMapping("/my/delete")
    @ResponseBody
    public String cartdelete(@RequestBody CartInfo ci) {
        cartDaoRep.delete(ci);
        return null;
    }

    // 清空购物车
    @RequestMapping("/my/deleteAll")
    @ResponseBody
    public String cartdeleteAll() {
        cartDaoRep.deleteAll();
        return null;
    }

    @RequestMapping("/my/sum")
    @ResponseBody
    public int getSum() {
        return cartDaoRep.getSum();
    }
}
