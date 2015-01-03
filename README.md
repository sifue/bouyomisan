# 棒読みさんって何？
![棒読みさん](https://raw.githubusercontent.com/sifue/bouyomisan/master/lips-icon.png)
Windowsにある[棒読みさん](http://chi.usamimi.info/Program/Application/BouyomiChan/)の機能の一部をMac用に移植したアプリです。

# 棒読みちゃんから移植した機能
![棒読みさんスクリーンショット](https://raw.githubusercontent.com/sifue/bouyomisan/master/doc/img/screenshot.png)
- ニコニコの読み上げ文化などに対応した、単語や正規表現による辞書置換
- 漢字のかなへの読み上げの変換
- Socket通信を利用した読み上げ
- 棒読みちゃんとおなじAquesTalkを利用するためのUIを提供

# インストール方法
1. [Java8(JREまたはJDK)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)のインストール
2. [SayKotoeri2](https://sites.google.com/site/nicohemus/home/saykotoeri2)のインストール
3. [棒読みさん](https://raw.githubusercontent.com/sifue/bouyomisan/master/bouyomisan-1.0.0.dmg)をインストール
![棒読みさんインストール](https://raw.githubusercontent.com/sifue/bouyomisan/master/doc/img/install.png)

# 使い方
起動すると、テキスト入力エリアがあるので入力後、読み上げボタンを押すことで読ませることができます。  
なおデフォルトでは棒読みちゃんとおなじ50001ポートにて同じ形式で読み上げコマンドのみを受け付けるようになっています。

# ライセンス
[The MIT License](http://opensource.org/licenses/MIT)  

# 開発者向け
## ビルド方法
JDK8をインストールして、git cloneの後、  
```
$ ./gradlew build
```
にてビルドすることができます。  
実行は、  
```
$ ./gradlew run
```
にて行うことができますので、カスタマイズなどしたい際にはご利用下さい。

## IntelliJ IDEAで利用したい
```
$ ./gradlew idea
```
以上のコマンドを実行することで、IDEAで開発できるようになります。

## 今後作りたいもの
- 音量、声、読み上げスピードなどを調節するUIの用意
- 置換辞書の編集機能


