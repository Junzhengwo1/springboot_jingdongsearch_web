package com.kou.util;

import com.kou.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JIAJUN KOU
 *
 * 网页解析工具类
 */
@Component
public class HtmlParseUtil {

    //获取请求 需要联网，不能获取ajax数据

    public static List<Content> parseJD(String keywords) throws IOException {
        String url="https://search.jd.com/Search?keyword="+keywords+"&enc=utf-8";
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        //获取所有的li标签
        Elements lis = element.getElementsByTag("li");

        ArrayList<Content> goodsList=new ArrayList<>();
        //获取元素中的内容
        for (Element li : lis) {
            String img=li.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price=li.getElementsByClass("p-price").eq(0).text();
            String title=li.getElementsByClass("p-name").eq(0).text();

            Content content=new Content();
            content.setTitle(title);
            content.setImg(img);
            content.setPrice(price);

            goodsList.add(content);

        }

        return goodsList;
    }


    public static void main(String[] args) throws IOException {

        HtmlParseUtil.parseJD("java").forEach(System.out::println);


    }





}
