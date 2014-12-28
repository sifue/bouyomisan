package org.soichiro.bouyomisan;

import java.io.File;
import java.io.IOException;

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
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(commandPath, text);
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
    public void setCommandPath(String commandPath) throws IOException {
        if(new File(commandPath).isFile()) {
            this.commandPath = commandPath;
        } else {
            throw new IOException(
                    String.format("読み上げコマンド %s が存在しません.", commandPath));
        }
    }
}
