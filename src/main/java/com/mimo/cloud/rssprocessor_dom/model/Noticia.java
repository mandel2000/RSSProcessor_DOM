/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mimo.cloud.rssprocessor_dom.model;

import com.mimo.cloud.rssprocessor_dom.utils.XmlDomUtils;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author mande
 */
public class Noticia {

    private String titulo;
    private String url;
    private String descripcion;
    private String fecha_publicacion;
    private String categoría;

    public Noticia() {
    }

    public Noticia(String titulo, String url, String descripcion, String fecha_publicacion, String categoría) {
        this.titulo = titulo;
        this.url = url;
        this.descripcion = descripcion;
        this.fecha_publicacion = fecha_publicacion;
        this.categoría = categoría;
    }

    public Noticia(Node node) {

        if (node.hasChildNodes()) {

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

                    case "pubDate":
                        this.fecha_publicacion = childNode.getTextContent();
                        break;

                    case "category":
                        this.categoría = childNode.getTextContent();
                        break;

                    default:

                }
            }
        }
    }

    public Node toXmlNode() throws ParserConfigurationException {

        Document doc = XmlDomUtils.createEmptyDocument();

        Element parentNode = doc.createElement("noticia");
        parentNode.setTextContent(this.titulo);

        return parentNode.cloneNode(true);

    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("noticia", this.titulo);
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

    public String getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public String getCategoría() {
        return categoría;
    }

    public void setCategoría(String categoría) {
        this.categoría = categoría;
    }

}
