package ikhwan.hanif.alienshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class PesawatMusuh {
    Context context;
    Bitmap enemySpaceship;
    int ex, ey;
    int enemyVelocity;
    Random random;

    public PesawatMusuh(Context context) {
        this.context = context;
        enemySpaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.ufo);
        random = new Random();
        resetEnemySpaceship();
    }

    private void resetEnemySpaceship() {
        ex = random.nextInt(10);
        ey = 0;
        enemyVelocity = 1 + random.nextInt(5);


    }

    public Bitmap getEnemySpaceship(){
        return enemySpaceship;
    }

    int getEnemySpaceshipWidth(){
        return enemySpaceship.getWidth();
    }

    int getEnemySpaceshipHeight(){
        return enemySpaceship.getHeight();
    }
}
