package kr.edcan.exchat.data;

import io.realm.RealmObject;

/**
 * Created by MalangDesktop on 2016-02-05.
 */
public class FinanceCalcData extends RealmObject {
    private String contentTitle;
    private String contentValue;

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }
}
