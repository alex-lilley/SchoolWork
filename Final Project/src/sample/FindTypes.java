package sample;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.util.PcapPacketArrayList;

import java.util.ArrayList;

public class FindTypes {
    public static ArrayList<Double> findTypes(PcapPacketArrayList list) {
        double tcpcount = 0;
        double udpcount = 0;
        ArrayList<Double> totals = new ArrayList<>();
        Tcp tcp = new Tcp();
        Udp udp = new Udp();
        for (PcapPacket s : list){
            if (s.hasHeader(tcp)) {
                tcpcount=tcpcount+1;
            }
            if (s.hasHeader(udp)){
                udpcount=udpcount+1;
            }

        }

        totals.add(tcpcount);
        totals.add(udpcount);
        totals.add(tcpcount+udpcount);

        return totals;
    }
}
