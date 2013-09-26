package mobi.sherif.widgywidgetstest;

import java.util.ArrayList;
import java.util.Random;

import mobi.sherif.widgywidgets.DraggableGridView;
import mobi.sherif.widgywidgets.DraggableGridView.OnItemDragListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DraggableGridViewActivity extends Activity implements OnItemDragListener {

	DraggableGridView mGrid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draggablegridview);
		mGrid = (DraggableGridView) findViewById(R.id.grid);
		ArrayList<Item> items = new ArrayList<Item>();
		for(int i=0;i<1000;i++) {
			items.add(new Item(i));
		}
		mGrid.setAdapter(new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_2, android.R.id.text1, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				((TextView)v.findViewById(android.R.id.text2)).setText(getItem(position).text2);
				return v;
			}
		});
		mGrid.setOnItemDragListener(this, true, false);
	}
	private static class Item {
		private static final Random seeder = new Random(System.currentTimeMillis());
		public Item(int index) {
			text1 = "ITEM " + index;
			int l = seeder.nextInt(10);
			StringBuilder sb = new StringBuilder(l);
			for(int i=0;i<l;i++) {
				sb.append((char)seeder.nextInt());
			}
			text2 = sb.toString();
		}
		String text1;
		String text2;
		@Override
		public String toString() {
			return text1;
		}
	}
	private int mDraggedPosition = -1;
	private View mLastView;
	@Override
	public void onItemDragStart(View draggedView, int position) {
		log("Item " + position + " is being dragged");
		mDraggedPosition = position;
	}
	@Override
	public void onItemDragMove(View draggedView, View currentView, int position) {
		log("Item " + mDraggedPosition + " moving above Item " + position);
		if(currentView != mLastView) {
			if(mLastView != null)
				mLastView.setBackgroundColor(Color.argb(0, 0, 0, 0));
			mLastView = currentView;
			if(mLastView != null)
				mLastView.setBackgroundColor(Color.argb(190, 70, 70, 70));
		}
	}
	@Override
	public void onItemReleased(View draggedView, int startPosition, View endView, int endPosition) {
		log("Item " + startPosition + " released above Item " + endPosition);
		if(mLastView != null)
			mLastView.setBackgroundColor(Color.argb(0, 0, 0, 0));
	}
	private void log(String log) {
		Log.v("sherif", log);
	}
}
