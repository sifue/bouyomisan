package org.soichiro.bouyomisan;

import com.typesafe.config.ConfigFactory;

import java.io.File;

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
