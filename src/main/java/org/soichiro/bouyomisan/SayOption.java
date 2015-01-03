package org.soichiro.bouyomisan;

/**
 * 読み上げ実行の際のオプション
 * デフォルト値を使う場合にはnullを代入して利用する
 */
public class SayOption {

    /**
     * 読み上げテキスト
     */
    public final String sayText;

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
     * コンストラクタ
     * @param sayText
     * @param sayVolume
     * @param sayVoice
     * @param saySpeed
     */
    public SayOption(String sayText, String sayVolume, String sayVoice, String saySpeed) {
        this.sayText = sayText;
        this.sayVolume = sayVolume;
        this.sayVoice = sayVoice;
        this.saySpeed = saySpeed;
    }

    @Override
    public String toString() {
        return "SayOption{" +
                "sayText='" + sayText + '\'' +
                ", sayVolume='" + sayVolume + '\'' +
                ", sayVoice='" + sayVoice + '\'' +
                ", saySpeed='" + saySpeed + '\'' +
                '}';
    }
}
