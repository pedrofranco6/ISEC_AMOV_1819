package pedrofranco6.tp_amov.Game;

import java.util.Random;

public class GameEngine {

    private static final Random RANDOM = new Random();
    private static final int boardSize = 8;

    public static final int VAZIO = 0;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;

    private int gameMode;
    private int[] discs;
    private int currentPlayer;
    private int discsP1 = 2, discsP2 = 2;

    public GameEngine(int g) {
        gameMode = g;
        discs = new int[boardSize * boardSize];
        newGame();
    }

    public void play(int x, int y) {
        discs[boardSize * y + x] = currentPlayer;
        changeOtherDiscsColor(x, y);
        countDiscs();

        changePlayer();

        if (gameMode == 1) {
            computer();
        } else if (gameMode == 2) {
            computerAI();
        }
    }

    public boolean isValidPlay(int x, int y) {
        int i, j, dirx, diry;

        if (getDisc(x, y) != VAZIO)
            return false;
        for (diry = -1; diry < 2; diry++) {
            for (dirx = -1; dirx < 2; dirx++) {
                if (!isInsideGameBoard(x + dirx, y + diry) || getDisc(x + dirx, y + diry) == currentPlayer || getDisc(x + dirx, y + diry) == VAZIO)
                    continue;
                int col = x + dirx, row = y + diry;
                if (getDisc(col, row) == notCurrentPlayer()) {
                    for (i = col + dirx, j = row + diry; isInsideGameBoard(i, j); i += dirx, j += diry) {
                        if (getDisc(i, j) == notCurrentPlayer()) {
                            continue;
                        } else if (getDisc(i, j) == currentPlayer) {
                            return true;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void changeOtherDiscsColor(int x, int y) {
        int i, j, ii, jj, dirx, diry;

        for (diry = -1; diry < 2; diry++) {
            for (dirx = -1; dirx < 2; dirx++) {
                if (!isInsideGameBoard(x + dirx, y + diry) || getDisc(x + dirx, y + diry) == currentPlayer || getDisc(x + dirx, y + diry) == VAZIO)
                    continue;
                int col = x + dirx, row = y + diry;
                for (i = col + dirx, j = row + diry; isInsideGameBoard(i, j); i += dirx, j += diry) {
                    if (getDisc(i, j) == currentPlayer) {
                        for (ii = i - dirx, jj = j - diry; isInsideGameBoard(i, j); ii -= dirx, jj -= diry) {
                            if (getDisc(ii, jj) == notCurrentPlayer())
                                discs[boardSize * jj + ii] = currentPlayer;
                            else
                                break;
                        }
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    public int getNumPecasViradas(int x, int y) {
        int i, j, ii, jj, dirx, diry, pecasViradas = 0;

        for (diry = -1; diry < 2; diry++) {
            for (dirx = -1; dirx < 2; dirx++) {
                if (!isInsideGameBoard(x + dirx, y + diry) || getDisc(x + dirx, y + diry) == currentPlayer || getDisc(x + dirx, y + diry) == VAZIO)
                    continue;
                int col = x + dirx, row = y + diry;
                for (i = col + dirx, j = row + diry; isInsideGameBoard(i, j); i += dirx, j += diry) {
                    if (getDisc(i, j) == currentPlayer) {
                        for (ii = i - dirx, jj = j - diry; isInsideGameBoard(i, j); ii -= dirx, jj -= diry) {
                            if (getDisc(ii, jj) == notCurrentPlayer())
                                pecasViradas++;
                            else
                                break;
                        }
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }

        return pecasViradas;
    }

    private void countDiscs() {
        int player1 = 0, player2 = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (getDisc(i, j) == VAZIO)
                    continue;
                else {
                    if (getDisc(i, j) == PLAYER_1)
                        player1++;
                    else
                        player2++;
                }
            }
        }
        discsP1 = player1;
        discsP2 = player2;
    }

    public int getCountDiscs(int player) {
        return player == PLAYER_1  ? discsP1 : discsP2;
    }

    public boolean isInsideGameBoard(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public void changePlayer() {
        currentPlayer = (currentPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1);
    }

    public void newGame() {
        for (int i = 0; i < discs.length; i++)
            discs[i] = VAZIO;
        discs[28] = PLAYER_1;
        discs[35] = PLAYER_1;
        discs[27] = PLAYER_2;
        discs[36] = PLAYER_2;

        countDiscs();

        currentPlayer = PLAYER_1;
    }

    public int getDisc(int x, int y) {
        return discs[boardSize * y + x];
    }

    public void computer() {
        int x, y;

        do {
            x = RANDOM.nextInt(boardSize);
            y = RANDOM.nextInt(boardSize);
        } while (!isValidPlay(x, y));

        discs[boardSize * y + x] = PLAYER_2;
        changeOtherDiscsColor(x, y);
        countDiscs();

        changePlayer();
    }

    public void computerAI() {
        int pecasViradas = 0, x = -1,y = -1;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (isValidPlay(i, j)) {
                    int temp = getNumPecasViradas(i, j);
                    //System.out.println("temp: " + temp + " / pecasViradas: " + pecasViradas);
                    if (temp > pecasViradas) {
                        pecasViradas = temp;
                        x = i;
                        y = j;
                    }
                }

            }
        }

        if (x != -1 && y != -1) {
            discs[boardSize * y + x] = PLAYER_2;
            changeOtherDiscsColor(x, y);
            countDiscs();
        }

        changePlayer();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int notCurrentPlayer() {
        return currentPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    public boolean checkGameEnd() {
        return (discsP1 + discsP2 == boardSize * boardSize) || !checkForValidPlays();
    }

    public boolean checkForValidPlays() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (isValidPlay(i, j))
                    return true;
            }
        }
        return false;
    }

    public int getWinner() {
        if (discsP1 + discsP2 < boardSize * boardSize)
            return notCurrentPlayer();
        else if (discsP1 < discsP2)
            return VAZIO;
        else
            return discsP1 < discsP2 ? PLAYER_2 : PLAYER_1;
    }
}
