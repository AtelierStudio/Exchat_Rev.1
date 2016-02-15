package kr.edcan.exchat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import kr.edcan.exchat.data.FinanceCalcData;
import kr.edcan.exchat.data.HistoryData;

/**
 * Created by Junseok on 2016. 1. 10..
 */
public class ExchatUtils {

    long lastUpdateTime;
    ArrayList<String> titles;
    ArrayList<String> values;
    Context context;
<<<<<<< HEAD
=======
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Realm realm;

>>>>>>> 4e76ad575e9c59140a7574f938f4d92181f0786a
    public ExchatUtils(Context context) {
        this.context = context;
        realm = Realm.getInstance(context);
        sharedPreferences = context.getSharedPreferences("Exchat", 0);
        editor = sharedPreferences.edit();
    }

    // Getter
    public ArrayList<String> getFinanceFromDB(boolean isTitle) {
        ArrayList<String> arrayList = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<FinanceCalcData> results = realm.where(FinanceCalcData.class).findAll();
        for(FinanceCalcData result : results){
            if(isTitle) arrayList.add(result.getContentTitle());
            else arrayList.add(result.getContentValue());
        }
        realm.commitTransaction();
        return arrayList;
    }

    // Parse, Update
    public void setFinanceStatus() {
        Calendar c = Calendar.getInstance();
        lastUpdateTime = sharedPreferences.getLong("lastUpdateTime", -1);
        if (lastUpdateTime == -1 || c.get(Calendar.HOUR_OF_DAY) >= 18 ||
                System.currentTimeMillis() - lastUpdateTime >= 86400000) {
            updateFinance();
        }
    }

    private void updateFinance() {
        titles = getCurrencyList("title");
        values = getCurrencyList("values");
        realm.beginTransaction();
        RealmResults<FinanceCalcData> results = realm.where(FinanceCalcData.class).findAll();
        results.clear();
        for (int i = 0; i < titles.size(); i++) {
            FinanceCalcData data = realm.createObject(FinanceCalcData.class);
            data.setContentTitle(titles.get(i));
            data.setContentValue(values.get(i));
        }
        editor.putLong("lastUpdateTime", System.currentTimeMillis());
        editor.commit();
        realm.commitTransaction();
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
            if (CSSQuery.equals("td.sale"))
                arrayList.set(3, Float.parseFloat(arrayList.get(3).replace(",", "")) / 100 + "");
            return arrayList;
        }
    }


    //Calculate
    public float calculateValues(float target, int prevPosition, int convertPosition) {
        if (prevPosition == convertPosition) return target;
        else {
            if (prevPosition == 0) return convertFromKRW(convertPosition, target);
            else if (convertPosition == 0) return convertToKRW(prevPosition, target);
            else return convertFromKRW(convertPosition, convertToKRW(prevPosition, target));
        }
    }

    public float convertFromKRW(int position, float target) {
        return target / Float.parseFloat(getFinanceFromDB(false).get(position).replace(",", ""));
    }

    public float convertToKRW(int position, float target) {
        return target * Float.parseFloat(getFinanceFromDB(false).get(position).replace(",", ""));
    }

}
