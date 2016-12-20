package sample.model;


import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by ParkCheolHo on 2016-02-27.
 * Xml파일 관련 클래스
 */
class MakeXml implements WriteFile {

    private OutputStream outputStream;
    private XMLStreamWriter out;
    MakeXml(File filepath) {
        try {
//            outputStream = new FileOutputStream(filepath);
//            out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int start(boolean flag) {
        try {
            out.writeStartDocument("UTF-8","1.0");
            out.writeStartElement("Movies");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public synchronized void add(String index, String name, String eng_name, int country,
                                 String story_name, String story, InformationParser reader, ArrayList<Actor> actors, ArrayList<String> title, int year) {
        try {
            out.writeStartElement("Movie");
                out.writeAttribute("index", index);

                out.writeStartElement("Name");
                    out.writeCharacters(name);
                out.writeEndElement();

                out.writeStartElement("Year");
                    out.writeCharacters(String.valueOf(year));
                out.writeEndElement();

                out.writeStartElement("engName");
                    out.writeCharacters(eng_name);
                out.writeEndElement();

                out.writeStartElement("countryNum");
                    out.writeCharacters(String.valueOf(country));
                out.writeEndElement();

                out.writeStartElement("genres");
                for(int value : reader.GetGenreList()){
                    out.writeStartElement("genre");
                    out.writeCharacters(String.valueOf(value));
                    out.writeEndElement();
                }
                out.writeEndElement();

                out.writeStartElement("story");
                out.writeCharacters(story);
                out.writeEndElement();

                out.writeStartElement("Appear");
                    for(int i =0;i<actors.size();i++){
                        out.writeStartElement(title.get(i));
                        out.writeCharacters(actors.get(i).name);
                        out.writeEndElement();
                    }
                out.writeEndElement();
            out.writeEndElement();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
//        System.out.println("-----------------------------------------------------------------------");
//        System.out.println(index + "\n" + name + "\n" + engName + "\n" + getCountry + "\n" + h_tx_story + "\n" + con_tx);
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
    public void end() {
        try {
            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            outputStream.close();
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }
}
