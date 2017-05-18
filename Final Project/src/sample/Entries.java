package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class Entries {
    private IntegerProperty ipdst;
    private IntegerProperty ipsrc;
    private IntegerProperty size;
    private IntegerProperty framenum;
    private LongProperty time;

    Entries(int src, int dst, long t, int frame, int samplesize){
        ipdst = new SimpleIntegerProperty(dst);
        ipsrc =new SimpleIntegerProperty(src);
        time = new SimpleLongProperty(t);
        framenum  = new SimpleIntegerProperty(frame);
        size = new SimpleIntegerProperty(samplesize);
    }

    public IntegerProperty getIpdst(){
        return ipdst;
    }

    public IntegerProperty getIpsrc(){
        return ipsrc;
    }

    public IntegerProperty getFramenum(){
        return framenum;
    }

    public LongProperty getTime(){
        return time;
    }

    public IntegerProperty getSize(){
        return size;
    }
}
