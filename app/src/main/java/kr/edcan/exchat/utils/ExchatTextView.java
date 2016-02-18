package kr.edcan.exchat.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import kr.edcan.exchat.R;

/**
 * Created by MalangDesktop on 2016-02-04.
 */
public class ExchatTextView extends TextView {

    public ExchatTextView(Context context) {
        super(context);
        applyFont(context, false);
    }


    private void applyFont(Context context, boolean isBold) {
        if (isBold)
            setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_bold.ttf"));
        else setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_light.ttf"));
    }

    public ExchatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExchatTextView);
        applyFont(context, a.getBoolean(R.styleable.ExchatTextView_isBold, false));
    }

    public ExchatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExchatTextView);
        applyFont(context, a.getBoolean(R.styleable.ExchatTextView_isBold, false));
    }

}
