package sample;

import org.jnetpcap.PcapHeader;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.util.PcapPacketArrayList;
import java.util.ArrayList;

public class FindTimeDiff {
    public static ArrayList<Long> findTimeDif (PcapPacketArrayList list) {
        long previousNum = 0;
        long currentNum;
        ArrayList<Long> timeDifs = new ArrayList<>();
        for (PcapPacket s : list) {
            PcapHeader x = s.getCaptureHeader();
            if (s.getFrameNumber() == 1) {
                previousNum = x.timestampInMicros();
            }
            currentNum = x.timestampInMicros();
            timeDifs.add(currentNum - previousNum);
            previousNum = currentNum;
        }
        return timeDifs;
    }
}
