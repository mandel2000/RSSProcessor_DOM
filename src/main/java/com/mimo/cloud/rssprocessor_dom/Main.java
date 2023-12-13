/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mimo.cloud.rssprocessor_dom;

import com.mimo.cloud.rssprocessor_dom.model.Canal;
import com.mimo.cloud.rssprocessor_dom.model.Noticia;
import com.mimo.cloud.rssprocessor_dom.utils.Constants;
import com.mimo.cloud.rssprocessor_dom.utils.XmlDomUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Manuel Delgado Sánchez
 */
public class Main {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        System.out.println("Comienza el programa");

        Document response = XmlDomUtils.parseStringToDocument(getFromUrl(Constants.url));

        NodeList channelNode = response.getElementsByTagName("channel");

        if (channelNode.getLength() > 0) {

            Canal canal = new Canal(channelNode.item(0));

            showChannelInfo(canal);

            writeXmlFile(canal, Constants.xmlFilePath);

            writeJsonFile(canal, Constants.jsonFilePath);

        } else {
            System.out.println("La respuesta recibida no contiene un nodo 'noticias'.");
        }
    }

    private static void showChannelInfo(Canal canal) {

        System.out.println("\n  Información del Canal ");
        System.out.println(" ----------------------- ");
        System.out.println("  Título: " + canal.getTitulo());
        System.out.println("  URL: " + canal.getUrl());
        System.out.println("  Descripción: " + canal.getDescripcion());
        System.out.println("  Noticias: ");

        int i = 0;

        for (Noticia noticia : canal.getNoticias()) {
            System.out.println("\n\t Noticia " + ++i);
            System.out.println("\t| Titulo: " + noticia.getTitulo());
            System.out.println("\t| URL: " + noticia.getUrl());
            System.out.println("\t| Descripción: " + noticia.getDescripcion());
            System.out.println("\t| Fecha de publicación: " + noticia.getFecha_publicacion());
            System.out.println("\t| Categoría: " + noticia.getCategoría());
        }

    }

    private static void writeXmlFile(Canal canal, String xmlFilePath) {

        try {

            Document doc = XmlDomUtils.createEmptyDocument();

            //Element root = doc.createElement("root");
            //root.appendChild(doc.importNode(canal.toXmlNode(), true));
            //doc.appendChild(root);
            doc.appendChild(doc.importNode(canal.toXmlNode(), true));

            XmlDomUtils.writeXmlToFile(doc, String.format(Constants.xmlFilePath, canal.getTitulo()));

        } catch (ParserConfigurationException | TransformerException ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error creating xml file", ex);

        }
    }

    private static void writeJsonFile(Canal canal, String jsonFilePath) {

        try {
            String content = canal.toJson().toString(4);
            String fileName = String.format(jsonFilePath, canal.getTitulo());

            FileUtils.writeStringToFile(new File(fileName), content, "UTF-8");

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error creating json file", ex);
        }

    }

    private static String getFromUrl(String urlAsString) throws IOException {

        StringBuilder body = new StringBuilder();

        URL url = new URL(urlAsString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int status = conn.getResponseCode();

        try ( BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")))) {

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                body.append(inputLine);
            }

            System.out.println("Response - " + body.toString());
            in.close();

            conn.disconnect();

            return body.toString();
        }

    }

}
