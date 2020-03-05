package com.sae.gandhi.spring.ui.dashboard.charts;

import com.vaadin.flow.component.Component;
import com.vaadin.navigator.View;

public interface ChartView extends View{

	public Component getChart();
	public String getSource();
}
