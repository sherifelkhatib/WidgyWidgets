package mobi.sherif.widgywidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class KnobView extends View {
	private Drawable mKnobDrawable;

	public KnobView(Context context) {
		super(context);
		init(context, null);
	}

	public KnobView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public KnobView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KnobView);
		int drawable = a.getResourceId(R.styleable.KnobView_knob, 0);
		if(drawable != 0) {
			mKnobDrawable = a.getDrawable(R.styleable.KnobView_knob);
		}
		else {
			mKnobDrawable = new ShapeDrawable(new OvalShape());
			ShapeDrawable s = (ShapeDrawable) mKnobDrawable;
	        s.getPaint().setColor(Color.BLACK);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mKnobDrawable.setBounds(w/2-w/4, h/2-h/4, w/2+w/4, h/2+h/4);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mKnobDrawable.draw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = MotionEventCompat.getActionMasked(event);
		if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
			int w = getWidth();
			int h = getHeight();
			int x = (int) event.getX();
			int y = (int) event.getY();
			mKnobDrawable.setBounds(x-w/4, y-h/4, x+w/4, y+h/4);
			invalidate();
		}
		else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			int w = getWidth();
			int h = getHeight();
			int x = w/2;
			int y = h/2;
			mKnobDrawable.setBounds(x-w/4, y-h/4, x+w/4, y+h/4);
			invalidate();
        }
		return true;
	}
}