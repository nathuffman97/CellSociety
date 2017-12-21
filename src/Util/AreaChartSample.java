package Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;

/**
 * Class AreaChartSample Purpose: To set up and plot the number of cells from each cell state in the simulation over time.
 * Assumptions: The Game class will be passing the list of cellState names so that each series in the graph can be labeled accordingly. It will also
 * be passing a string for the title of the graph. If either of these are null or of invalid data types, the class constructor would not work.
 * Dependencies: Depends on CellGroup data for the number of cells in each state and the Game class to pass on this information.
 * Example of how to use it: create the chart, graph/don't graph according to pauses, add data to graph every step
 * Credit to a StackOverFlow post--borrowed a lot of code from there
 * 
 * @author Owen Smith
 *
 */

public class AreaChartSample {
    private int xSeriesData=0;
    private int maxWidth = 500;
    private int maxHeight = 155;
    private int startX = 165;
    private int startY = 0;
    private List<Series<Number, Number>> series = new ArrayList<Series<Number, Number>>();
    private AreaChart<Number,Number> sc;
	
	public AreaChartSample(List<String> cellStateNames,String title) {
		NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRanging(true);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        //-- Chart
        sc = new AreaChart<Number,Number>(xAxis,yAxis);
        sc.setId("liveAreaChart");
        sc.setTitle(title);

        //-- Chart Series
        for(int i = 0; i < cellStateNames.size(); i++) {
	        Series<Number, Number> seriestemp=new AreaChart.Series<Number,Number>();
	        seriestemp.setName(cellStateNames.get(i));
	        sc.getData().add(seriestemp);
	        sc.setMaxSize(maxWidth, maxHeight);
	        sc.setLayoutX(startX);
	        sc.setLayoutY(startY);
	        series.add(seriestemp);
        }
	}
	
	/** getAreaChart() purpose: to return just the graph
	 *  Assumptions: An AreaChart object has already been constructed.
	 * 
	 * @return -- the AreaChart 
	 */
	public AreaChart<Number,Number> getAreaChart() {
		return sc;
	}
	
	/** addDatatoSeries(Queue<Number> dataQ) purpose: to add data to the area chart from the dataqueue at each step
	 * Assumptions: That dataQ isn't null, although the method checks for that, since then it wouldn't plot anything
	 * @param dataQ--a queue that contains the number of cells of each state at each step() iteration
	 */
	
	public void addDataToSeries(Queue<Number> dataQ){
		int it = 0;
        while(!dataQ.isEmpty() && it<series.size())   {
            series.get(it).getData().add(new AreaChart.Data<Number, Number>(xSeriesData,dataQ.remove()));

            //-- Get rid of a bunch from the chart
            if (series.get(it).getData().size() > 1000) {
                series.get(it).getData().remove(0,999);
            }
            it++;
        }
        xSeriesData++;
    }

}
