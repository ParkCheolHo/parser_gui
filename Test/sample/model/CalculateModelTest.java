package sample.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by beakya on 2016. 12. 19..
 */
public class CalculateModelTest {
    @Test
    public void GetMovieDataTest () throws InterruptedException {
        CalculateModel cal = new CalculateModel("2015" ,1, 10 , new RootThread("2015"), new MakeXml(null));
        cal.getMovieData("132933", new InformationParser());
    }

}