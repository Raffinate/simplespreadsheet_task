package local.ss;

import java.util.Hashtable;

public class IntegerConstantValue implements IntegerValue {
	
	public IntegerConstantValue(final int value) {
		this.value = value;
	}

	public Integer getValue(Hashtable<CellAddress, Object> vars) {
		return value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}

	private final Integer value;
}
