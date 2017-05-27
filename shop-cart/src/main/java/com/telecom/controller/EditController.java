package com.telecom.controller;

import com.telecom.dal.ItemDaoRep;
import com.telecom.domain.ImageInfo;
import com.telecom.domain.ItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by chenhui on 17/5/25.
 */
@Controller
public class EditController {

    private static final String FILE_PATH = "J:\\workspace\\IdeaProjects\\shop-cart\\src\\main\\resources\\static\\image\\";

    @Resource
    private ItemDaoRep itemDaoRep;

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping("/image/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + FILE_PATH + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {
        ModelAndView modelAndView = new ModelAndView("edit");
        List<ItemDO> list = itemDaoRep.pageQuery(0, page, pageSize);
        int total = itemDaoRep.total(0);
        modelAndView.addObject("list", list);
        modelAndView.addObject("page", page);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("total", total);
        return modelAndView;
    }


    @RequestMapping("/get")
    @ResponseBody
    public ItemDO getItemDO(@RequestParam(value = "id", required = false, defaultValue = "1") Long itemId) {
        return itemDaoRep.getItem(itemId);
    }

    @RequestMapping("/item/edit")
    @ResponseBody
    public String submitEdit(@RequestBody ItemDO item) {
        int row = itemDaoRep.update(item);
        return "SUCCESS" + row;
    }


    @RequestMapping("/image/upload")
    @ResponseBody
    public ImageInfo uploadImage(@RequestParam("image") MultipartFile file) {
        ImageInfo result = new ImageInfo();
        try {

            String end = file.getOriginalFilename().split("\\.")[1];
            File outFile = new File(FILE_PATH + UUID.randomUUID() + "." + end);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }

            try (BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
                 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile))) {
                byte[] byteArray = new byte[1024];
                int tmp = 0;
                while ((tmp = bis.read(byteArray)) != -1) {
                    bos.write(byteArray);
                    bos.flush();
                }

                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setName(outFile.getName());
                imageInfo.setUrl(outFile.getName());
                imageInfo.setSize(outFile.length());
                return imageInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
