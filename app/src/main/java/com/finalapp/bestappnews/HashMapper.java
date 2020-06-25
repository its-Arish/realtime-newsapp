package com.finalapp.bestappnews;

import java.util.Map;
public class HashMapper {

    public static String urlToLabel (String value) {

        Map<String, String> vars = new java.util.HashMap<>();
        vars.put(
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_0_label_value),
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_0_label));
        vars.put(
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_5_label_value),
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_5_label));

        vars.put(
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_3_label_value),
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_3_label));

        vars.put(
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_2_label_value),
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_2_label));

        vars.put(
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_1_label_value),
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_1_label));

        vars.put(
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_4_label_value),
                MyApplication.takeappContext().getResources().getString(R.string.pref_topic_4_label));

        return vars.get(value);
    }
}
