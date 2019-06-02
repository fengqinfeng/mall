package com.mall.controller;

import com.mall.entity.Class_info;
import com.mall.entity.Sku_info;

import com.mall.service.Class_infoService;
import com.mall.service.Recommend;
import com.mall.service.Sku_infoService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CSku_info_Thymeleaf {
    @Autowired
    private  Sku_infoService sku_infoService;
    @Autowired
    private Class_infoService class_infoService;
    @Autowired
    private Recommend recommends;
    //根据传入的商品id获取商品的明细信息，显示
    //给show_sku_detail.html页面
    @RequestMapping("show_sku_info_detail")
    public String showSku_infoDetail(HttpServletRequest request, Model model){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        //System.out.println("..........." + sku_id);
        Sku_info sku_info = sku_infoService.showSku_info_Detail(sku_id);
        int userid=(int)request.getSession().getAttribute("user_id");
        //Sku_info skuinfo=sku_info.get(0);
        //String[] prodcut_property_values = sku_info.getProduct_property_infoList().get(0).getProperty_value().split("\\|");
        //model.addAttribute("arr_ppv", prodcut_property_values);
        model.addAttribute("sku_info", sku_info);
        //model.addAttribute("property",sku_info);
        try {
            List<RecommendedItem> ans=recommends.recommend(userid,sku_id);
            for (int i=0;i<ans.size();i++){
                System.out.println("id="+ans.get(i).getItemID()+" value="+ans.get(i).getValue());
            }
        }catch (Exception e){
            System.out.println(e);
        }


        return "details";
    }
    @RequestMapping("all_commodity")
    public String all_Commodity(){
        return "commodity";
    }

    @RequestMapping("qry_sku_info")
    //点分类进来调用此函数，如果是点搜索，则调用其他函数。
    //传入参数为：分类id，排序字段，分页起始位置，每页数量，升序或降序。
    //以上写法简单，分页可以考虑写个分页类。
    public String qry_Sku_info(HttpServletRequest request, Model model){
        String class_id = request.getParameter("class_id");
        int start = Integer.parseInt(request.getParameter("start"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String sortField = request.getParameter("sortField");
        String sortMode = request.getParameter("sortMode");
        String sort = "s." + sortField + " " + sortMode;
        //System.out.println(sort);
        List<Sku_info> sku_infoList = sku_infoService.qry_Sku_info(class_id,sort, start, pageSize);
        List<Class_info>classinfo=class_infoService.class_all();
        model.addAttribute("sku_infoList", sku_infoList);
        model.addAttribute("classinfo", classinfo);
        return "commodity";
    }
}
