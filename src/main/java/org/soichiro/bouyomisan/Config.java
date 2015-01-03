package org.soichiro.bouyomisan;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 棒読さんの設定のシングルトン
 */
public class Config {

    /**
     * シングルトンインスタンス
     */
    volatile private static Config singleton = null;

    /**
     * 読み上げコマンド
     */
    public final String sayCommand;

    /**
     * 読み上げボリューム
     */
    public final String sayVolume;

    /**
     * 読み上げボイス設定
     */
    public final String sayVoice;

    /**
     * 読み上げスピード
     */
    public final String saySpeed;

    /**
     * 読み上げスピード
     */
    public final String serverPort;

    /**
     * ソート済み置換単語リスト
     */
    private final List<BouyomichanReplaceWord> listReplaceWords;

    /**
     * ソート済み置換正規表現リスト
     */
    private final List<BouyomichanReplaceRegex> listReplaceRegexes;

    /**
     * コンストラクタ
     */
    private Config() {
        com.typesafe.config.Config conf =
                ConfigFactory.parseFile(new File(
                        System.getProperty("bouyomisan.conf.path", "bouyomisan.conf")));
        this.sayCommand = conf.getString("bouyomisan.say.command");
        this.sayVoice = conf.getString("bouyomisan.say.voice");
        this.sayVolume = conf.getString("bouyomisan.say.volume");
        this.saySpeed = conf.getString("bouyomisan.say.speed");
        this.serverPort = conf.getString("bouyomisan.server.port");

        this.listReplaceWords = new ArrayList<BouyomichanReplaceWord>();
        List<String> listConfWord = conf.getStringList("bouyomichan.replace.words");
        for (String line: listConfWord) {
            String[] split = line.split("    ");
            try {
                BouyomichanReplaceWord w = new BouyomichanReplaceWord(
                        Integer.parseInt(split[0]),
                        split[1],
                        split[2],
                        split.length < 4 ? "" : split[3]
                );
                this.listReplaceWords.add(w);
            } catch (Exception ignore) {
                System.err.println("パース出来ませんでした。 word:" + line);
            }
        }
        Collections.sort(listReplaceWords, new Comparator<BouyomichanReplaceWord>() {
            @Override
            public int compare(BouyomichanReplaceWord o1, BouyomichanReplaceWord o2) {
                return o2.priority - o1.priority;
            }
        });

        this.listReplaceRegexes = new ArrayList<BouyomichanReplaceRegex>();
        List<String> listConfRegex = conf.getStringList("bouyomichan.replace.regexes");
        for (String line: listConfRegex) {
            String[] split = line.split("    ");
            try {
                BouyomichanReplaceRegex r = new BouyomichanReplaceRegex(
                        Integer.parseInt(split[0]),
                        split[1],
                        split[2],
                        split[3]
                );
                this.listReplaceRegexes.add(r);
            } catch (Exception ignore) {
                System.err.println("パース出来ませんでした。 regex:" + line);
            }
        }
        Collections.sort(listReplaceRegexes, new Comparator<BouyomichanReplaceRegex>() {
            @Override
            public int compare(BouyomichanReplaceRegex o1, BouyomichanReplaceRegex o2) {
                return o2.priority - o1.priority;
            }
        });
    }

    /**
     * 設定のシングルトンインスタンスを取得する
     * @return
     */
    synchronized static public Config getSingleton() {
        if(singleton == null ){
            singleton = new Config();
        }
        return singleton;
    }

}
