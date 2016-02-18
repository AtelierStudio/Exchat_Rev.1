package kr.edcan.exchat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.edcan.exchat.R;
import kr.edcan.exchat.utils.ExchatUtils;


public class TutorialActivity extends AppCompatActivity {
    private ViewPager mPager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> titleArr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        titleArr = new ArrayList<>();
        ExchatUtils utils = new ExchatUtils(getApplicationContext());
        utils.setFinanceStatus();
        titleArr = utils.getFinanceFromDB(true);
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
                setSpinner(v);
            }

            ((ViewPager) pager).addView(v, 0);
            return v;
        }

        private void setSpinner(View v) {
            //Log.e("asdf", titleArr.size()+"");
            final Spinner spinner = (Spinner) v.findViewById(R.id.tuto_4_spinner);
            if (titleArr.size() != 0) {
                SpinnerAdapter units = new ArrayAdapter<String>(TutorialActivity.this, R.layout.spinner_tutorial_textstyle, titleArr);
                spinner.setAdapter(units);
            }
            spinner.setSelection(1);

            TextView start = (TextView) v.findViewById(R.id.tutorial_page_next);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (spinner.getSelectedItemPosition() != 0) {
                        SharedPreferences sharedPreferences = getSharedPreferences("Exchat", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("mainUnit", spinner.getSelectedItemPosition());
                        editor.putBoolean("isFirst", false);
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else
                        Snackbar.make(v, "주요 환율로 KRW는 설정하실 수 없습니다", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        private void setNextPage(View v, final View pager) {
            TextView toNext = (TextView) v.findViewById(R.id.tutorial_page_next);
            toNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ViewPager) pager).setCurrentItem(((ViewPager) pager).getCurrentItem() + 1, true);
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
