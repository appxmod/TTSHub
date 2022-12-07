package me.ag2s.tts.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import okio.ByteString;

public class SU {

    static final Pattern NoVoicePattern = Pattern.compile("[\\s\\p{C}\\p{P}\\p{Z}\\p{S} ]");
    //static final Pattern NoVoicePattern = Pattern.compile("^[\\s\\p{C}\\p{P}\\p{Z}\\p{S} ]+$");
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);


//    /**
//     * 得到分句后格式化的内容
//     * @param txt String
//     * @return String
//     */
//    public static String getFormatSentence(CharSequence txt) {
//        txt=p0.matcher(txt).replaceAll("$1");//把常用的影响分句的重复符号合并
//        txt=p1.matcher(txt).replaceAll("$1</p><p>$2");//单字符断句符,排除后面有引号的情况
//        txt=p2.matcher(txt).replaceAll("<break strength='strong' />$2");//中英文省略号停顿处理
//        txt=p3.matcher(txt).replaceAll("$1</p><p>$2");//多字符断句符，后面有引号的的情况
//        txt=p4.matcher(txt).replaceAll("<phoneme alphabet='sapi' ph='chong 2'>重</phoneme>");
//        //修复在小说中“重”作为量词时(读chong 2)的错误读音。这在修仙类小说中很常见.
//
//        StringBuilder sb=new StringBuilder(txt);
//        //自定义校正发音
//        List<TtsDict> dictList = TtsDictManger.getInstance().getDict();
//        for (TtsDict dict : dictList) {
//            CommonTool.replaceAll(sb, dict.getWorld(), dict.getXML());
//        }
//
//        txt="<p>"+sb.toString()+"</p>";
//        Log.e("TXT01", "ED:"+ txt);
//        return txt.toString();
//
//    }

//    public static String getSSML(StringBuilder text, String id, String time, String name, String style, String styleDegree, int pitch, int rate, int volume, String lang) {
//
//        String rateString = rate >= 0 ? "+" + rate + "%" : rate + "%";
//        String pitchString = pitch >= 0 ? "+" + pitch + "Hz" : pitch + "Hz";
//       // Log.e("TXT", text.toString());
//
//        return "X-RequestId:" + id + "\r\n" +
//                "Content-Type:application/ssml+xml\r\n" +
//                "X-Timestamp:" + time + "Z\r\n" +
//
//                "Path:ssml\r\n\r\n" +
//                "<speak version=\"1.0\" xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:mstts=\"https://www.w3.org/2001/mstts\" xml:lang=\"" + lang + "\">" +
//                "<voice  name=\"" + name + "\">" +
//                "<prosody pitch=\"" + pitchString + "\" " +
//                "rate =\"" + rateString + "\" " +
//                "volume=\"" + volume + "\">" +
//                "<mstts:express-as  style=\"" + style + "\" styledegree=\"" + styleDegree + "\" >" + getFormatSentence(text.toString()) + "</mstts:express-as>" +
//                "</prosody></voice></speak>";
//    }


    public static boolean isNoVoice(CharSequence charSequence) {
        //return NoVoicePattern.matcher(charSequence).matches();
        return NoVoicePattern.matcher(charSequence).replaceAll("").isEmpty();
    }

    /**
     * 移除所有空格
     *
     * @param sb StringBuilder
     */
    @SuppressWarnings("unused")
    public static void removeAllBlankSpace(StringBuilder sb) {
        int j = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (!(Character.isWhitespace(sb.charAt(i)) || sb.charAt(i) == '　')) {
                sb.setCharAt(j++, sb.charAt(i));
            }
        }
        sb.delete(j, sb.length());
    }

    /**
     * 移除首尾空格(包含中文空格)
     *
     * @param sb StringBuilder
     */
    public static void Trim(StringBuilder sb) {
        if (sb == null || sb.length() == 0) return;
        //去除前面的空格
        int st = 0;
        while (Character.isWhitespace(sb.charAt(st)) || sb.charAt(st) == '　') {
            st++;
        }
        if(st>0){
            sb.delete(0,st);
        }


        //去除后面的空格
        int ed = sb.length();
        while (Character.isWhitespace(sb.charAt(ed-1)) || sb.charAt(ed-1) == '　'){
            ed--;
        }
        if(ed<sb.length()){
            sb.delete(ed,sb.length());
        }

    }


    public static void replace(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
    }

    public static void replaceAll(StringBuilder sb, String regex, String replacement) {
        String s = Pattern.compile(regex).matcher(sb.toString()).replaceAll(replacement);
        sb.delete(0, sb.length());
        sb.append(s);
    }

//    public static void replaceAll(StringBuilder sb, Pattern pattern, String replacement) {
//        Matcher m = pattern.matcher(sb);
//        int start = 0;
//        while (m.find(start)) {
//            sb.replace(m.start(), m.end(), replacement);
//            start = m.start() + replacement.length();
//        }
//    }


    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();

        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

//
//    // 移除字符串首尾空字符的高效方法(利用ASCII值判断,包括全角空格)
//    public static String FixTrim(String s) {
//        if (s == null || s.isEmpty()) {
//            return "";
//        }
//        int start = 0;
//        int len = s.length();
//        int end = len - 1;
//        while (start < end && (s.charAt(start) <= 0x20 || s.charAt(start) == '　')) {
//            ++start;
//        }
//        while (start < end && (s.charAt(end) <= 0x20 || s.charAt(end) == '　')) {
//            --end;
//        }
//        if (end < len) {
//            ++end;
//        }
//        return (start > 0 || end < len) ? s.substring(start, end) : s;
//
//    }

    /**
     * 获取时间戳
     *
     * @return String time
     */
    public static String getTime() {
        Date date = new Date();
        return sdf.format(date);
    }


    public static String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        if ("TW".equals(countryCode) && Locale.getDefault().getCountry().equals("CN")) {
            return "";
        }
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
//
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 对传递过来的字符串进行md5加密
     *
     * @param str 待加密的字符串
     * @return 字符串Md5加密后的结果
     */
    public static String getMD5String(String str) {
        return ByteString.of(str.getBytes(StandardCharsets.UTF_8)).md5().hex();
    }
}
