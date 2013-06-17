package me.pjq.dragdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private View selected_item = null;
	private int offset_x = 0;
	private int offset_y = 0;

	private ImageView img;

	private ArrayList<Position> initPosition;
	private ArrayList<ImageView> imageViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initPosition();
		initImageView();

		ViewGroup vg = (ViewGroup) findViewById(R.id.vg);
		vg.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_MOVE:
					int x = (int) event.getX() - offset_x;
					int y = (int) event.getY() - offset_y;
					log("x=" + x + ",y=" + y);
					if (!inImageRect(event, selected_item)) {
						return true;
					}

					int w = getWindowManager().getDefaultDisplay().getWidth() - 100;
					int h = getWindowManager().getDefaultDisplay().getHeight() - 100;
					if (x > w)
						x = w;
					if (y > h)
						y = h;
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
							new ViewGroup.MarginLayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));
					lp.setMargins(x, y, 0, 0);

					selected_item.setLayoutParams(lp);
					break;

				case MotionEvent.ACTION_UP:
					log("ACTION_UP");
					Animation animation = createAnimation(selected_item);
					selected_item.startAnimation(animation);

					break;

				default:
					break;
				}
				return true;
			}
		});

		img = (ImageView) findViewById(R.id.img);
		img.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					offset_x = (int) event.getX();
					offset_y = (int) event.getY();
					log("offset_x=" + offset_x + ",offset_y=" + offset_y);
					selected_item = v;
					break;

				case MotionEvent.ACTION_UP:
					log("ACTION_UP");
					// Animation animation = createAnimation();
					// img.startAnimation(animation);

					break;

				default:
					break;
				}

				return false;
			}
		});
	}

	class Position {
		int x = 0;
		int y = 0;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	int YSTEP = 100;

	private void initPosition() {
		initPosition = new ArrayList<Position>();
		int i = 1;
		Position position = new Position(10, YSTEP * (i++));
		initPosition.add(position);
		position = new Position(10, 10 + YSTEP * (i++));
		initPosition.add(position);
		position = new Position(10, 10 + YSTEP * (i++));
		initPosition.add(position);
		position = new Position(10, 10 + YSTEP * (i++));
		initPosition.add(position);
	}

	private void initImageView() {
		imageViews = new ArrayList<ImageView>();
		ViewGroup vg = (ViewGroup) findViewById(R.id.vg);

		for (Position item : initPosition) {
			int x = item.x;
			int y = item.y;

			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(R.drawable.ic_launcher);

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					new ViewGroup.MarginLayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
			lp.setMargins(x, y, 0, 0);

			imageView.setLayoutParams(lp);
			imageView.setTag(item);
			vg.addView(imageView);

			imageView.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getActionMasked()) {
					case MotionEvent.ACTION_DOWN:
						offset_x = (int) event.getX();
						offset_y = (int) event.getY();
						log("offset_x=" + offset_x + ",offset_y=" + offset_y);
						selected_item = v;
						break;

					case MotionEvent.ACTION_UP:
						log("ACTION_UP");
						// Animation animation = createAnimation();
						// img.startAnimation(animation);

						break;

					default:
						break;
					}

					return false;
				}
			});
		}

	}

	private Animation createAnimation(final View view) {
		Position position = (Position) view.getTag();
		if (null == position) {
			position = new Position(0, 0);
			return null;
		}
		Rect rect = getImageRect(view);

		TranslateAnimation mShowAnimation = new TranslateAnimation(
				Animation.ABSOLUTE, position.x, Animation.ABSOLUTE, -rect.left,
				Animation.ABSOLUTE, position.y, Animation.ABSOLUTE, -rect.top);
		mShowAnimation.setDuration(500);
		mShowAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						new ViewGroup.MarginLayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT));
				Position position = (Position) view.getTag();
				if (null == position) {
					position = new Position(0, 0);
				}

				lp.setMargins(position.x, position.y, 0, 0);

				selected_item.setLayoutParams(lp);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}
		});

		return mShowAnimation;
	}

	private Rect getImageRect(View img) {
		Rect rect = new Rect();
		int x_begin = img.getLeft();
		int x_end = img.getRight();
		int y_begin = img.getTop();
		int y_end = img.getBottom();

		rect.left = x_begin;
		rect.right = x_end;
		rect.top = y_begin;
		rect.bottom = y_end;
		log("left=" + rect.left + ",right=" + rect.right + ",top=" + rect.top
				+ ",bottom=" + rect.bottom);

		return rect;
	}

	private boolean inImageRect(MotionEvent event, View img) {
		Rect rect = new Rect();
		img.getDrawingRect(rect);

		int x_begin = img.getLeft();
		int x_end = img.getRight();
		int y_begin = img.getTop();
		int y_end = img.getBottom();

		rect.left = x_begin;
		rect.right = x_end;
		rect.top = y_begin;
		rect.bottom = y_end;

		int x = (int) event.getX();
		int y = (int) event.getY();
		boolean in = rect.contains(x, y);

		return in;
	}

	private void log(String msg) {
		Log.i(TAG, msg);

	}
}