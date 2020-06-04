package com.example.a2.game;

import android.content.Context;
import android.graphics.*;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.a2.R;

/**
 * Responsible for the display of the game, the logical judgment of the game is in Game.java
 */

// SurfaceHolder.Callback can monitor the status of SurfaceView,
// such as SurfaceView change, create, destroy, etc.
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "GameView";
    private static final boolean DEBUG = true;

    // Define SurfaceHolder object
    SurfaceHolder mHolder = null;

    // Chess piece paint
    private Paint chessPaint = new Paint();

    // Chessboard paint
    private Paint boardPaint = new Paint();
    private int boardColor = 0;
    private float boardWidth = 0.0f;
    private float anchorWidth = 0.0f;

    // Clear screen paint
    Paint clear = new Paint();

    public int[][] mChessArray = null;

    // Bitmap object
    Bitmap mBlack = null;
    Bitmap mBlackNew = null;
    Bitmap mWhite = null;
    Bitmap mWhiteNew = null;

    // Declare the width and height of the board, initialized to 0
    int mChessboardWidth = 0;
    int mChessboardHeight = 0;
    int mChessSize = 0;

    Context mContext = null;

    private Game mGame;

    // Create a coordinate object
    private Coordinate focus;
    private boolean isDrawFocus;
    private Bitmap bFocus;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        boardColor = Color.BLACK;
        boardWidth = getResources().getDimensionPixelSize(R.dimen.boardWidth);
        anchorWidth = getResources().getDimensionPixelSize(R.dimen.anchorWidth);
        focus = new Coordinate();
        init();
    }

    private void init() {
        // Initialize the SurfaceHolder object mHolder
        // use getHolder () to get the interface in the current method
        // use the callback function
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        // Set the canvas background to transparent
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        // Set the anti-aliasing of the chess piece's brush
        chessPaint.setAntiAlias(true);
        // Set the thickness of the checkerboard brush
        boardPaint.setStrokeWidth(boardWidth);
        // Set the color of the checkerboard brush
        boardPaint.setColor(boardColor);
        // The clearing brush only clears the upper picture when the two pictures intersect.
        // This will only clear chess pieces and keep chessboard.
        clear.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        setFocusable(true);
    }

    /**
     * Set up the game
     *
     * @param game
     */

    // Layout redraw
    public void setGame(Game game) {
        mGame = game;
        requestLayout();
    }

    // Take control measurements and
    // set the length and width of chessboard to the same
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set the height to be the same as the width
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mGame != null) {
            if (width % mGame.getWidth() == 0) {
                float scale = ((float) mGame.getHeight()) / mGame.getWidth();
                int height = (int) (width * scale);
                // Set the size of the custom view
                setMeasuredDimension(width, height);
            } else {
                width = width / mGame.getWidth() * mGame.getWidth();
                float scale = ((float) mGame.getHeight()) / mGame.getWidth();
                int height = (int) (width * scale);
                // Set the size of the custom view
                setMeasuredDimension(width, height);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    // This is subclass onLayout and used to determine the position of the control relative to the parent class
    // Set the width and height of chessboard the same as the height and width in Game.class
    // Set the size of the chess piece to the width of the control / the number of columns.
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (DEBUG)
            Log.d(TAG, "left=" + left + "  top=" + top + " right=" + right + " bottom=" + bottom);
        if (mGame != null) {
            mChessboardWidth = mGame.getWidth();
            mChessboardHeight = mGame.getHeight();
            mChessSize = (right - left) / mChessboardWidth;
            Log.d(TAG, "mChessSize=" + mChessSize + " mChessboardWidth="
                    + mChessboardWidth + " mChessboardHeight"
                    + mChessboardHeight);
        }
    }

    /**
     * Draw game interface
     */
    public void drawGame() {
        // Lock the canvas
        Canvas canvas = mHolder.lockCanvas();
        if (mHolder == null || canvas == null) {
            Log.d(TAG, "mholde=" + mHolder + "  canvas=" + canvas);
            return;
        }
        // Clear screen:
        // Whether it is possible to use double buffer technology without clearing the screen
        canvas.drawPaint(clear);
        // Draw the chessboard, pieces and focus on the canvas
        drawChessBoard(canvas);
        drawChess(canvas);
        drawFocus(canvas);
        // Unlock the canvas and display the content of canvas on the screen.
        mHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * Add a piece of chess
     *
     * @param x X-axis
     * @param y Y-axis
     */
    public void addChess(int x, int y) {
        if (mGame == null) {
            Log.d(TAG, "game can not be null");
            return;
        }
        mGame.addChess(x, y);
        drawGame();
    }

    // Rewrite the function of onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // getAction () represents the original 32-bit information of the touch action,
        // including the specific action of the touch event and touch point information.
        int action = event.getAction();
        // Use getX () and getY () to get the x and y coordinates of the touch point.
        float x = event.getX();
        float y = event.getY();
        // Judge different events and respond differently:
        switch (action) {
            // When finger touches the screen for the first time
            // Display a focus frame as a prompt to the user
            case MotionEvent.ACTION_DOWN:
                focus.x = (int) (x / mChessSize);
                focus.y = (int) (y / mChessSize);
                // Draw the focus frame on the chessboard on the position of the finger focus.
                isDrawFocus = true;
                // Redraw the game canvas
                drawGame();
                break;
            // When the user's finger slides, no action is taken.
            case MotionEvent.ACTION_MOVE:
                break;
            // When the finger leaves, draw the pieces.
            case MotionEvent.ACTION_UP:
                isDrawFocus = false;
                // Get the position of the piece to be drawn
                int newx = (int) (x / mChessSize);
                int newy = (int) (y / mChessSize);
                // First determine whether this position can draw pieces
                if (canAdd(newx, newy, focus)) {
                    addChess(focus.x, focus.y);
                } else {// If it cannot be drawn, call drawGame () to redraw the game
                    drawGame();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Determine whether the chess piece position is too far from
     * the position where the finger touches
     *
     * @param x x position
     * @param y y position
     * @return
     */
    private boolean canAdd(float x, float y, Coordinate focus) {
        return x < focus.x + 3 && x > focus.x - 3
                && y < focus.y + 3 && y > focus.y - 3;
    }

    /**
     * Create chess pieces
     *
     * @param width  VIEW width
     * @param height VIEW height
     * @param type   chess type——white or black
     * @return Bitmap
     */
    private Bitmap createChess(int width, int height, int type) {
        // Set chess piece size in view/columns
        int tileSize = width / 15;
        // Create new bitmap based on parameters
        Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // Set different chess pictures according to different situations
        Drawable d = null;
        if (type == 0) { // Black chess put for a while
            d = getResources().getDrawable(R.drawable.black);
        } else if (type == 1) { // White chess put for a while
            d = getResources().getDrawable(R.drawable.white);
        } else if (type == 2) { // Black chess just put
            d = getResources().getDrawable(R.drawable.black_new);
        } else if (type == 3) {// White chess just put
            d = getResources().getDrawable(R.drawable.white_new);
        } else if (type == 4) { // Focus frame
            d = getResources().getDrawable(R.drawable.focus);
        }
        // The position of rectangular area where drawable is drawn in the canvas.
        // setBounds(int left, int top, int right, int bottom)
        d.setBounds(0, 0, tileSize, tileSize);
        d.draw(canvas);
        return bitmap;
    }

    // Judge whether can paint chessboard background
    private void drawChessBoard() {
        Canvas canvas = mHolder.lockCanvas();
        if (mHolder == null || canvas == null) {
            return;
        }
        drawChessBoard(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    // Painted chessboard background actually
    private void drawChessBoard(Canvas canvas) {
        // Draw anchor(Center point)
        int startX = mChessSize / 2;
        int startY = mChessSize / 2;
        int endX = startX + (mChessSize * (mChessboardWidth - 1));
        int endY = startY + (mChessSize * (mChessboardHeight - 1));
        // draw vertical line
        for (int i = 0; i < mChessboardWidth; ++i) {
            canvas.drawLine(startX + (i * mChessSize), startY, startX + (i * mChessSize), endY, boardPaint);
        }
        // draw horizontal line
        for (int i = 0; i < mChessboardHeight; ++i) {
            canvas.drawLine(startX, startY + (i * mChessSize), endX, startY + (i * mChessSize), boardPaint);
        }
        // Draw anchor(center points)
        int circleX = startX + mChessSize * (mChessboardWidth / 2);
        int circleY = startY + mChessSize * (mChessboardHeight / 2);
        ;
        canvas.drawCircle(circleX, circleY, anchorWidth, boardPaint);
    }

    // draw chess
    private void drawChess(Canvas canvas) {
        int[][] chessMap = mGame.getChessMap();
        for (int x = 0; x < chessMap.length; ++x) {
            for (int y = 0; y < chessMap[0].length; ++y) {
                int type = chessMap[x][y];
                if (type == Game.BLACK) {
                    canvas.drawBitmap(mBlack, x * mChessSize, y * mChessSize, chessPaint);
                } else if (type == Game.WHITE) {
                    canvas.drawBitmap(mWhite, x * mChessSize, y * mChessSize, chessPaint);
                }
            }
        }
        // Draw the latest chess piece
        if (mGame.getActions() != null && mGame.getActions().size() > 0) {
            // Get the position of the last touch screen action
            // and store it in the coordinate system variable last
            Coordinate last = mGame.getActions().getLast();
            // Get the history of the chessboard corresponding to this position
            int lastType = chessMap[last.x][last.y];
            // Determine the type of the new piece,
            // and draw the picture of the new piece at the corresponding position
            if (lastType == Game.BLACK) {
                canvas.drawBitmap(mBlackNew, last.x * mChessSize, last.y * mChessSize, chessPaint);
            } else if (lastType == Game.WHITE) {
                canvas.drawBitmap(mWhiteNew, last.x * mChessSize, last.y * mChessSize, chessPaint);
            }
        }
    }

    /**
     * Draw the current frame
     *
     * @param canvas
     */

    // Draw the focus frame
    private void drawFocus(Canvas canvas) {
        if (isDrawFocus) {
            canvas.drawBitmap(bFocus, focus.x * mChessSize, focus.y * mChessSize, chessPaint);
        }
    }

    // Call and redraw the chessboard when the surface size changes:
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mBlack != null) {
            mBlack.recycle();
        }
        if (mWhite != null) {
            mWhite.recycle();
        }
        mWhite = createChess(width, height, 1);
        mBlack = createChess(width, height, 0);
        mBlackNew = createChess(width, height, 2);
        mWhiteNew = createChess(width, height, 3);
        bFocus = createChess(width, height, 4);
    }

    // Called when the surface is created to start the drawing thread.
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Initialize the board
        drawChessBoard();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }

}
