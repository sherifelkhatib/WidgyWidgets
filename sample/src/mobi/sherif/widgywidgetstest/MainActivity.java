package mobi.sherif.widgywidgetstest;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onEditTextWithCustomError(View v) {
		startActivity(new Intent(this, EditTextWithCustomErrorActivity.class));
	}

	public void onKnobView(View v) {
		startActivity(new Intent(this, KnobViewActivity.class));
	}
}
