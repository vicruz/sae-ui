package com.sae.gandhi.spring.ui.dashboard;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface ChartJsModel extends TemplateModel {
    void setChartJs(String jsonChart);
    void setChartData(String jsonChartData);
    void setChartOptions(String jsonChartOptions);
}
