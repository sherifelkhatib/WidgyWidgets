package mobi.sherif.widgywidgetstest;

import mobi.sherif.widgywidgets.EditTextWithCustomError;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;

public class EditTextWithCustomErrorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editeextwithcustomerroractivity);
	}
	
	public void onShowError(View v) {
		((EditTextWithCustomError)findViewById(R.id.input)).setError(((EditText)findViewById(R.id.inputerror)).getText());
	}
}
