package kr.edcan.exchat.data;

/**
 * Created by Junseok on 2016. 1. 10..
 */
public class DeveloperData {
    private String title, content;
    public DeveloperData(String title, String content){
        this.title = title;
        this.content=  content;
    }
    public String getTitle(){
        return this.title;
    }

    public String getContent() {
        return content;
    }
}
