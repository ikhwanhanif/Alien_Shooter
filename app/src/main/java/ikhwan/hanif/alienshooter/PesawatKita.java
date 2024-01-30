package ikhwan.hanif.alienshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class PesawatKita {
    Context context;
    Bitmap ourSpaceship;
    int ox, oy;
    boolean isAlive = true;
    int ourVelocity;
    Random random;

    public PesawatKita(Context context) {
        this.context = context;
        ourSpaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket);
        random = new Random();
        resetOurSpaceship();
    }

    private void resetOurSpaceship() {
        ox = random.nextInt(SpaceShooter.screenWidth);
        oy = SpaceShooter.screenHeight - ourSpaceship.getHeight();
        ourVelocity = 10 + random.nextInt(6);
    }

    public Bitmap getOurSpaceship(){
        return ourSpaceship;
    }

    int getOurSpaceshipWidth(){
        return ourSpaceship.getWidth();
    }
}
