package com.telecom.controller;

/**
 * Created by Administrator on 2017/5/23.
 */
import com.alibaba.fastjson.JSON;
import com.telecom.dal.ItemDaoRep;
import com.telecom.domain.ItemDO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class Router {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/hello")
    @ResponseBody//返回一个接口的形式   异步渲染
    public String hello(@RequestParam(value = "name", required = false) String name,
                        HttpServletRequest request) {

        String sql = "select * from Item";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        String listString = JSON.toJSONString(list);

        String nameRequest = request.getParameter("name");
        return listString;
    }

    /**
     * 同步加载页面
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "name", required = false) String name,
                              @RequestParam("page") Integer page,
                              @RequestParam("pageSize") Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView("index");
        List<ItemDO> list = itemDaoRep.pageQuery(0, page, pageSize);
        int total = itemDaoRep.total(0);
        modelAndView.addObject("name", name);
        modelAndView.addObject("list", list);
        modelAndView.addObject("page", page);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("total", total);
        return modelAndView;
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * FROM test");
        if (null != result) {
            return JSON.toJSONString(result);
        }
        return "no data";
    }


    @Resource
    private ItemDaoRep itemDaoRep;
    @RequestMapping("/list")
    @ResponseBody
    /*public String list(@RequestParam("page") Integer page,
                       @RequestParam("pageSize") Integer pageSize) {
        List<ItemDO> list = itemDaoRep.pageQuery(0, page, pageSize);
        String listString = JSON.toJSONString(list);
        return listString;
    }
    */
    public List<ItemDO> list(@RequestParam("page") Integer page,
                             @RequestParam("pageSize") Integer pageSize) {
        List<ItemDO> list = itemDaoRep.pageQuery(0, page, pageSize);
        return list;
    }


}

