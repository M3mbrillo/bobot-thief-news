package com.facundo;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        long time_start, time_end;
        time_start = System.currentTimeMillis();

        //Obtengo las notas
        SiteMap sitemap = new SiteMap();
        List<News> news_List = sitemap.GetNews();

        //las proceso y descargo
        News news;
        for (int i= 0; i < news_List.size(); i++){
            news = news_List.get(i);

            System.out.print("Nota " + i + " / " + news_List.size() + " : " + news.title);
            news.DownloadNote();
            news.SaveInFile();
            System.out.println(" - Finish");
        }

        time_end = System.currentTimeMillis();
        System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
    }
}
