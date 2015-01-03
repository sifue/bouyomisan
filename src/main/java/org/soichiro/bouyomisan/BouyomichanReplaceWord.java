package org.soichiro.bouyomisan;

/**
 * 棒読みちゃんの単語置換の情報
 */
public class BouyomichanReplaceWord {

    final public int priority;
    final public String type;
    final public String from;
    final public String to;

    public BouyomichanReplaceWord(int priority, String type, String from, String to) {
        this.priority = priority;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "BouyomichanReplaceWord{" +
                "priority=" + priority +
                ", type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
