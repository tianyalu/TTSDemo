# TTS示例demo

[TOC]

本文为`Android`原生`TTS(Text to Speech)` 文字转语音的简单使用`demo`，其`api`官方网址为：[https://developer.android.google.cn/reference/android/speech/tts/TextToSpeech.html](https://developer.android.google.cn/reference/android/speech/tts/TextToSpeech.html) 。

## 一、主要使用方法

```java
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
```

## 二、遇到的坑

```java
speak failed: not bound to TTS engine
```

去“设置” --> “无障碍” --> “文字转语音（TTS）输出” 中查看是否有“语音引擎”，本人测试修改为“科大讯飞语音引擎”后可以正常使用。