package kr.edcan.exchat.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Junseok on 2016. 1. 10..
 */
public class ExchatUtils {

    ArrayList<String> titles, values;
    Context context;
    public ExchatUtils(Context context) {
        this.context = context;

        titles = new ArrayList<>();
        values = new ArrayList<>();
        titles = getCurrencyList("title");
        values = getCurrencyList("values");

    }

    private ArrayList<String> getCurrencyList(String type) {
        try {
            return new getCurrency(type).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getCurrency extends AsyncTask<String, String, ArrayList<String>> {
        ArrayList<String> arrayList;
        String CSSQuery = "";

        public getCurrency(String s) {
            arrayList = new ArrayList<>();
            if (s.equals("title")) {
                CSSQuery = "td.tit";
                arrayList.add("한국 KRW");
            } else {
                arrayList.add("1");
                CSSQuery = "td.sale";
            }
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect("http://info.finance.naver.com/marketindex/exchangeList.nhn").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements data = doc.select(CSSQuery);
            for (Element datas : data) {
                arrayList.add(datas.text());
            }
            if(CSSQuery.equals("td.sale")) arrayList.set(3, Float.parseFloat(arrayList.get(3).replace(",",""))/100+"");
            return arrayList;
        }
    }

    public float calculateValues(float target, int prevPosition, int convertPosition) {
        if (prevPosition == convertPosition) return target;
        else {
            if (prevPosition == 0) return convertFromKRW(convertPosition, target);
            else if (convertPosition == 0) return convertToKRW(prevPosition, target);
            else return convertFromKRW(convertPosition, convertToKRW(prevPosition, target));
        }
    }

    public float convertFromKRW(int position, float target) {
        return target / Float.parseFloat(values.get(position).replace(",", ""));
    }

    public float convertToKRW(int position, float target) {
        return target * Float.parseFloat(values.get(position).replace(",", ""));
    }


    // Getter
    public ArrayList<String> getTitles() {
        return titles;
    }

    public ArrayList<String> getValues() {
        return values;
    }
}
