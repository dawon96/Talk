package com.example.talk.fragment;

/**
 * Created by JiSeong Nam on 2017-11-28.
 */

public class adapter {
    public String imageUrl;
    public String ad_title;
    public String ad_money;
    public String ad_content;
    public String ad_category;
    public String ad_useruid;

    public adapter(){}

    public adapter(String a, String b, String c, String d, String e, String f){
        a = this.imageUrl;
        b = this.ad_title;
        c = this.ad_money;
        d = this.ad_content;
        e = this.ad_category;
        f = this.ad_useruid;
    }
}