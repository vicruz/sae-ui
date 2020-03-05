package com.sae.gandhi.spring.ui.dashboard.charts;

public class BarStudents {//extends AbstractChartView {

	/**
	 * 
	 */
	//private static final long serialVersionUID = -439420723261562199L;
/*
	@Override
	public Component getChart() {
		BarChartConfig barConfig = new BarChartConfig();
		barConfig.
        data()
            .labels("January", "February", "March", "April", "May", "June", "July")
            .addDataset(
                    new BarDataset().backgroundColor("rgba(220,220,220,0.5)").label("Dataset 1").yAxisID("y-axis-1"))
            .addDataset(
                    new BarDataset().backgroundColor("rgba(151,187,205,0.5)").label("Dataset 2").yAxisID("y-axis-2").hidden(true))
            .addDataset(
                    new BarDataset().backgroundColor(
                        ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7),
                        ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7),
                        ColorUtils.randomColor(0.7)).label("Dataset 3").yAxisID("y-axis-1"))
            .and();
		
	    barConfig.
	        options()
	            .responsive(true)
	            .hover()
	                .mode(InteractionMode.INDEX)
	                .intersect(true)
	                .animationDuration(400)
	                .and()
	            .title()
	                .display(true)
	                .text("Chart.js Bar Chart - Multi Axis")
	                .and()
	            .scales()
	                .add(Axis.Y, new LinearScale().display(true).position(Position.LEFT).id("y-axis-1"))
	                .add(Axis.Y, new LinearScale().display(true).position(Position.RIGHT).id("y-axis-2").gridLines().drawOnChartArea(false).and())
	                .and()
	           .done();
	    
	    List<String> labels = barConfig.data().getLabels();
        for (Dataset<?, ?> ds : barConfig.data().getDatasets()) {
            BarDataset lds = (BarDataset) ds;
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100));
            }
            lds.dataAsList(data);
        }
        
        ChartJs chart = new ChartJs(barConfig);
        chart.setJsLoggingEnabled(true);
        
        //Div div = new Div();
        //div.add(chart);
        return null;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
*/
}
