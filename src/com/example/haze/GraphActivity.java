package com.example.haze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.LineGraphView;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;

public class GraphActivity extends Activity {

	private Cache cache = null;
	private static String TAG = "Haze.java";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);

		Globals appState = ((Globals) getApplicationContext());
		cache = appState.getCache();

		new GraphGenerator(cache, this).execute();

		// Create a couple arrays of y-values to plot:
		// List<Number> series1Numbers = new ArrayList<Number>();
		//
		// for (int i = 0; i < cache.last_ten_readings.size(); i++) {
		// series1Numbers.add((Number) cache.last_ten_readings.get(i).value);
		// }
		//
		// drawGraph(series1Numbers);
	}

	public void drawGraph(List<PSIValue> values) {

		GraphViewData[] data = new GraphViewData[values.size()];
		for (int i = values.size() - 1; i >= 0; i++) {
			// Log.v(TAG, String.valueOf(values.get(i).timeAsDouble()));
			data[i] = new GraphViewData(i, values.get(i).value);
		}

		GraphView graphView = new LineGraphView(this // context
				, "PSI Values" // heading
		);

		String[] labels = new String[values.size()];
		for (int i = values.size() - 1; i >= 0; i++) {
			labels[i] = values.get(i).time;
		}

		graphView.setHorizontalLabels(labels);

		// add data
		graphView.addSeries(new GraphViewSeries(data));
		// set view port, start=2, size=40
		graphView.setViewPort(0, values.size());
		graphView.setScrollable(false);
		// optional - activate scaling / zooming
		// graphView.setScalable(false);

		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		layout.addView(graphView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph, menu);
		return true;
	}
}
