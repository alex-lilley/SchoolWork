package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.util.PcapPacketArrayList;
import sample.Filereading.PcapFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    private static File file;
    private static ArrayList<File> files = new ArrayList<>();
    private String content = "t";

    public void fileChoose() throws Exception {
        FileChooser chooser = new FileChooser();
        try {
            chooser.setTitle("Open File");
            file = chooser.showOpenDialog(new Stage());

            Tab tab = new Tab();
            tab.setText(file.getName());
            tab.setId(file.toString());

            if (content.equals("t")) {
                changeFile(tab.getId());
                tab.setContent(gettable());
            } else if (content.equals("l")) {
                changeFile(tab.getId());
                tab.setContent(LineChart());
            } else if (content.equals("p")) {
                changeFile(tab.getId());
                tab.setContent(PieChart.pieChart(file));
            }

            files.add(file);
            Main.createTab(tab);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void quit() {
        System.exit(1);
    }

    public static void changeFile(String newFile) {
        for (File f : files) {
            if (f.toString().equals(newFile)) {
                file = f;
            }
        }
    }

    public TableView gettable() throws IllegalAccessException {
        int i = 0;
        PcapFile myfile = new PcapFile(file.toString());
        PcapPacketArrayList mypackets = myfile.readOfflineFiles();

        TableView<Entries> table = new TableView<Entries>();

        TableColumn<Entries, Number> framnenum = new TableColumn<>("Frame #");
        TableColumn<Entries, Number> sourceip = new TableColumn<>("Source");
        TableColumn<Entries, Number> destinatonip = new TableColumn<>("Destination");
        TableColumn<Entries, Number> time = new TableColumn<>("Time (Microseconds)");
        TableColumn<Entries, Number> size = new TableColumn<>("Size(Bytes)");

        framnenum.setCellValueFactory(celldata->celldata.getValue().getFramenum());
        sourceip.setCellValueFactory(celldata->celldata.getValue().getIpsrc());
        destinatonip.setCellValueFactory(celldata->celldata.getValue().getIpdst());
        time.setCellValueFactory(celldata->celldata.getValue().getTime());
        size.setCellValueFactory(celldata->celldata.getValue().getSize());

        ArrayList<Long> timeDifs = new ArrayList<>(FindTimeDiff.findTimeDif(mypackets));
        ArrayList<ArrayList> address = new ArrayList<>(getIP(mypackets));
        ArrayList<Integer> sizes = new ArrayList<>(getsize(mypackets));
        ObservableList<Entries> inputdata = FXCollections.observableArrayList();
        ArrayList<Integer> srcs = address.get(0);
        ArrayList<Integer> dsts = address.get(1);

        for(Long in:timeDifs){
            Entries sample = new Entries(srcs.get(i),dsts.get(i) ,in,i+1,sizes.get(i));
            inputdata.add(sample);
            i+=1;
        }

        table.getColumns().addAll(framnenum,time,sourceip,destinatonip,size);
        table.setItems(inputdata);
        return table;
    }

    public ArrayList<ArrayList> getIP(PcapPacketArrayList list){
        ArrayList listlist = new ArrayList();
        ArrayList<Integer>ipsrc=new ArrayList<>();
        ArrayList<Integer>ipdst = new ArrayList<>();

        Tcp tcp = new Tcp();
        Udp udp = new Udp();
        Arp arp = new Arp();

        for (PcapPacket s : list){
            if (s.hasHeader(tcp)){
                ipdst.add(s.getHeader(tcp).destination());
                ipsrc.add(s.getHeader(tcp).source());
            }else {
                if (s.hasHeader(udp)){
                    ipdst.add(s.getHeader(udp).destination());
                    ipsrc.add(s.getHeader(udp).source());
                } else{
                    if(s.hasHeader(arp)){
                        ipdst.add(0);
                        ipsrc.add(0);
                    }else{
                        ipdst.add(0);
                        ipsrc.add(0);}}}
        }

        listlist.add(ipdst);
        listlist.add(ipsrc);

        return listlist;

    }

    public ArrayList<Integer> getsize(PcapPacketArrayList list){
        ArrayList<Integer> listlist= new ArrayList();

        for (PcapPacket s : list){
            listlist.add(s.size());
        }

        return listlist;
    }

    public LineChart LineChart() throws Exception {
        PcapFile myfile = new PcapFile(file.toString());
        PcapPacketArrayList mypackets = myfile.readOfflineFiles();
        ArrayList<Long> timeDifs = new ArrayList<>(FindTimeDiff.findTimeDif(mypackets));

        NumberAxis xAxis = new NumberAxis(0, timeDifs.size(), 10);

        xAxis.setLabel("Packet Num");
        Long maxValue = 0L;

        int index = 0;
        timeDifs.set(0, 0L);

        for(Long entry: timeDifs){
            if (entry > maxValue){
                maxValue = entry;
            }
        }

        NumberAxis yAxis = new NumberAxis(0, maxValue*1.1, maxValue/25);
        yAxis.setLabel("Time (MicroSeconds)");
        LineChart linechart1 = new LineChart(xAxis, yAxis);
        linechart1.setTitle("Times in microseconds");
        XYChart.Series series = new XYChart.Series();
        linechart1.setCreateSymbols(false);
        linechart1.setLegendVisible(false);

        for(Long entry : timeDifs){
            index+=1;
            series.getData().add(new XYChart.Data<>(index, entry));
        }

        linechart1.getData().add(series);
        return linechart1;
    }

    public void compare() throws IOException {
        Compare compare = new Compare();
        compare.Compare(files);
    }

    public void createLineGraph() throws Exception {
        try {
            TabPane tabPane = Main.getTabPane();

            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                Tab tab = tabPane.getTabs().get(i);
                changeFile(tab.getId());
                tab.setContent(LineChart());
            }

            content = "l";
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createTable() throws Exception {
        try {
            TabPane tabPane = Main.getTabPane();

            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                Tab tab = tabPane.getTabs().get(i);
                changeFile(tab.getId());
                tab.setContent(gettable());
            }

            content = "t";
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createPieChart() throws Exception {
        try {
            TabPane tabPane = Main.getTabPane();

            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                Tab tab = tabPane.getTabs().get(i);
                changeFile(tab.getId());
                tab.setContent(PieChart.pieChart(file));
            }

            content = "p";
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
