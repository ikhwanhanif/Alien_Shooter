package ikhwan.hanif.alienshooter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private DatabaseReference leaderboardRef;
    private ListView listView;

    ImageView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        info = findViewById(R.id.infoL);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog();
            }
        });

        leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard");
        listView = findViewById(R.id.listView);




// Memanggil metode untuk menampilkan leaderboard
        displayLeaderboard();



    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Info");
        builder.setMessage("Leaderboard akan menampilkan Skor user dari tertinggi sampai terendah, Untuk membuat permainan semakin menantang Skor yang akan ditampilkan di Leaderboard adalah skor yang baru saja di dapatkan, bukan HIGHSCORE.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayLeaderboard() {
        leaderboardRef.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    String userId = entrySnapshot.getKey();

                    // Periksa apakah nilai skor berupa HashMap
                    Object scoreObject = entrySnapshot.getValue();
                    if (scoreObject instanceof Long) {
                        long score = (long) scoreObject;

                        // Hapus bagian "@gmail.com" dari userId
                        String displayedUserId = userId.replace("@gmail,com", "");

                        leaderboardEntries.add(new LeaderboardEntry(displayedUserId, score));
                    }
                }

                Collections.sort(leaderboardEntries, Collections.reverseOrder());

                LeaderboardAdapter adapter = new LeaderboardAdapter(LeaderboardActivity.this, leaderboardEntries);
                listView.setAdapter(adapter);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


    private String getUsernameFromUserId(String userId) {
        // Mendapatkan username dari ID pengguna (menghapus "@gmail.com")
        int atIndex = userId.indexOf("@");
        if (atIndex != -1) {
            return userId.substring(0, atIndex);
        } else {
            return userId;
        }
    }

}
