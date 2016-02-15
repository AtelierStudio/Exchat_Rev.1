package kr.edcan.exchat.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MalangDesktop on 2016-02-04.
 */
public class ExchatTextView extends TextView {
    public ExchatTextView(Context context) {
        super(context);
        applyFont(context);
    }

    private void applyFont(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_light.ttf"));
    }

    public ExchatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyFont(context);

    }

    public ExchatTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyFont(context);

    }

}
