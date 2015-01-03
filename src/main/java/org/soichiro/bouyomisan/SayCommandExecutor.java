package org.soichiro.bouyomisan;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SayCommandを実行するサービス
 */
public class SayCommandExecutor {

    /**
     * コンストラクタ
     */
    public SayCommandExecutor() {
    }

    /**
     * 読み上げの実行
     * 読み上げられたカナのテキストを返すが、
     * 読みあげられなかった時には空文字を返す
     *
     * @param option
     * @return 読み上げられたカナのテキスト
     */
    synchronized public String execute(SayOption option) {
        if(option.sayText == null || option.sayText.isEmpty()) return "";

        String replacedText = getReplacedText(option.sayText);
        String readingText = getKanaReading(replacedText);
        Config conf = Config.getSingleton();
        if(!new File(conf.sayCommand).isFile()) {
            throw new IllegalStateException(
                    String.format("読み上げコマンド %s が存在しません.", conf.sayCommand));
        }

        List<String> commandList = new ArrayList<String>();
        commandList.add(conf.sayCommand);
        commandList.add("-p");
        commandList.add(option.sayVoice == null
                ? conf.sayVoice : option.sayVoice);
        commandList.add("-s");
        commandList.add(option.saySpeed == null
                ? conf.saySpeed : option.saySpeed);
        commandList.add("-b");
        commandList.add(option.sayVolume == null
                ? conf.sayVolume : option.sayVolume);
        commandList.add(readingText);
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(commandList);

        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return readingText;
    }

    /**
     * 置換設定を利用して読み上げテキストを置換したものを取得する
     * @param text
     * @return
     */
    private String getReplacedText(String text) {
        String replacedWord = text;
        List<BouyomichanReplaceWord> listReplaceWords = Config.getSingleton().getListReplaceWords();
        for (BouyomichanReplaceWord w :listReplaceWords) {
            replacedWord = replacedWord.replaceAll(w.from, w.to);
        }

        List<BouyomichanReplaceRegex> listReplaceRegexes = Config.getSingleton().getListReplaceRegexes();
        for (BouyomichanReplaceRegex r: listReplaceRegexes) {
            Pattern p = Pattern.compile(r.from, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(replacedWord);
            if(m.find()){
                int count = m.groupCount();
                for (int i = 1; i <= count ; i++) {
                    replacedWord = r.to.replaceAll("\\$" + Integer.valueOf(i), m.group(i));
                }
            }
        }
        return replacedWord.trim(); // 余計なものが入ることがあるので最後にトリムする
    }

    /**
     * 漢字かな変換をしたよみを取得する
     * @param text
     * @return
     */
    private String getKanaReading(String text) {
        Tokenizer tokenizer = Tokenizer.builder().build();
        List<Token> tokens = tokenizer.tokenize(text);
        StringBuffer kanas = new StringBuffer();
        for (Token token: tokens) {
            String reading = token.getReading();
            if(reading == null) {
                kanas.append(token.getSurfaceForm());
            } else {
                kanas.append(reading);
            }
        }
        return kanas.toString();
    }
}
