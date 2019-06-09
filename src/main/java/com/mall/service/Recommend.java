package com.mall.service;

import com.mall.entity.Ratings_info;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Service
public class Recommend {
    public List<RecommendedItem> recommend(long userid,long itemid)throws Exception{
        DataModel dataModel=null;
        try {
            //File file = new File("ratings.dat");
            //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
            dataModel = new FileDataModel(new File("C:\\D\\课件\\企业级javaee\\推荐算法学习\\ml-latest-small\\ra.csv"));
            //计算相似度
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
            //System.out.println(itemSimilarity.allSimilarItemIDs(6).toString());
            //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于物品的协同过滤推荐
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
            //给用户ID等于5的用户推荐10个与2398相似的商品
            List<RecommendedItem> recommendedItemList = recommender.recommendedBecause(userid, itemid, 6);

            //打印推荐的结果
            //System.out.println("使用基于物品的协同过滤算法");
            //System.out.println("根据用户5当前浏览的商品2398，推荐10个相似的商品");
            for (RecommendedItem recommendedItem : recommendedItemList){
                System.out.println(recommendedItem);
            }
//            long start = System.currentTimeMillis();
            return recommendedItemList;
        }catch (IOException e){
            System.out.println(e);
            return null;
        }



    }
}
