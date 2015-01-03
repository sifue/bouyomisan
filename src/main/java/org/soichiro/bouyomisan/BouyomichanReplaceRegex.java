package org.soichiro.bouyomisan;

/**
 * 棒読みちゃんの置換の正規表現情報
 */
public class BouyomichanReplaceRegex {

    final public int priority;
    final public String type;
    final public String from;
    final public String to;

    public BouyomichanReplaceRegex(int priority, String type, String from, String to) {
        this.priority = priority;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "BouyomichanReplaceRegex{" +
                "priority=" + priority +
                ", type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
