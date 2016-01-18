package kr.edcan.exchat.data;

/**
 * Created by Junseok on 2016. 1. 18..
 */
public class ClipBoardData {
    private String unit;
    private float value;
    private float convertValue;

    public ClipBoardData(String unit, float value, float convertValue) {
        this.unit = unit;
        this.value = value;
        this.convertValue = convertValue;
    }

    public float getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setConvertValue(float convertValue) {
        this.convertValue = convertValue;
    }

    public float getConvertValue() {
        return convertValue;
    }
}
