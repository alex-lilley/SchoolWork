package sample.Filereading;

/**
 * Created by ethan on 3/28/2017.
 */



import org.jnetpcap.*;
import org.jnetpcap.packet.*;
import org.jnetpcap.util.PcapPacketArrayList;

 /**
 13   * @author Emad Heydari Beni
 14   * Doing some IO functions related to PCAP files.
 15   */
public class PcapFile {
/*************************************************
9     * Local Variables
 20     *************************************************/
String FileAddress = "test1.pcap";
    /**
 25     *
 26     * @param FileAddress  Address and the name of the PCAP file.
 27     */
            public PcapFile(String FileAddress)
  {
              this.FileAddress = FileAddress;
            }

            /**
 34     * Opens the offline Pcap-formatted file.
 35     *
 36     * @return PcapPacketArrayList  List of packets in the file
 37     * @throws ExceptionReadingPcapFiles Facing any erro in opening the file
 38     */
            public PcapPacketArrayList readOfflineFiles() throws IllegalAccessException
{
      //First, setup error buffer and name for our file
      final StringBuilder errbuf = new StringBuilder(); // For any error msgs
      //Second ,open up the selected file using openOffline call
      Pcap pcap = Pcap.openOffline(FileAddress, errbuf);

      //Throw exception if it cannot open the file
     if (pcap == null) {
              throw new IllegalAccessException(errbuf.toString());
         }

      //Next, we create a packet handler which will receive packets from the libpcap loop.
      PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>() {

             public void nextPacket(PcapPacket packet, PcapPacketArrayList PaketsList) {

                  PaketsList.add(packet);
              }
          };

         /***************************************************************************
         62           * (From jNetPcap comments:)
         63           * Fourth we enter the loop and tell it to capture unlimited packets. The loop
         64           * method does a mapping of pcap.datalink() DLT value to JProtocol ID, which
         65           * is needed by JScanner. The scanner scans the packet buffer and decodes
         66           * the headers. The mapping is done automatically, although a variation on
         67           * the loop method exists that allows the programmer to specify exactly
         68           * which protocol ID to use as the data link type for this pcap interface.
         69           **************************************************************************/

         try {
                      PcapPacketArrayList packets = new PcapPacketArrayList();
                     pcap.loop(-1,jpacketHandler,packets);
                    return packets;
                   } finally {
                       //Last thing to do is close the pcap handle
                       pcap.close();
         }
}
        }
