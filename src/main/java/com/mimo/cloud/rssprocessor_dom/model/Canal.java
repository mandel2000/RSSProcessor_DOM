/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mimo.cloud.rssprocessor_dom.model;

import com.mimo.cloud.rssprocessor_dom.utils.XmlDomUtils;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author mande
 */
public class Canal {

    private String titulo;
    private String url;
    private String descripcion;
    private ArrayList<Noticia> noticias;

    public Canal() {
        this.noticias = new ArrayList<>();
    }

    public Canal(String titulo, String url, String descripcion, ArrayList<Noticia> noticias) {
        this.titulo = titulo;
        this.url = url;
        this.descripcion = descripcion;
        this.noticias = noticias;
    }

    public Canal(Node node) {

        if (node.hasChildNodes()) {

            this.noticias = new ArrayList<>();

            for (int i = 0; i < node.getChildNodes().getLength(); i++) {

                Node childNode = node.getChildNodes().item(i);

                switch (childNode.getNodeName()) {

                    case "title":
                        this.titulo = childNode.getTextContent();
                        break;

                    case "link":
                        this.url = childNode.getTextContent();
                        break;

                    case "description":
                        this.descripcion = childNode.getTextContent();
                        break;

                    case "item":
                        this.noticias.add(new Noticia(childNode));
                        break;

                    default:

                }
            }
        }
    }

    public Node toXmlNode() throws ParserConfigurationException {

        Document doc = XmlDomUtils.createEmptyDocument();

        Element parentNode = doc.createElement("noticias");
        parentNode.setAttribute("canal", this.titulo);

        this.noticias.forEach(noticia -> {

            try {

                parentNode.appendChild(doc.importNode(noticia.toXmlNode(), true));

            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Canal.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        return parentNode;

    }

    public JSONObject toJson() {

        JSONObject json = new JSONObject();

        JSONArray jsonArray = new JSONArray(this.noticias.stream().map(noticia -> noticia.toJson()).collect(Collectors.toList()));

        json.put("noticias", jsonArray);

        return json;

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Noticia> getNoticias() {
        return noticias;
    }

    public void setNoticias(ArrayList<Noticia> noticias) {
        this.noticias = noticias;
    }

}
