package cm.studio.devbee.communitymarket.postActivity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cm.studio.devbee.communitymarket.R;

public class PostActivityFinal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post_final );
       /* frameLayout=findViewById ( R.id.matiere_framelayout_container );
        ActionBar ab=getSupportActionBar ();
        ab.setDisplayHomeAsUpEnabled ( true );
        title =getIntent ().getStringExtra ( "categoryName" );
        getSupportActionBar ().setTitle ( title );
        setMatiereTroisieme ( title );*/
    }
}
