package test;

import java.util.Scanner;

public class Mahjong {
    // 定数
    // 順子一覧、index で指定したいので -1 している
    final int[][] listOfShuntsu = {
            {0, 1, 2}, {1, 2, 3}, {2, 3, 4}, {3, 4, 5}, {4, 5, 6},
            {5, 6, 7}, {6, 7, 8},
    };
    // フィールド変数
    private String paishi;
    private int[] listOfCountOfPaiInPaishi;
    // private メソッド
    // 最も数字の小さい順子を 1 つ除外する
    private int[] excludeShuntsu(int[] listOfCountOfPaiInPaishi) {
        for (int[] shuntsu:this.listOfShuntsu) {
            if (listOfCountOfPaiInPaishi[shuntsu[0]] >= 1 && listOfCountOfPaiInPaishi[shuntsu[1]] >= 1 && listOfCountOfPaiInPaishi[shuntsu[2]] >= 1) {
                listOfCountOfPaiInPaishi[shuntsu[0]]--; // 除外 (= カウントを -1 する)
                listOfCountOfPaiInPaishi[shuntsu[1]]--; // 除外 (= カウントを -1 する)
                listOfCountOfPaiInPaishi[shuntsu[2]]--; // 除外 (= カウントを -1 する)
                return listOfCountOfPaiInPaishi;
            }
        }
        return null;
    }
    // 最も数字の小さい刻子を 1 つ除外する
    private int[] excludeKotsu(int[] listOfCountOfPaiInPaishi) {
        for (int i = 0; i < 9; i++) {
            if (listOfCountOfPaiInPaishi[i] >= 3) {
                listOfCountOfPaiInPaishi[i] -= 3;
                return listOfCountOfPaiInPaishi;
            }
        }
        return null;
    }
    // コンストラクタ
    public Mahjong() {}
    // print
    public void print(String string) {
        System.out.println(string);
    }
    // 牌姿を入力
    public void scannerPaishi() {
        // 説明文を表示
        this.print("牌姿を入力してください");
        // 牌姿を入力
        Scanner scanner = new Scanner(System.in);
//        this.print(scanner.next());
        // 変数に格納
        this.paishi = scanner.next();
    }
    // 牌姿を表示
    public void printPaishi() {
        this.print(this.paishi);
    }
    // 牌姿を検査 - 長さ
    public boolean isValidLength() {
        // 牌姿の長さが 13 でない場合
        if (!(this.paishi.length() == 13)) {
            this.print("牌姿の長さが 13 ではありません。");
            return false;
        }
        return true;
    }
    // 牌姿を検査 - 文字
    public boolean isValidChar() {
        // 牌姿に 1-9 以外の文字が含まれる場合
        if (!(this.paishi.matches("[1-9]{13}"))) {
            this.print("牌姿に 1-9 以外の文字が含まれています。");
            return false;
        }
        return true;
    }
    // 牌姿から各牌の個数をカウントし、配列にしたものを作成
    public void createListOfCountOfPaiInPaishi() {
        // 配列を初期化
        this.listOfCountOfPaiInPaishi = new int[9];
        // 牌姿を分割
        String[] listOfPaiInPaishi = this.paishi.split("");
        // カウントを追加していく
        for (String paiInPaishi:listOfPaiInPaishi) {
            int index = Integer.parseInt(paiInPaishi) - 1;
            this.listOfCountOfPaiInPaishi[index]++;
        }
    }
    // 牌姿を検査 - カウントが 5 以上の牌が存在しないか
    public boolean isValidCount() {
        for (int i = 0; i < 9; i++) {
            if (this.listOfCountOfPaiInPaishi[i] >= 5) {
                this.print("牌姿に " + String.valueOf(i+1) + " が 5 枚以上含まれています。");
                return false;
            }
        }
        return true;
    }
    // 待ちを確認する牌を 1~9 までループ
    public void loopMachi() {
        // 待ちを確認する牌を 1~9 までループ
        for (int i = 0; i < 9; i++) {
            // この index でアガれるか否か(元の配列を破壊しないよう clone)
            if (isAbleToAgariInThisIndexOfMachi(listOfCountOfPaiInPaishi.clone(), i)) {
                // true なら表示
                System.out.println(i+1);
            }
        }  
    }
    // この待ち(index)でアガれるか否か
    private boolean isAbleToAgariInThisIndexOfMachi(int[] listOfCountOfPaiInPaishi, int indexOfMachi) {
        // 待ちを確認する牌を追加し、14枚にする
        listOfCountOfPaiInPaishi[indexOfMachi]++;
        // 雀頭に指定する牌を 1~9 までループ
        for (int i = 0; i < 9; i++) {
            // この雀頭で進めた結果、true なら return(元の配列を破壊しないよう clone)
            if (isAbleToAgariInThisIndexOfJanto(listOfCountOfPaiInPaishi.clone(), i)) {
                return true;
            }
        }
        // いずれの雀頭でもダメだった(= for ループが正常に終了している)場合
        return false;
    }
    // この雀頭(index)でアガれるか否か
    private boolean isAbleToAgariInThisIndexOfJanto(int[] listOfCountOfPaiInPaishi, int indexOfJanto) {
        // 雀頭を抜く
        listOfCountOfPaiInPaishi[indexOfJanto] -= 2;
        // マイナスになっている場合(=そもそも雀頭として抜けない)場合は return
        if (listOfCountOfPaiInPaishi[indexOfJanto] < 0) {
            return false;
        }
        // 面子を抜く。4面子それぞれに順子と刻子の2通りがあるので、2 ** 4 で全16パターン分ループ。
        for (int i = 0; i < 16; i++) {
            // この面子パターンで進めた結果、true なら return
            if (isAbleToAgariInThisNumberOfPatternOfMentsu(listOfCountOfPaiInPaishi.clone(), i)) {
                return true;
            }
        }
        // ここまで到達するということは、16パターンのいずれでも面子を除外しきれなかったということなので、false を return
        return false;
    }
    // この面子パターン(番号)でアガれるか否か
    private boolean isAbleToAgariInThisNumberOfPatternOfMentsu(int[] listOfCountOfPaiInPaishi, int numberOfPatternOfMentsu) {
        // 商
        int sho = numberOfPatternOfMentsu;
        // 4回(= 4面子分)繰り返す
        for (int l = 0; l < 4; l++) {
            // 順子 - 剰余が0か1かで順子を抜くか刻子を抜くかを決める、0なら順子
            if (sho % 2 == 0) {
                // 牌姿を更新
                listOfCountOfPaiInPaishi = excludeShuntsu(listOfCountOfPaiInPaishi);
                // null なら処理終了、16通りの次の抜き方へ
                if (listOfCountOfPaiInPaishi == null) {
                    return false;
                }
            }
            //　刻子 - 剰余が0か1かで順子を抜くか刻子を抜くかを決める、0でないなら刻子
            else {
                // 牌姿を更新
                listOfCountOfPaiInPaishi = excludeKotsu(listOfCountOfPaiInPaishi);
                // null なら処理終了、16通りの次の抜き方へ
                if (listOfCountOfPaiInPaishi == null) {
                    return false;
                }
            }
            // 商を更新
            sho /= 2;
        }
        // ここまで到達できれば、4つの面子を除外することができたということなので、true を return
        return true;
    }
}
