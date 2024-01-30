package ikhwan.hanif.alienshooter;

import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class SpaceShooter extends View {

    private static boolean isMuted = false;
    private AudioManager audioManager;
    private int originalVolume; // New variable to store the original volume

    private DatabaseReference leaderboardRef;
    private FirebaseUser currentUser;
    private String userId;

    Context context;
    Bitmap background, lifeImage;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;

    int score = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_SIZE = 80;
    boolean paused = false;
    PesawatKita ourSpaceship;
    PesawatMusuh enemySpaceship;
    Random random;
    ArrayList<Shot> enemyShots, ourShots;
    boolean enemyExplosion;
    Ledakan explosion;
    ArrayList<Ledakan> explosions;
    boolean enemyShotAction = false;
    final Runnable runnable = this::invalidate;

    public SpaceShooter(Context context) {
        super(context);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard");

        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        enemyShots = new ArrayList<>();
        ourShots = new ArrayList<>();
        explosions = new ArrayList<>();
        ourSpaceship = new PesawatKita(context);
        enemySpaceship = new PesawatMusuh(context);
        handler = new Handler();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            checkAndSaveUserId();
        }

    }
    public static void setMute(boolean mute) {
        isMuted = mute;
    }

    private void gameover() {
        // ...

        // Simpan skor pengguna ke Firebase Realtime Database
        saveScoreToLeaderboard(score);
    }

    private void saveScoreToLeaderboard(int score) {
        // Hanya simpan skor jika pengguna memiliki ID dan email
        if (userId != null && !userId.isEmpty() && currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Ganti karakter yang tidak diizinkan dalam path
            String sanitizedEmail = userEmail.replace(".", ",");

            leaderboardRef.child(sanitizedEmail).setValue(score);
        }
    }


    private void saveUserIdToSharedPreferences(String userId) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", userId);
        editor.apply();
    }

    private void checkAndSaveUserId() {
        if (userId != null && !userId.isEmpty()) {
            saveUserIdToSharedPreferences(userId);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawText(context.getString(R.string.score) + points, 0, TEXT_SIZE, scorePaint);
        for (int i = life; i >= 1; i--) {
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.getWidth() * i, 0, null);
        }
        if (life == 0) {
            if (!isMuted) {
                MediaPlayer mp = MediaPlayer.create(this.getContext(), R.raw.suara_lose);
                mp.start();
            }
            paused = true;
            handler = null;

            SharedPreferences preferences = context.getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("lastScore", score);
            editor.apply();

            gameover();

            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();

        }

        enemySpaceship.ex += enemySpaceship.enemyVelocity;
        if(enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() >= screenWidth){
            enemySpaceship.enemyVelocity *= -1;
        }
        if(enemySpaceship.ex <=0){
            enemySpaceship.enemyVelocity *= -1;
        }
        if ((!enemyShotAction) && (enemySpaceship.ex >= random.nextInt(10))){

            Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth()/2, enemySpaceship.ey + enemySpaceship.getEnemySpaceshipHeight()/2);
            enemyShots.add(enemyShot);
            enemyShotAction = true;
            MediaPlayer mPlayer = MediaPlayer.create(this.getContext(), R.raw.suara_shoot);
            mPlayer.start();
        }
        if (!enemyExplosion){
            canvas.drawBitmap(enemySpaceship.getEnemySpaceship(), enemySpaceship.ex, enemySpaceship.ey, null);
        }
        if (ourSpaceship.isAlive){
            if (ourSpaceship.ox > screenWidth - ourSpaceship.getOurSpaceshipWidth()){
                ourSpaceship.ox = screenWidth - ourSpaceship.getOurSpaceshipWidth();
            } else if (ourSpaceship.ox < 0){
                ourSpaceship.ox = 0;
            }
            canvas.drawBitmap(ourSpaceship.getOurSpaceship(), ourSpaceship.ox, ourSpaceship.oy, null);
        }
        for (int i=0; i<enemyShots.size(); i++){
            enemyShots.get(i).shy += 15;
            if (points >= 10){
                enemyShots.get(i).shy += 20;
            }
            if (points >= 20){
                enemyShots.get(i).shy += 25;
            }
            if (points >= 30){
                enemyShots.get(i).shy += 30;
            }
            if (points >= 40){
                enemyShots.get(i).shy += 25;
            }
            if (points >= 50){
                enemyShots.get(i).shy += 20;
            }
            canvas.drawBitmap(enemyShots.get(i).getShot(), enemyShots.get(i).shx, enemyShots.get(i).shy, null);
            if ((enemyShots.get(i).shx >= ourSpaceship.ox)
            && (enemyShots.get(i).shx <= ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth())
            && (enemyShots.get(i).shy >= ourSpaceship.oy)
            && (enemyShots.get(i).shy <= screenHeight)){
                life--;
                enemyShots.remove(i);
                explosion = new Ledakan(context, ourSpaceship.ox, ourSpaceship.oy);
                explosions.add(explosion);
                MediaPlayer mPlayer = MediaPlayer.create(this.getContext(), R.raw.suara_ledakan);
                mPlayer.start();
            } else if (enemyShots.get(i).shy >= screenHeight) {
                enemyShots.remove(i);
            }
            if (enemyShots.size() == 0){
                enemyShotAction = false;
            }
        }
        for (int i=0; i < ourShots.size(); i++){
            ourShots.get(i).shy -= 15;
            if (points >= 50){
                ourShots.get(i).shy -= 25;
            }
            canvas.drawBitmap(ourShots.get(i).getShot(), ourShots.get(i).shx, ourShots.get(i).shy, null);
            if ((ourShots.get(i).shx >= enemySpaceship.ex)
            && (ourShots.get(i).shx <= enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth())
            && (ourShots.get(i).shy <= enemySpaceship.getEnemySpaceshipHeight())
            && (ourShots.get(i).shy >= enemySpaceship.ey)){
                score++;
                points++;

                if (points == 10){
                    enemySpaceship.enemyVelocity = 10 + random.nextInt(15);

                }
                if (points == 30){
                    enemySpaceship.enemyVelocity = 20 + random.nextInt(25);

                }
                if (points == 60){
                    enemySpaceship.enemyVelocity = 30 + random.nextInt(35);

                }

                if (points == 10){

                    life++;
                }
                if (points == 30){

                    life++;
                }
                if (points == 60){

                    life++;
                }
                if (points == 100){

                    Intent intent = new Intent(context, SuccessActivity.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }

                ourShots.remove(i);
                explosion = new Ledakan(context, enemySpaceship.ex, enemySpaceship.ey);
                explosions.add(explosion);
                MediaPlayer mPlayer = MediaPlayer.create(this.getContext(), R.raw.suara_ledakan);
                mPlayer.start();
            } else if (ourShots.get(i).shy <= 0) {
                ourShots.remove(i);
            }
        }
        for (int i=0; i<explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame),
                    explosions.get(i).eX,
                    explosions.get(i).eY,
                    null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 8){
                explosions.remove(i);
            }
        }
        if (!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(ourShots.size() < 3){
                Shot ourShot = new Shot(context, ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth() / 2, ourSpaceship.oy);
                ourShots.add(ourShot);
                if (!isMuted) {
                    MediaPlayer mPlayer = MediaPlayer.create(this.getContext(), R.raw.suara_shoot);
                    mPlayer.start();
                }
            }
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            ourSpaceship.ox = touchX;
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            ourSpaceship.ox = touchX;
        }
        return true;
    }
}
