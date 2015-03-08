/*
 * Copyright (C) 2015 Cedric
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.polytech.drivers;

/**
 *
 * @author Cedric
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import jssc.SerialPortException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
 
public class VibratingPieceGraph extends JPanel implements Observer{

    
    static TimeSeries ts = new TimeSeries("", Millisecond.class);
    
    public static void main(String[] args) throws InterruptedException {
         try {
             Driver driver = new TestPlaqueVibranteDriver();
             
             if (driver.doConnect("COM3"))
                 System.out.println(driver+"> Now connected to the device on port ");
             else
                 System.err.println(driver+"> Fail to connect to the device on port");
             VibratingPieceGraph graph = new VibratingPieceGraph();
             driver.addObserver(graph);
             
             
         } catch (SerialPortException ex) {
             Logger.getLogger(VibratingPieceGraph.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    
    public VibratingPieceGraph(){
        super();
        TimeSeriesCollection dataset = new TimeSeriesCollection(ts);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                     "",
                     "Time",
                     "Value",
                     dataset,
                     true,
                     true,
                     false
             );
             final XYPlot plot = chart.getXYPlot();
             ValueAxis axis = plot.getDomainAxis();
             axis.setAutoRange(true);
             axis.setFixedAutoRange(60000.0);
 
             ChartPanel chartPnl = new ChartPanel(chart);
             setLayout(new BorderLayout());
             add(chartPnl, BorderLayout.CENTER);
             this.setVisible(true);
             chartPnl.setPreferredSize(new Dimension(this.getPreferredSize().width,150));
    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Byte) {
            Byte msg = (Byte)arg;
            int test = msg.intValue();
            test+=128;
            ts.addOrUpdate(new Millisecond(), test);
        }
    }
}
