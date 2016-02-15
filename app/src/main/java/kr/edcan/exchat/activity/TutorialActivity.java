package kr.edcan.exchat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import kr.edcan.exchat.R;


public class TutorialActivity extends Activity {
    private ViewPager mPager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mPager = (ViewPager) findViewById(R.id.ViewPager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
        sharedPreferences = getSharedPreferences("Exchat", 0);
        editor = sharedPreferences.edit();
    }

    private class PagerAdapterClass extends PagerAdapter {
        private LayoutInflater mInflater;

        public PagerAdapterClass(Context c) {
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(final View pager, int position) {
            View v = null;
            // TODO: Set each page's view
            if (position == 0) {
                v = mInflater.inflate(R.layout.tuto_1, null);
                setNextPage(v, pager);
            } else if (position == 1) {
                v = mInflater.inflate(R.layout.tuto_2, null);
                setNextPage(v, pager);
            } else if (position == 2) {
                v = mInflater.inflate(R.layout.tuto_3, null);
                setNextPage(v, pager);
            } else if (position == 3) {
                v = mInflater.inflate(R.layout.tuto_4, null);
                setNextPage(v, pager);
                setSpinner();
            }

            ((ViewPager) pager).addView(v, 0);
            return v;
        }

        private void setSpinner() {

        }

        private void setNextPage(View v, final View pager) {
            TextView toNext = (TextView) v.findViewById(R.id.tutorial_page_next);
            toNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewPager)pager).setCurrentItem(((ViewPager)pager).getCurrentItem()+1, true);
                }
            });
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }
}
