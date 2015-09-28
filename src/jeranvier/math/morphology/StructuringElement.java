package jeranvier.math.morphology;

import jeranvier.math.timeseries.Timeseries;

public class StructuringElement extends Timeseries {

	private static final long serialVersionUID = 1L;
	private int centerIndex;

	public StructuringElement(Timeseries data, int centerIndex) {
		super(data);
		this.centerIndex = centerIndex;
	}
	
	public int getCenterIndex(){
		return this.centerIndex;
	}
	
	

}
