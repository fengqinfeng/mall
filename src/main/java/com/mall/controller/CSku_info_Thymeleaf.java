package com.mall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.entity.BuyerItem_Patch;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CSku_info_Thymeleaf {
    @Autowired
    private  Sku_infoService sku_infoService;
    @Autowired
    private Class_infoService class_infoService;
    @Autowired
    private Recommend recommendservice;
    //根据传入的商品id获取商品的明细信息，显示
    //给show_sku_detail.html页面
    @RequestMapping("show_sku_info_detail")
    public String showSku_infoDetail(HttpServletRequest request, Model model){
        int sku_id = Integer.parseInt(request.getParameter("sku_id"));
        //System.out.println("..........." + sku_id);
        Sku_info sku_info = sku_infoService.showSku_info_Detail(sku_id);
        int userid=0;
        if(request.getSession().getAttribute("user_id")!=null){
            userid=(int)request.getSession().getAttribute("user_id");
        }
        //Sku_info skuinfo=sku_info.get(0);
        //String[] prodcut_property_values = sku_info.getProduct_property_infoList().get(0).getProperty_value().split("\\|");
        //model.addAttribute("arr_ppv", prodcut_property_values);
        model.addAttribute("sku_info", sku_info);
        //model.addAttribute("property",sku_info);
        try {
            List<RecommendedItem> ans=new ArrayList<>();
            if(userid==0){
                ans=recommendservice.recommend(1,sku_id);
            }
            else{
                ans=recommendservice.recommend(userid,sku_id);
            }
            List<Sku_info>recommendans=new ArrayList<Sku_info> ();;
            for (int i=0;i<ans.size();i++){
                //System.out.println("id="+ans.get(i).getItemID()+" value="+ans.get(i).getValue());
                Sku_info temp=sku_infoService.showSku_info_Detail((int)ans.get(i).getItemID());
                recommendans.add(temp);
            }
            //System.out.println(recommendans);
            for(int i=0;i<recommendans.size();i++){
                //System.out.println(recommendans.get(i).getSku_id()+" "+recommendans.get(i).getP_image_path());
            }
            model.addAttribute("recommendans",recommendans);
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
    public String qry_Sku_info(HttpServletRequest request, Model model,@RequestParam(required = false,defaultValue = "1",value = "pn")Integer pn,
                               Map<String,Object> map){

        String class_id = request.getParameter("class_id").toString();
        String sortField = request.getParameter("sortField");
        String sortMode = request.getParameter("sortMode");
        String sort = "s." + sortField + " " + sortMode;
        //System.out.println(sort);
        PageHelper.startPage(pn,9);

        List<Sku_info> sku_infoList = sku_infoService.qry_Sku_info(class_id,sort);
        PageInfo pageInfo = new PageInfo<>(sku_infoList,5);
        //System.out.println(pageInfo);
        List<Class_info>classinfo=class_infoService.class_all();
        map.put("pageInfo",pageInfo);
        //System.out.println(sku_infoList.size());
        //System.out.println(sku_infoList.get(0).getProduct_class_name());
        model.addAttribute("sku_infoList", sku_infoList);

        model.addAttribute("classinfo", classinfo);//左边的分类列
        return "commodity";
    }
}
