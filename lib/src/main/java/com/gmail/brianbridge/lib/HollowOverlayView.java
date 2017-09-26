package com.gmail.brianbridge.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class HollowOverlayView extends View {
	public static final String TAG = HollowOverlayView.class.getSimpleName();

	private HollowOverlayViewConfig mConfig;
	private Canvas mOverlayCanvas;
	private Bitmap mOverlayBitmap;

	public HollowOverlayView(Context context) {
		super(context);
	}

	public HollowOverlayView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public HollowOverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mOverlayBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mOverlayCanvas = new Canvas(mOverlayBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mConfig.onDraw(mOverlayCanvas);
		canvas.drawBitmap(mOverlayBitmap, 0, 0, null);
	}

	public void setConfig(HollowOverlayViewConfig config) {
		mConfig = config;
	}

	@SuppressWarnings({"unchecked", "WeakerAccess"})
	public static abstract class HollowOverlayViewConfig<T> {
		protected Paint mHollowPaint = new Paint();
		protected Paint mOverlayPaint = new Paint();
		protected Paint mBorderPaint = new Paint();

		private HollowOverlayViewConfig() {
			mOverlayPaint.setStyle(Paint.Style.FILL);
			mHollowPaint.setColor(Color.TRANSPARENT);
			mHollowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			mBorderPaint.setStyle(Paint.Style.STROKE);
		}

		public T setOverlayColor(int overlayColor) {
			mOverlayPaint.setColor(overlayColor);
			return (T) this;
		}

		public T setBorderColor(int borderColor) {
			mBorderPaint.setColor(borderColor);
			return (T) this;
		}

		public T setBorderWidth(int borderWidth) {
			mBorderPaint.setStrokeWidth(borderWidth);
			return (T) this;
		}

		public abstract void onDraw(Canvas canvas);
	}

	public static class RoundedRectangleConfig extends HollowOverlayViewConfig<RoundedRectangleConfig> {
		private RectF mRect;
		private float mCornerRadius;

		public RoundedRectangleConfig setRect(RectF rect) {
			mRect = rect;
			return this;
		}

		public RoundedRectangleConfig setRect(float left, float top, float right, float bottom) {
			mRect = new RectF(left, top, right, bottom);
			return this;
		}

		public RoundedRectangleConfig setCornerRadius(float cornerRadius) {
			mCornerRadius = cornerRadius / 2;
			return this;
		}

		@Override
		public void onDraw(Canvas canvas) {
			canvas.drawPaint(mOverlayPaint);
			canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mHollowPaint);

			if (mBorderPaint.getStrokeWidth() > 0) {
				canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mBorderPaint);
			}
		}
	}
}