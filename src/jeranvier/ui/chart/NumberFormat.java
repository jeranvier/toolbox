package jeranvier.ui.chart;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class NumberFormat extends Format{

	private static final long serialVersionUID = 1L;

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		return toAppendTo.append(obj);
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		return Double.parseDouble(source);
	}

}
