package com.bachors.subtitleresync;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by @bachors on 6/12/2018.
 * https://github.com/bachors/Android-SubtitleResync
 */

public class SubtitleResync {

    public static final boolean ADD = true;
    public static final boolean REMOVE = false;

    private boolean add;
    private int ms;
    private String srt;
    private Listener listener;

    public SubtitleResync(){

    }

    public SubtitleResync subtitle(String srt){
        this.srt = srt;
        return this;
    }

    public SubtitleResync delay(boolean add){
        this.add = add;
        return this;
    }

    public SubtitleResync millisecond(int ms){
        this.ms = ms;
        return this;
    }

    public void resync(Listener listener) {
        this.listener = listener;
        new Init().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class Init extends AsyncTask<String, Integer, Void> {

        private Init(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg0) {

            Pattern p = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2}),(\\d{3})");
            Matcher m = p.matcher(srt);
            ArrayList<String> matches = new ArrayList<>();
            while(m.find()) {
                if(!matches.contains(m.group(0))){
                    matches.add(m.group(0));
                }
            }
            if(add){
                Collections.sort(matches, Collections.reverseOrder());
            }
            for(int i=0; i<matches.size(); i++){
                String tm[] = matches.get(i).split(",");
                String sc[] = tm[0].split(":");
                int hors    = Integer.parseInt(sc[0]);
                int mins    = Integer.parseInt(sc[1]);
                int secs    = Integer.parseInt(sc[2]);
                String mls  = "";
                int ofs     = 0;
                int mlc;
                int scn;
                if(add){
                    mlc = Integer.parseInt(tm[1]) + ms;
                    if(mlc > 999){
                        ofs = mlc / 1000;
                        mlc = mlc % 1000;
                    }
                    if(String.valueOf(mlc).length() == 1){
                        mls = "00";
                    }else if(String.valueOf(mlc).length() == 2){
                        mls = "0";
                    }
                    scn = ((hors * 3600 + mins * 60 + secs) * 1000 / 1000) + ofs;
                }else{
                    mlc = Integer.parseInt(tm[1]) - ms;
                    scn = ((hors * 3600 + mins * 60 + secs) * 1000 / 1000);
                    if(mlc < 0){
                        int tmp = ((scn * 1000) + mlc) / 1000;
                        mlc     = ((scn * 1000) + mlc) % 1000;
                        scn     = tmp;
                    }
                    if(String.valueOf(mlc).length() == 1){
                        mls = "00";
                    }else if(String.valueOf(mlc).length() == 2){
                        mls = "0";
                    }
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String date = sdf.format(scn * 1000L);
                String replace = date + "," + mls + mlc;
                srt = srt.replaceAll(matches.get(i), replace);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listener.onResponse(srt);
        }

    }

    public interface Listener {
        void onResponse(String srt);
    }

}
