package com.facundo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class SiteMap {

    private Document document;

    public SiteMap() throws Exception{

        File file_sitemap = new File("sitemap-news.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.document = dBuilder.parse(file_sitemap);
        this.document.normalize();
    }

    public List<News> GetNews() throws Exception{
        NodeList urls = this.document.getElementsByTagName("url");
        News news;
        List<News> newsList = new ArrayList<News>();

        for (int index = 0; index < urls.getLength() ; index++){
            Node node = urls.item(index);
            news = new News();

            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                news.url = element.getElementsByTagName("loc").item(0).getTextContent();
                news.title = element.getElementsByTagName("news:title").item(0).getTextContent();
                news.seccion = element.getElementsByTagName("news:keywords").item(0).getTextContent();

                NodeList nodeImageList = element.getElementsByTagName("image:loc");
                //for (int y = 0;y < 3 && y < nodeImageList.getLength(); y++){
                for (int y = 0;y < nodeImageList.getLength(); y++){
                    news.url_images.add(nodeImageList.item(y).getTextContent());
                }
            }

            //si no tienen 3 o mas fotos no me interesan
            if(news.url_images.size() >= 3){
                newsList.add(news);
            }
        }

        return newsList;
    }

}
