package mobi.sherif.widgywidgetstest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;

public class KnobViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knobview);
		findViewById(R.id.knob1).getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				View v = findViewById(R.id.knob1);
				v.getViewTreeObserver().removeOnPreDrawListener(this);
				int width = v.getWidth();
				int height = v.getHeight();
				v.getLayoutParams().width = Math.min(width, height);
				v.getLayoutParams().height = Math.min(width, height);
				v.requestLayout();
				return false;
			}
		});
	}
}
