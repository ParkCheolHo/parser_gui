package sample.model;

import java.util.ArrayList;

/**
 * Created by ParkCheolHo on 2016-03-15.
 */
public interface GetMovieData {
    public void getSpecificMovieData(MakeXml xmlwriter, ArrayList<String> moviesurl, ArrayList<String> errors, boolean flag) throws InterruptedException ;
}
