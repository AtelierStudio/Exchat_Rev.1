package kr.edcan.exchat.utils;

/**
 * Created by Junseok on 2016. 1. 15..
 */
public class ExchatClipboardUtils {

    final static String[] foreignmoneyUnits = new String[]{
            "원", "달러", "유로", "엔", "위안", "홍콩 달러", "신타이완달러", "파운드", "오만리얄", "캐나다 달러",
            "스위스프랑", "스웨덴크로나", "오스트레일리아달러", "뉴질랜드달러", "코루나", "페소", "리라", "투그릭",
            "크로네", "리얄", "쿠웨이트디나르", "디나르", "디르함", "요르단디나르", "이집트파운드", "밧", "싱가포르달러",
            "링깃", "루피아", "카타르리얄", "텡게", "브루나이달러", "인도루피", "파키스탄루피", "타카", "필리핀페소",
            "멕시코페소", "레알", "동", "랜드", "루블", "포린트", "즈워티"
    };
    public int getCountIndexFromString(String s) {
        return 0;
    }

    public String getNumFromString(String s) {
        if (s == null || s.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
