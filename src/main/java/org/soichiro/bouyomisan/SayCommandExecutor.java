package org.soichiro.bouyomisan;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * SayCommandを実行するサービス
 */
public class SayCommandExecutor {

    private volatile String commandPath;

    /**
     * コンストラクタ
     * @param commandPath
     */
    public SayCommandExecutor(String commandPath) {
        try {
            setCommandPath(commandPath);
        } catch (IOException e) {
            new IllegalArgumentException(e);
        }
    }

    /**
     * 読み上げの実行
     * @param text
     */
    synchronized public void execute(String text) {
        if(text == null || text.isEmpty()) return;

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

        String readingText =  kanas.toString();
        System.out.println("readingText: " + readingText);

        ProcessBuilder pb = new ProcessBuilder();
        pb.command(commandPath, readingText);
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * commandPathを設定
     * @param commandPath
     */
    private void setCommandPath(String commandPath) throws IOException {
        if(new File(commandPath).isFile()) {
            this.commandPath = commandPath;
        } else {
            throw new IOException(
                    String.format("読み上げコマンド %s が存在しません.", commandPath));
        }
    }
}
