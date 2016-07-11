package sample.model;


import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by ParkCheolHo on 2016-02-27.
 * Xml파일 관련 클래스
 */
class MakeXml {

    OutputStream outputStream;
    XMLStreamWriter out;

    public MakeXml(File filepath) {
        try {
            outputStream = new FileOutputStream(filepath);
            out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Start() {
        try {
            out.writeStartDocument("UTF-8","1.0");
            out.writeStartElement("Movies");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public synchronized void add(String index, String name, String engName, int countrycode,
                                 String h_tx_story, String con_tx, ArrayList<Actor> actors, ArrayList<String> title, ArrayList<Integer> genre) {
        try {
            out.writeStartElement("Movie");
                out.writeAttribute("index", index);
//
                out.writeStartElement("Name");
                out.writeCharacters(name);
                out.writeEndElement();
//
                out.writeStartElement("engName");
                out.writeCharacters(engName);
                out.writeEndElement();
//
                out.writeStartElement("genres");
                for(int value : genre){
                    out.writeStartElement("genre");
                    out.writeCharacters(String.valueOf(value));
                    out.writeEndElement();
                }
                out.writeEndElement();

                out.writeStartElement("story");
                out.writeCharacters(h_tx_story);
                out.writeEndElement();
//
                out.writeStartElement("Appear");
                    for(int i =0;i<actors.size();i++){
                        out.writeStartElement(title.get(i));
                        out.writeCharacters(actors.get(i).name);
                        out.writeEndElement();
                    }
                out.writeEndElement();

                out.writeStartElement("country");
                out.writeCharacters(String.valueOf(countrycode));
                out.writeEndElement();
            out.writeEndElement();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
//        System.out.println("-----------------------------------------------------------------------");
//        System.out.println(index + "\n" + name + "\n" + engName + "\n" + countrycode + "\n" + h_tx_story + "\n" + con_tx);
//        System.out.println("''''''''''''''''''''''");
//        for (int i = 0; i < actors.size(); i++) System.out.println(actors.get(i));
//        for (String value : genre) {
//            System.out.println(value);
//        }
//        for (String value : title) {
//            System.out.println(value);
//        }
//        System.out.println("////////////////////////////////////////////////////////////////////////");

    }


    public void End() {
        try {
            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            outputStream.close();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
