package ikhwan.hanif.alienshooter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard");
        listView = findViewById(R.id.listView);




// Memanggil metode untuk menampilkan leaderboard
        displayLeaderboard();



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
