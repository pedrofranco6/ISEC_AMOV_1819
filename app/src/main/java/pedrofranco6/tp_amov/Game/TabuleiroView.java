package pedrofranco6.tp_amov.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import pedrofranco6.tp_amov.GameActivity;
import pedrofranco6.tp_amov.R;

public class TabuleiroView extends View {

    private static final int boardSize = 8;

    private static final int LINE_THICK = 4;
    private static final int DISC_MARGIN = 8;
    private int width, height, discW, discH;
    private Paint gridPaint, p1Paint, p2Paint;
    private GameEngine gameEngine;
    private GameActivity activity;
    private int gameMode;
    private TextView pecasP1, pecasP2;

    public TabuleiroView(Context context) {
        super(context);
    }

    public TabuleiroView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gridPaint = new Paint();
        p1Paint = new Paint();
        p1Paint.setColor(Color.BLACK);
        p2Paint = new Paint();
        p2Paint.setColor(Color.GREEN);
    }

    public void setMainActivity(GameActivity a, int g, TextView p1, TextView p2) {
        activity = a;
        gameMode = g;
        pecasP1 = p1;
        pecasP1.setText(Integer.toString(gameEngine.getCountDiscs(gameEngine.PLAYER_1)));
        pecasP2 = p2;
        pecasP2.setText(Integer.toString(gameEngine.getCountDiscs(gameEngine.PLAYER_2)));
    }

    public void setGameEngine(GameEngine g) {
        gameEngine = g;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        discW = (width - LINE_THICK) / boardSize;
        discH = (height - LINE_THICK) / boardSize;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGrid(canvas);
        drawBoard(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) (event.getX() / discW);
        int y = (int) (event.getY() / discH);

        if (gameMode == 0 || ((gameMode == 1 || gameMode == 2) && gameEngine.getCurrentPlayer() == gameEngine.PLAYER_1)) {
            if (gameEngine.isValidPlay(x, y)) {
                gameEngine.play(x, y);
                pecasP1.setText(Integer.toString(gameEngine.getCountDiscs(gameEngine.PLAYER_1)));
                pecasP2.setText(Integer.toString(gameEngine.getCountDiscs(gameEngine.PLAYER_2)));
                if (gameEngine.checkGameEnd())
                    activity.gameEnded(gameEngine.getWinner());
            }
        }

        invalidate();
        return super.onTouchEvent(event);
    }

    private void drawBoard(Canvas canvas) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameEngine.getDisc(i, j) != 0)
                    drawDisc(canvas, i, j);
            }
        }
    }

    private void drawGrid(Canvas canvas) {
        for (int i = -1; i < boardSize; i++) {
            // vertical lines
            float left = discW * (i + 1);
            float right = left + LINE_THICK;
            float top = 0;
            float bottom = height;

            canvas.drawRect(left, top, right, bottom, gridPaint);

            // horizontal lines
            float left2 = 0;
            float right2 = width;
            float top2 = discH * (i + 1);
            float bottom2 = top2 + LINE_THICK;

            canvas.drawRect(left2, top2, right2, bottom2, gridPaint);
        }
    }

    private void drawDisc(Canvas canvas, int x, int y) {
        float xx = (discW * x) + discW / 2;
        float yy = (discH * y) + discH / 2;

        if (gameEngine.getDisc(x, y) == GameEngine.PLAYER_1)
            canvas.drawCircle(xx, yy, Math.min(discW, discH) / 2 - DISC_MARGIN * 2, p1Paint);
        else
            canvas.drawCircle(xx, yy, Math.min(discW, discH) / 2 - DISC_MARGIN * 2, p2Paint);
    }

    public TextView getPecasP1() {
        return pecasP1;
    }

    public TextView getPecasP2() {
        return pecasP2;
    }
}
