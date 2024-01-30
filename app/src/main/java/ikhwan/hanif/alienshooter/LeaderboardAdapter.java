package ikhwan.hanif.alienshooter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LeaderboardAdapter extends ArrayAdapter<LeaderboardEntry> {

    public LeaderboardAdapter(Context context, List<LeaderboardEntry> leaderboardEntries) {
        super(context, 0, leaderboardEntries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LeaderboardEntry entry = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.leaderboard_item, parent, false);
        }

        TextView rankTextView = convertView.findViewById(R.id.rankTextView);
        TextView userIdTextView = convertView.findViewById(R.id.userIdTextView);
        TextView scoreTextView = convertView.findViewById(R.id.scoreTextView);

        rankTextView.setText(String.valueOf(position + 1));
        userIdTextView.setText(entry.getUserId());
        scoreTextView.setText(String.valueOf(entry.getScore()));

        return convertView;
    }
}

