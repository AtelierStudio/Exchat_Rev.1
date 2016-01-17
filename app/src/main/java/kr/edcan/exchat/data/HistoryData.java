package kr.edcan.exchat.data;

/**
 * Created by Junseok on 2016. 1. 11..
 */
public class HistoryData {
    private String prevUnit, convertUnit;
    private String prevValue, convertValue;

    public HistoryData(String prevValue, String prevUnit, String convertValue, String convertUnit){
        this.prevUnit = prevUnit;
        this.prevValue = prevValue;
        this.convertUnit = convertUnit;
        this.convertValue = convertValue;
    }

    public String getConvertUnit() {
        return convertUnit;
    }

    public String getPrevUnit() {
        return prevUnit;
    }

    public String getPrevValue() {
        return prevValue;
    }

    public String getConvertValue() {
        return convertValue;
    }
}
