package test;

public class Main {
    public static void main(String args[]) {
        Mahjong mahjong = new Mahjong();
        
        while (true) {
            // 牌姿を入力
            mahjong.scannerPaishi();
            // 牌姿を検査 - 長さ
            if (!(mahjong.isValidLength())) {
                continue;
            }
            // 牌姿を検査 - 文字
            if (!(mahjong.isValidChar())) {
                continue;
            }
            // 個数を計測
            mahjong.createListOfCountOfPaiInPaishi();
            // 牌姿を検査 - カウント
            if (!(mahjong.isValidCount())) {
                continue;
            }
            mahjong.print("有効な牌姿です。");
            // 待ちを調査
            mahjong.loopMachi();
            // break
            break;
        }
    }
}