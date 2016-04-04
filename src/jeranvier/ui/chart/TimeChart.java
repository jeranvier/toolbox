package jeranvier.ui.chart;

import java.text.Format;
import java.text.SimpleDateFormat;

import jeranvier.math.timeseries.TimeseriesCollection;

public class TimeChart extends Chart<Long, Double>{

	private static final long serialVersionUID = 1L;

	public TimeChart(TimeseriesCollection tsc) {
			super(tsc.getLabelsData(), tsc.getTimeSeries());
	}

	@Override
	public Format verticalAxisFormater() {
		return new NumberFormat();
	}

	@Override
	public Format horizontalAxisFormater() {
		return new SimpleDateFormat("HH:mm:ss.SSS");
	}
		
}
