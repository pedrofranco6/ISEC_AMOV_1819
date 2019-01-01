package pedrofranco6.tp_amov;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import pedrofranco6.tp_amov.Game.GameEngine;
import pedrofranco6.tp_amov.Game.TabuleiroView;

import static pedrofranco6.tp_amov.R.id.tabuleiro;

public class GameActivity extends AppCompatActivity {

    private TabuleiroView tabuleiroView;
    private GameEngine gameEngine;
    private int gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        gameMode = bundle.getInt("gameMode");

        tabuleiroView = findViewById(tabuleiro);
        gameEngine = new GameEngine(gameMode);
        tabuleiroView.setGameEngine(gameEngine);
        tabuleiroView.setMainActivity(this, gameMode, (TextView) findViewById(R.id.pecasP1), (TextView) findViewById(R.id.pecasP2));
    }

    public void gameEnded(int player) {
        String msg = (player == 0) ? "Game Ended. Tie" : "GameEnded. P" + player + " win";

        new AlertDialog.Builder(this).setTitle("Reversi").
                setMessage(msg).
                setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        newGame();
                    }
                }).show();
    }

    private void newGame() {
        gameEngine.newGame();
        tabuleiroView.getPecasP1().setText(Integer.toString(gameEngine.getCountDiscs(gameEngine.PLAYER_1)));
        tabuleiroView.getPecasP2().setText(Integer.toString(gameEngine.getCountDiscs(gameEngine.PLAYER_2)));

        tabuleiroView.invalidate();
    }
}
