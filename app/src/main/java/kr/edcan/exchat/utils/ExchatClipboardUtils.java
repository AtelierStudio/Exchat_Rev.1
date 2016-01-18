package kr.edcan.exchat.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import kr.edcan.exchat.data.ClipBoardData;

/**
 * Created by Junseok on 2016. 1. 15..
 */
public class ExchatClipboardUtils {

    private ExchatUtils utils;
    final static String[] foreignmoneyUnits = new String[]{
            "원", "달러", "유로", "엔", "위안", "홍콩 달러", "신타이완달러", "파운드", "오만리얄", "캐나다 달러",
            "스위스프랑", "스웨덴크로나", "오스트레일리아달러", "뉴질랜드달러", "코루나", "페소", "리라", "투그릭",
            "크로네", "리얄", "쿠웨이트디나르", "디나르", "디르함", "요르단디나르", "이집트파운드", "밧", "싱가포르달러",
            "링깃", "루피아", "카타르리얄", "텡게", "브루나이달러", "인도루피", "파키스탄루피", "타카", "필리핀페소",
            "멕시코페소", "레알", "동", "랜드", "루블", "포린트", "즈워티"
    };

    public ExchatClipboardUtils(Context c) {
        utils = new ExchatUtils(c);
    }

    public ArrayList<ClipBoardData> getResult(String s) {

        ArrayList<ClipBoardData> result = new ArrayList<>();
        StringBuilder sb_1, sb_2;
        // Count Units, if string contains over 2 units it will return null
        int indexcnt = 0;
        ArrayList<String> valueArr = new ArrayList<>();
        ArrayList<Integer> indexArr = new ArrayList<>();
        for (int i = 0; i < foreignmoneyUnits.length; i++) {
            if (s.contains(foreignmoneyUnits[i])) {
                indexcnt++;
                valueArr.add(foreignmoneyUnits[i]);
                indexArr.add(i);
                if (indexcnt > 2) return null;
            }
        }
        if (indexcnt == 0) return null;
        int firstIndex, secondIndex;
        firstIndex = s.indexOf(valueArr.get(0));
        String first = s.substring(0, firstIndex).replaceAll(",", "");
        sb_1 = new StringBuilder();
        char[] c = first.toCharArray();
        for (int i = first.length() - 1; i >= 0; i--) {
            if(Character.isWhitespace(c[i])) {
                break;
            }
            if (Character.isDigit(c[i]) || (c[i] + "").equals(".")) sb_1.append(c[i]);
        }
        sb_1.reverse();
        result.add(new ClipBoardData(valueArr.get(0), Float.parseFloat(sb_1.toString()), utils.convertToKRW(indexArr.get(0), Float.parseFloat(sb_1.toString()))));
        if (indexcnt == 2) {
            secondIndex = s.indexOf(valueArr.get(1));
            String second = s.substring(firstIndex + 1, secondIndex).replaceAll(",", "");
            sb_2 = new StringBuilder();
            char[] c2 = second.toCharArray();
            for (int i = second.length() - 1; i >= 0; i--) {
                if(Character.isWhitespace(c2[i])) break;
                if (Character.isDigit(c2[i]) || (c2[i] + "").equals(".")) sb_2.append(c2[i]);
            }
            sb_2.reverse();
            result.add(new ClipBoardData(valueArr.get(1), Float.parseFloat(sb_2.toString()), utils.convertToKRW(indexArr.get(1), Float.parseFloat(sb_2.toString()))));

        }

        return result;
    }
}
