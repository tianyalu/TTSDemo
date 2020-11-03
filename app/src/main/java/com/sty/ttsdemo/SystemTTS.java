package com.sty.ttsdemo;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

/**
 * @Author: tian
 * @UpdateDate: 2020/11/3 2:15 PM
 */
public class SystemTTS extends UtteranceProgressListener implements TextToSpeech.OnUtteranceCompletedListener {
    private static final String TAG = SystemTTS.class.getSimpleName();
    private Context mContext;
    private static volatile SystemTTS singleton;
    private TextToSpeech textToSpeech; //系统语音播报类
    private boolean isSupport = true;

    public static SystemTTS getInstance(Context context) {
        if(singleton == null) {
            synchronized (SystemTTS.class) {
                if(singleton == null) {
                    singleton = new SystemTTS(context);
                }
            }
        }
        return singleton;
    }

    public SystemTTS(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                //系统语音初始化成功
                if(status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CANADA);
                    textToSpeech.setPitch(1.0f); //设置音调，值越大越尖（女生），值越小则变成男声，1.0是常规
                    textToSpeech.setSpeechRate(3.0f);
                    textToSpeech.setOnUtteranceProgressListener(SystemTTS.this);
                    textToSpeech.setOnUtteranceCompletedListener(SystemTTS.this);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //系统不支持中文播报
                        isSupport = false;
                        Log.e(TAG, "系统不支持中文播报");
                    }
                }
            }
        });
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.e(TAG, "onUtteranceCompleted : " + utteranceId);
    }

    @Override
    public void onStart(String utteranceId) {
        Log.e(TAG, "onStart : " + utteranceId);
    }

    @Override
    public void onDone(String utteranceId) {
        Log.e(TAG, "onDone : " + utteranceId);
    }

    @Override
    public void onError(String utteranceId) {
        Log.e(TAG, "onError : " + utteranceId);
    }

    public void playText(String playText) {
        if(!isSupport) {
            Log.e(TAG, "暂不支持");
            return;
        }
        if(textToSpeech != null) {
            textToSpeech.speak(playText, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void stopSpeak() {
        if(textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void destroy() {
        stopSpeak();
        if(textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}
