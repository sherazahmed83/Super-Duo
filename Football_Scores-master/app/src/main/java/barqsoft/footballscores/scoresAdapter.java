package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class scoresAdapter extends CursorAdapter
{
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public static double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    public scoresAdapter(Context context,Cursor cursor,int flags)
    {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        final String TEAM_PREFIX = "Team";
        final String MATCH_TIME_PREFIX = "Match Time is";
        final String MATCH_SCORE_PREFIX = "Match score is";
        final String MATCH_GOALS_STR = "goals";
        final String TEAM_IMAGE_POSTFIX = "'s image";
        final String LEAGUE_PREFIX = "The league name is ";
        final String SHARE_MATCH_SCORE = "Share match score";

        final ViewHolder mHolder = (ViewHolder) view.getTag();
        String homeTeamName = cursor.getString(COL_HOME);
        String awayTeamName = cursor.getString(COL_AWAY);
        String matchTime = cursor.getString(COL_MATCHTIME);
        int homeTeamGoals = cursor.getInt(COL_HOME_GOALS);
        int awayTeamGoals = cursor.getInt(COL_AWAY_GOALS);
        double matchId = cursor.getDouble(COL_ID);
        int matchDay = cursor.getInt(COL_MATCHDAY);
        int leagueId = cursor.getInt(COL_LEAGUE);


        mHolder.home_name.setText(homeTeamName);
        mHolder.home_name.setContentDescription(TEAM_PREFIX + " " + homeTeamName);

        mHolder.away_name.setText(awayTeamName);
        mHolder.away_name.setContentDescription(TEAM_PREFIX + " " + awayTeamName);

        mHolder.date.setText(matchTime);
        mHolder.date.setContentDescription(MATCH_TIME_PREFIX + " " + matchTime);

        mHolder.score.setText(Utilies.getScores(homeTeamGoals, awayTeamGoals));

        mHolder.score.setContentDescription(MATCH_SCORE_PREFIX + " " + homeTeamGoals + " " + MATCH_GOALS_STR + " to " + awayTeamGoals + " " + MATCH_GOALS_STR);

        mHolder.match_id = matchId;

        mHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(homeTeamName));
        mHolder.home_crest.setContentDescription(homeTeamName + TEAM_IMAGE_POSTFIX);

        mHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(awayTeamName));
        mHolder.away_crest.setContentDescription(awayTeamName + TEAM_IMAGE_POSTFIX);

        //Log.v(FetchScoreTask.LOG_TAG,mHolder.home_name.getText() + " Vs. " + mHolder.away_name.getText() +" id " + String.valueOf(mHolder.match_id));
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(detail_match_id));
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if(mHolder.match_id == detail_match_id)
        {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utilies.getMatchDay(matchDay, leagueId));
            match_day.setContentDescription(Utilies.getMatchDay(matchDay, leagueId));

            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utilies.getLeague(leagueId));
            league.setContentDescription(LEAGUE_PREFIX + Utilies.getLeague(leagueId));

            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText()+" "
                    +mHolder.score.getText()+" "+mHolder.away_name.getText() + " "));
                }
            });
            share_button.setContentDescription(SHARE_MATCH_SCORE);
        }
        else
        {
            container.removeAllViews();
        }

    }
    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT + Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}
