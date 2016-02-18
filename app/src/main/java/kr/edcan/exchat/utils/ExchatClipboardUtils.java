package kr.edcan.exchat.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.edcan.exchat.data.ClipBoardData;

/**
 * Created by Junseok on 2016. 1. 15..
 */
public class ExchatClipboardUtils {

    private ExchatUtils utils;
    public final static String[] foreignmoneyUnits = new String[]{
            "KRW", "USD", "EUR", "JPY", "CNY", "HKD", "TWD", "GBP", "OMR", "CAD", "CHF", "SEK", "AUD", "NZD", "CZK", "CLP",
            "TRY", "MNT", "ILS", "DKK", "NOK", "SAR", "KWD", "BHD", "AED", "JOD", "EGP", "THB", "SGD", "MYR", "IDR", "QAR",
            "KZT", "BND", "INR", "PKR", "BDT", "PHP", "MXN", "BRL", "VND", "ZAR", "RUB", "HUF", "PLN"
    };
    public final static String[] foreignmoneyUnitsText = new String[]{
            "원", "달러", "유로", "엔", "위안", "홍콩달러", "신타이완달러", " 파운드", "오만리얄", "캐나다달러",
            "스위스프랑", "스웨덴크로나", "오스트레일리아달러", "뉴질랜드달러", "코루나", "페소", "리라", "투그릭", "셰켈",
            "크로네", "크로네", "리얄", "쿠웨이트디나르", "디나르", "디르함", "요르단디나르", "이집트파운드", "밧", "싱가포르달러",
            "링깃", "루피아", "카타르리얄", "텡게", "브루나이달러", "인도루피", "파키스탄루피", "타카", "필리핀페소",
            "멕시코페소", "레알", "동", "랜드", "루블", "포린트", "즈워티"
    };
    public final static String[] foreignmoneyUnitsSign = new String[]{
            "₩", "$", "€", "Ұ", "Y", "HK$", "NT$", "£", "ر.ع.", "C$", "CHF", "kr", "AUD", "NZD", "Kč",
            "CLP", "TRY", "₮", "\t₪", "kr", "Kr", "﷼", "د.ك", ".د.ب", "د.إ", "دينار", "ج.م", "฿", "S$",
            "RM", "Rp", "QR", "₸", "B$", "Rs", "Rs", "৳", "₱", "M$", "R$", "₫", "R", "\u20BD\n", "Ft", "zł"
    };

    public ExchatClipboardUtils(Context c) {
        utils = new ExchatUtils(c);
    }

    public ClipBoardData getResult(String origin) {

        String s = origin.replace(",", "").replace(" ", "");
        // Count Units, if string contains over 2 units it will return null
        int indexcnt = 0;
        int type = -1; // Default is -1
        String regularPattern = "";
        String unitValue = "";
        int index = -1;

        for (int i = 0; i < foreignmoneyUnitsText.length; i++) {
            if (s.contains(foreignmoneyUnitsText[i])) {
                type = 0;
                indexcnt++;
                unitValue = foreignmoneyUnitsText[i];
                index = i;
            }
        }
        if (indexcnt == 0) {
            for (int i = 0; i < foreignmoneyUnitsSign.length; i++) {
                if (s.contains(foreignmoneyUnitsSign[i])) {
                    type = 1;
                    indexcnt++;
                    unitValue = foreignmoneyUnitsSign[i];
                    index = i;
                }
            }
        }
        if (type == -1) return null;
        else {
            if (type == 0)
                regularPattern = "[0-9]+(\\.[0-9]*)?" + unitValue;
            else regularPattern = unitValue.replace("$", "\\$") + "[0-9]+(\\.[0-9]*)?";
            Pattern pattern = Pattern.compile(regularPattern);
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                float temp = Float.parseFloat(matcher.group(0).replace(unitValue, ""));
                return new ClipBoardData(index, temp, utils.convertToKRW(index, temp));
            }
        }
        return null;
    }
}
