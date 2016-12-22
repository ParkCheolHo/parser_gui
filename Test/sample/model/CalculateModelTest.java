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
        Movie movie = cal.getMovieData("125709", new InformationParser());
        movie.printAll();
        int testVal = cal.getImageHeight(movie.getImgAddress());
        assertTrue(testVal > 200  && testVal < 300);
    }

}