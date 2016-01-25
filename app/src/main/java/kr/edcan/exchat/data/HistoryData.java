package kr.edcan.exchat.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Junseok on 2016. 1. 11..
 */
public class HistoryData extends RealmObject{

    private int prevUnit, convertUnit;
    private float prevValue, convertValue;
    private Date date;

    public int getConvertUnit() {
        return convertUnit;
    }

    public int getPrevUnit() {
        return prevUnit;
    }

    public float getConvertValue() {
        return convertValue;
    }

    public float getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(float prevValue) {
        this.prevValue = prevValue;
    }

    public void setPrevUnit(int prevUnit) {
        this.prevUnit = prevUnit;
    }

    public void setConvertValue(float convertValue) {
        this.convertValue = convertValue;
    }

    public void setConvertUnit(int convertUnit) {
        this.convertUnit = convertUnit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
