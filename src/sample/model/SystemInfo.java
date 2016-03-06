package sample.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ParkCheolHo on 2016-03-05.
 */
public class SystemInfo {
    private static SystemInfo ourInstance = new SystemInfo();

    public static SystemInfo getInstance() {
        return ourInstance;
    }

    private SystemInfo() {
    }
    private List<Thread> theadlist = new ArrayList<>();

    public void addTheadlist(Thread value) {
        this.theadlist.add(value);
    }
    public void clearTheadlist(Thread value) {
        this.theadlist.clear();
    }

    public List getTheadlist() {
        return theadlist;
    }
    public void startThreadlist(){
        theadlist.forEach(Thread::start);
    }
}
