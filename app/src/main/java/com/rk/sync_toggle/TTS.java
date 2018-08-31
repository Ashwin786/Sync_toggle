package com.rk.sync_toggle;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by user1 on 27/7/18.
 */
public class TTS extends Service implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private TextToSpeech mTts;
    private int current=5;
    private AudioManager audioManager;
//    private String spokenText="Magesh loves chitu kuruvi";
//    private String spokenText="Goudam is a bad boy";

    @Override
    public void onCreate() {
        // This is a good place to set spokenText
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setmaxvolume();
        mTts = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        speak_time(status);

    }

    @Override
    public void onUtteranceCompleted(String uttId) {
        setCurrentvolume();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void speak_time(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                mTts.setOnUtteranceCompletedListener(this);
                mTts.setPitch(1.3f);
                mTts.setSpeechRate(0.7f);
                // start speak
                mTts.speak(get_time(), TextToSpeech.QUEUE_FLUSH, null);
                HashMap<String, String> params = new HashMap<String, String>();

                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "stringId");

                mTts.speak(get_time(), TextToSpeech.QUEUE_FLUSH, params);
            }
        }

    }

    private String get_time() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH mm a");
        String myTime = "Time is " + df.format(c.getTime());
        Log.e("myTime",""+myTime);
//        int mHour = c.get(Calendar.HOUR_OF_DAY);
//        int mMinute = c.get(Calendar.MINUTE);
//        final String myTime = "The Time is " + String.valueOf(mHour)+ " " + String.valueOf(mMinute);
        return myTime;
    }

    private void setmaxvolume() {
        current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)-3, 0);
    }
    private void setCurrentvolume() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, 0);
    }
}
