package sample.model;

import java.io.File;
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
    private File filepath = null;
    public void addTheadlist(Thread value) {
        this.theadlist.add(value);
    }
    public void clearTheadlist(Thread value) {
        this.theadlist.clear();
    }
    public File getFilePath(){ return filepath;}
    public boolean filpathempty(){
        if(filepath==null)
            return true;
        else
            return false;
    }
    public void setFilePath(File filepath){this.filepath = filepath; }
    public List getTheadlist() {
        return theadlist;
    }
    public void startThreadlist(){
        theadlist.forEach(Thread::start);
    }
}
