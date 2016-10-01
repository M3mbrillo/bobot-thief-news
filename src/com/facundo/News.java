package com.facundo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.facundo.DiarioRequest;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by facundo on 11/08/15.
 */
public class News {
    //Sacados del xml
    public String url = "";
    public String title = "";
    public List<String> url_images;
    public String epigrafe;
    public String seccion = "";

    //Sacados del http
    public String bajada = "";
    public String texto = "";
    public String autor = "";

    public DiarioRequest diarioRequest;
    public String path;

    public News() throws Exception{
        this.url_images = new ArrayList<String>();
        this.diarioRequest = new DiarioRequest();
        this.path = "";

        this.epigrafe = "Material extraido desde la web. Foto de autor desconocido";
        this.autor = "Autor anonimo";
    }

    enum NEWS_STATUS{
        NEWS_OK,
        NEWS_ERROR
    }

    public NEWS_STATUS CheckNews(){
        //comprueba los datos que obtuve
        if (this.texto.isEmpty() ||
                this.seccion.isEmpty() ||
                this.title.isEmpty() ||
                this.bajada.isEmpty() ||
                this.autor.isEmpty() ||
                this.epigrafe.isEmpty()
                ){
            return NEWS_STATUS.NEWS_ERROR;
        }

        return NEWS_STATUS.NEWS_OK;
    }

    public void SaveInFile() throws Exception{
        if (this.CheckNews() == NEWS_STATUS.NEWS_OK)
        {
            //creo el directorio
            this.path = "Notas/" + this.seccion + "/" + this.title + "/";
            File path_file = new File(this.path);
            path_file.mkdirs();

            Path archivo = Paths.get(this.path + this.title + ".txt");
            String string_file = new String(
                    Files.readAllBytes(Paths.get("template.txt")),
                    StandardCharsets.UTF_8
            );

            string_file = string_file.replace("[%%%-NOTA-%%%]", this.texto);
            string_file = string_file.replace("[%%%-FIRMA-%%%]", this.autor);
            string_file = string_file.replace("[%%%-BAJADA-%%%]", this.bajada);
            string_file = string_file.replace("[%%%-IMAGEN_1-%%%]", "");
            string_file = string_file.replace("[%%%-EPIGRAFE_1-%%%]", this.epigrafe);
            string_file = string_file.replace("[%%%-IMAGEN_2-%%%]", "");
            string_file = string_file.replace("[%%%-EPIGRAFE_2-%%%]", this.epigrafe);
            string_file = string_file.replace("[%%%-IMAGEN_3-%%%]", "");
            string_file = string_file.replace("[%%%-EPIGRAFE_3-%%%]", this.epigrafe);

            //grabo el archivo
            Files.write(archivo, string_file.getBytes());
            this.DownloadAllImage();
        }
    }

    public void DownloadNote() throws Exception{
        Document document;

        document = Jsoup.connect(this.url).timeout(1000000).get();
        if (!document.select("p.firma a b").text().isEmpty()){
            this.autor = document.select("p.firma a b").text();
        }

        if (!document.select("p.bajada").text().isEmpty()){
            this.bajada = document.select("p.bajada").text();
        }
        //extraigo el texto de todos los parrafos
        for (Element parrafo : document.select("#cuerpo p")){
            if (parrafo.text() != null){
                this.texto += "\n" + parrafo.text();
            }
        }

        if (!document.select("figcaption").text().isEmpty()){
            this.epigrafe = document.select("figcaption").text();
        }
    }

    public void DownloadAllImage() throws Exception{
        for (String image : this.url_images) {
            this.diarioRequest.DownloadImageNews(image, this.path);
        }
    }
}
