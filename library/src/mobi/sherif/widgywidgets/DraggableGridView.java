package mobi.sherif.widgywidgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

/**
 * @author Sherif elKhatib - shush
 * 
 * GridView component that supports Dragging. Still beta
 * v0.1 - September 26/2013
 */
public class DraggableGridView extends GridView implements OnItemLongClickListener, OnTouchListener, OnScrollListener {
	private static final int SCROLL_MARGIN = 40;
	private boolean isDragging = false;
	private View mDraggedView;
	private int mDraggedPosition;
	private Bitmap mDraggedViewBitmap;
	private int mDraggedViewX;
	private int mDraggedViewY;
	private int mDraggedViewCenterX;
	private int mDraggedViewCenterY;
	private int mScrollState = SCROLL_STATE_IDLE;
	
	private View mLastViewUnder;
	
	private boolean mNotifyExhaustive = false;
	private boolean mNotifyMove = false;
	private OnItemDragListener mOnItemDragListener; 
	
	public interface OnItemDragListener {
		public void onItemDragStart(View draggedView, int position);
		public void onItemDragMove(View draggedView, View currentView, int position);
		public void onItemReleased(View draggedView, int startPosition, View endView, int endPosition);
	}

	/**
	 * @param itemDragListener
	 * @param notifyMove boolean that indicates whether you want to be notified of dragged item movements via {@link OnItemDragListener#onItemDragMove(View, int, long)}
	 * @param exhaustive boolean that indicates whether you want all the move notifications or just when the underlying view changes
	 */
	public void setOnItemDragListener(OnItemDragListener itemDragListener, boolean notifyMove, boolean exhaustive) {
		mNotifyExhaustive = exhaustive;
		mNotifyMove = notifyMove;
		mOnItemDragListener = itemDragListener;
	}
	public DraggableGridView(Context context) {
		super(context);
		init(context, null);
	}

	public DraggableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public DraggableGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setOnItemLongClickListener(this);
		setOnTouchListener(this);
		setOnScrollListener(this);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isDragging) {
			canvas.drawBitmap(mDraggedViewBitmap, mDraggedViewX-mDraggedViewCenterX, mDraggedViewY-mDraggedViewCenterY, null);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
		isDragging = true;
		mDraggedView = view;                
		mDraggedPosition = position;
		
		mDraggedViewBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);                
	    view.draw(new Canvas(mDraggedViewBitmap));

	    mDraggedViewCenterX = view.getWidth()/2;
	    mDraggedViewCenterY = view.getHeight()/2;
	    if(mOnItemDragListener != null) {
	    	mOnItemDragListener.onItemDragStart(mDraggedView, mDraggedPosition);
	    }
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!isDragging) {
			return false;
		}
		switch(event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			mDraggedViewX = (int) event.getX();
			mDraggedViewY = (int) event.getY();
			if(mScrollState == SCROLL_STATE_IDLE) {
				if(mDraggedViewY < SCROLL_MARGIN) {
					pleaseScrollUp();
				}
				else if(mDraggedViewY > getHeight() - SCROLL_MARGIN) {
					pleaseScrollDown();
				} 
			}
		    if(mNotifyMove && mOnItemDragListener != null) {
		    	View view = getCurrentView();
		    	if(mLastViewUnder != view || mNotifyExhaustive) {
		    		mLastViewUnder = view;
		    		int position = mLastViewUnder!=null?getPositionForView(mLastViewUnder):INVALID_POSITION;
		    		mOnItemDragListener.onItemDragMove(mDraggedView, view, position);
		    	}
		    }
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mDraggedViewX = (int) event.getX();
			mDraggedViewY = (int) event.getY();
			isDragging = false;
			doneDragging();
			break;
		}
		return true;
	}

	private View getCurrentView() {
		Rect c = new Rect(); 
		for(int i=0;i<getChildCount();i++) {
			getChildAt(i).getHitRect(c);
			if(c.contains(mDraggedViewX, mDraggedViewY)) {
				return getChildAt(i);
			}
		}
		return null;
		
	}
	private void doneDragging() {
		View v = getCurrentView();
		int position = v!=null?getPositionForView(v):INVALID_POSITION;
		if(mOnItemDragListener != null)
			mOnItemDragListener.onItemReleased(mDraggedView, mDraggedPosition, v, position);
		mDraggedViewBitmap.recycle();
		mDraggedViewBitmap = null;
		invalidate();
	}

	private void pleaseScrollUp() {
		smoothScrollToPosition(getFirstVisiblePosition() - ((getFirstVisiblePosition() + getLastVisiblePosition())/2 - getFirstVisiblePosition()));
	}
	private void pleaseScrollDown() {
		smoothScrollToPosition(getLastVisiblePosition() + 1);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mScrollState = scrollState;
	}
}