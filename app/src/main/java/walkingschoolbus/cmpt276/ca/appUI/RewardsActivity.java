package walkingschoolbus.cmpt276.ca.appUI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import walkingschoolbus.cmpt276.ca.dataObjects.Achievement;
import walkingschoolbus.cmpt276.ca.dataObjects.User;
import walkingschoolbus.cmpt276.ca.walkingschoolbus.R;

/**
 * rewards page
 */
public class RewardsActivity extends AppCompatActivity {

    int distanceWalked;
    User userManager = User.getInstance();

    ListView rewardsListView;

    String[] milestone = {"5","15","10","20","50","75","100","125","150","200","500"};
    Integer[] milestoneStickers = {R.drawable.walk5,R.drawable.walk15,R.drawable.walk10,R.drawable.walk20,
            R.drawable.walk50,R.drawable.walk75,R.drawable.walk100,R.drawable.walk125,R.drawable.walk150,R.drawable.walk200,R.drawable.walk500};
    Integer[] notAchievedSticker = {R.drawable.notwalk5km,R.drawable.notwalk10,R.drawable.notwalk15,R.drawable.notwalk20,
            R.drawable.notwalk50,R.drawable.notwalk75,R.drawable.notwalk100,R.drawable.notwalk125,R.drawable.notwalk150,R.drawable.notwalk200,R.drawable.notwalk500,};

    Achievement[] achievements = new Achievement[milestone.length];

    String[] milestoneCompleted = new String[milestone.length];
    Integer[] achievedStickers = new Integer[milestone.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        setTotalDistanceWalked();
        setRewardsList();
        createRewardsList();
        rewardsListView = (ListView) findViewById(R.id.RewardsActivity_listView);

        CustomListView customListView = new CustomListView(this, milestoneCompleted, achievedStickers);
        rewardsListView.setAdapter(customListView);

    }

    private void setTotalDistanceWalked() {
        distanceWalked = userManager.getTotalPointsEarned();
    }

    private void setRewardsList() {
        for(int i = 0; i < milestone.length; i++) {
            Achievement newAchievement;
            if(distanceWalked >= Integer.parseInt(milestone[i])) {
                newAchievement = new Achievement(milestone[i], milestoneStickers[i],"UNLOCKED");

                System.out.println(""+ milestone.length + " | " + milestoneStickers.length);
                achievements[i] = newAchievement;
            }
            else {
                newAchievement = new Achievement(milestone[i], notAchievedSticker[i], "LOCKED");
                achievements[i] = newAchievement;
            }
        }
    }

    private void createRewardsList() {
        for(int i = 0; i < achievements.length; i++) {
            milestoneCompleted[i] = achievements[i].getAchievementState();
            achievedStickers[i] = achievements[i].getAchievementSticker();
        }
    }

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, RewardsActivity.class);
        return intent;
    }
}
