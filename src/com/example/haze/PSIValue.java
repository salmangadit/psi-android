package com.example.haze;

public class PSIValue {
	public String time;
	public Integer value;

	public double timeAsDouble() {
		String[] words = time.split(" ");
		return words[1]=="AM"?Double.valueOf(words[0])*100: (Double.valueOf(words[0])+12)*100;
	}

	@Override
	public String toString() {
		return time + ":" + String.valueOf(value);

	}
}
