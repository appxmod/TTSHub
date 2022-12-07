package me.ag2s.tts.services;

import android.speech.tts.SynthesisRequest;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.regex.Pattern;

import me.ag2s.tts.utils.SU;

public class SSML {

    /**
     * 把常用的影响分句的重复符号合并
     */
    static final Pattern p0 = Pattern.compile("([\\s　！。？?])+");
    /**
     * 单字符断句符,排除后面有引号的情况
     */
    static final Pattern p1 = Pattern.compile("([;。：！？?])([^”’])");
    /**
     * 中英文省略号处理
     */
    static final Pattern p2 = Pattern.compile("(\\.{6}|…{2})([^”’])");
    /**
     * 多字符断句符，后面有引号的的情况
     */
    static final Pattern p3 = Pattern.compile("([。！？?][”’])([^，。！？?])");
//
//
//    /**
//     * 修复在小说中“重”作为量词时(读chong 2)的错误读音。这在修仙类小说中很常见.
//     */
    //static final Pattern p4 = Pattern.compile("重(?=[一二三四五六七八九十])|(?<=[一二三四五六七八九十])重");


    /**
     * 指定讲话语言。 目前，讲不同的语言是特定于语音的。
     * 如果调整神经语音的讲话语言，则为必需项。 如果使用 lang xml:lang，则必须提供区域设置。
     */
    private final String lang;


    /**
     * 请求的id
     */
    private String id;
    /**
     * 请求的时间戳
     */
    private String time;

    /**
     * 发音角色
     */
	String name;

    /**
     * 发音风格
     */
    private WeakReference<TtsStyle> style;

    /**
     * 发音内容
     */
    private TTSService.SyntheticalRequest content;

    private short pitch;
    private short rate;
    boolean useDict;
    //是否使用预览版
	boolean usePre;

    private static SSML instance;


    private SSML(TTSService.SyntheticalRequest sReq, String name, TtsStyle ttsStyle, boolean useDict, boolean usePre) {
		final SynthesisRequest request = sReq.request;
        this.content = sReq;
        this.useDict = useDict;
        this.usePre = usePre;
        this.name = name;
        this.style = new WeakReference<>(ttsStyle);
        this.time = SU.getTime();
        this.pitch = (short) (request.getPitch() - 100);
        this.rate = (short) (request.getSpeechRate());
        Locale locale = Locale.getDefault();
        if (name.contains("Multilingual")) {
            locale = Locale.CHINA;
        }

        this.lang = locale.getLanguage() + "-" + locale.getCountry();
        this.id = SU.getMD5String(sReq.getId());
        this.useDict = useDict;
        handleContent();
    }

    public static SSML getInstance(TTSService.SyntheticalRequest sReq, String name, TtsStyle ttsStyle, boolean useDict, boolean usePre) {
		final SynthesisRequest request = sReq.request;
		if (instance == null) {
            instance = new SSML(sReq, name, ttsStyle, useDict, usePre);
        } else {
            instance.content = sReq;
            instance.useDict = useDict;
            instance.usePre = usePre;
            instance.name = name;
            instance.style = new WeakReference<>(ttsStyle);
            instance.time = SU.getTime();
            instance.pitch = (short) (request.getPitch() - 100);
            instance.rate = (short) (request.getSpeechRate());
            instance.id = SU.getMD5String(sReq.getId());
            instance.handleContent();
        }
        return instance;
    }
	
	private void handleContent() {
	
	}
	
	
	@NonNull
    @Override
    public String toString() {
        String rateString = rate / 100 + "." + rate % 100;
//        if (!usePre) {
//            return "Path: ssml" + "\r\n" +
//                    "X-RequestId: " + id + "\r\n" +
//                    "X-Timestamp: " + time + "Z" + "\r\n" +
//                    "Content-Type: application/ssml+xml" + "\r\n\r\n" +
//                    "<speak xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:mstts=\"http://www.w3.org/2001/mstts\" xmlns:emo=\"http://www.w3.org/2009/10/emotionml\" version=\"1.0\" xml:lang=\"en-US\"><voice name=\"" + name + "\"><prosody rate=\"" + rateString + "%\" pitch=\"" + pitch + "%\">" + content.toString() + "\r\n" +
//                    "</prosody></voice></speak>";
//        }
        //String pitchString = pitch >= 0 ? "+" + pitch + "Hz" : pitch + "Hz";
        StringBuilder sb = new StringBuilder()
                .append("Path:ssml\r\n")
                .append("X-RequestId:").append(id).append("\r\n")
                .append("X-Timestamp:").append(time).append("Z\r\n")
                .append("Content-Type:application/ssml+xml\r\n\r\n");


        sb.append("<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:emo=\"http://www.w3.org/2009/10/emotionml\"  xmlns:mstts=\"https://www.w3.org/2001/mstts\" xml:lang=\"").append(lang).append("\">");
        sb.append("<voice  name=\"").append(name).append("\">");
        if (usePre) {
            sb.append("<lang xml:lang=\"").append(lang).append("\">");
        }
        sb.append("<prosody pitch=\"").append(pitch).append("%\" ").append("rate=\"").append(rateString).append("\" ").append("volume=\"").append(style.get().getVolume()).append("\">");
		
		final String text = content.makeUtterance(this);
        if (usePre) {
            sb.append("<mstts:express-as  style=\"").append(style.get().value).append("\" styledegree=\"").append(style.get().getStyleDegree()).append("\" ><p>").append(text).append("</p></mstts:express-as>");
        } else {
            sb.append("").append(text).append("");
        }


        sb.append("</prosody>");
        if (usePre) {
            sb.append("</lang>");
        }

        sb.append("</voice></speak>");

        return sb.toString();
    }
}
