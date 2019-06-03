package com.sae.gandhi.spring.ui.dashboard;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;

@Tag("chart-js")
@HtmlImport("html/chartjs.html")
@JavaScript("bower_components/moment/min/moment.min.js")
@JavaScript("bower_components/chart.js/dist/Chart.min.js")
public class ChartJs extends PolymerTemplate<ChartJsModel> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Registration addClickListener(
            ComponentEventListener<ClickEvent> listener) {
        return addListener(ClickEvent.class, listener);
    }

    public ChartJs(String chart) {
        getModel().setChartJs(chart);
    }

    public void updateChart(String chart){
        getModel().setChartJs(chart);
    }

    public void updateData(String data){
        getModel().setChartData(data);
    }

    public void updateOptions(String options){
        getModel().setChartOptions(options);
    }

    /**
     * @param label - Dataset label
     * @param value - Dataset value
     */
    @ClientCallable
    private void handleClick(String label, String value) {
        fireEvent(new ClickEvent(this, false, label, value));
    }
}
