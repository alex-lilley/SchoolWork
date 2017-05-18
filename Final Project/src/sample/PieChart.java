package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jnetpcap.util.PcapPacketArrayList;
import sample.Filereading.PcapFile;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PieChart {
    private static DecimalFormat f = new DecimalFormat("##.00");

    public static javafx.scene.chart.PieChart pieChart(File file) throws Exception {
        PcapFile myfile = new PcapFile(file.toString());
        PcapPacketArrayList mypackets = myfile.readOfflineFiles();
        ArrayList<Double> list = FindTypes.findTypes(mypackets);
        ObservableList<javafx.scene.chart.PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new javafx.scene.chart.PieChart.Data("TCP "+f.format((list.get(0)/list.get(2))*100)+"% ("+ Math.round(list.get(0))+")", list.get(0)));
        pieChartData.add(new javafx.scene.chart.PieChart.Data("UDP "+f.format((list.get(1)/list.get(2))*100)+"% ("+ Math.round(list.get(1))+")", list.get(1)));

        final javafx.scene.chart.PieChart piechart1 = new javafx.scene.chart.PieChart(pieChartData);
        piechart1.setTitle("Categories");

        return piechart1;
    }
}
