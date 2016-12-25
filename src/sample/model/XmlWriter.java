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
class XmlWriter implements FileWriter {

    private OutputStream outputStream;
    private XMLStreamWriter out;
    private File file;
    XmlWriter(File file) {
        this.file = file;
    }
    @Override
    public int start() {
        try {
            outputStream = new FileOutputStream(file);
            out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            out.writeStartDocument("UTF-8","1.0");
            out.writeStartElement("Movies");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Object startup() {
        return null;
    }

    @Override
    public synchronized void add(Movie movie) {
        int open_year = Integer.parseInt(movie.getOpeningDate().substring(0,4));
        int open_month = Integer.parseInt(movie.getOpeningDate().substring(4,6));
        int open_day = Integer.parseInt(movie.getOpeningDate().substring(6,8));
        try {
            out.writeStartElement("Movie");
                out.writeAttribute("index", movie.getMovieIndex());

                out.writeStartElement("Name");
                    out.writeCharacters(movie.getTitle());
                out.writeEndElement();

                out.writeStartElement("Year");
                    out.writeCharacters(String.valueOf(open_year));
                out.writeEndElement();

                out.writeStartElement("engName");
                    out.writeCharacters(movie.getEngTitle());
                out.writeEndElement();

                out.writeStartElement("countryNum");
                    out.writeCharacters(String.valueOf(movie.getCountry()));
                out.writeEndElement();



                out.writeStartElement("genres");
                for(Integer value : movie.getGenre().keySet()){
                    out.writeStartElement("genre");
                    out.writeCharacters(String.valueOf(value));
                    out.writeEndElement();
                }
                out.writeEndElement();

                out.writeStartElement("story");
                out.writeCharacters(movie.getSummary());
                out.writeEndElement();

                out.writeStartElement("Appear");
                    for(int i =0;i<movie.getActors().size();i++){
                        out.writeStartElement(String.valueOf(movie.getActors().get(i).getIndex()));
                        out.writeCharacters(movie.getActors().get(i).getName());
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
