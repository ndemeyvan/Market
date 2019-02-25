package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {
        private Button gotoLogin;
        private Button gotoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_choice );
        gotoLogin=findViewById ( R.id.gotoLogin );
        gotoRegister=findViewById ( R.id.gotoRegister );
        login ();
        register ();
    }
    public void login(){
        gotoLogin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoLogin=new Intent ( ChoiceActivity.this,LoginActivity.class );
                startActivity ( gotoLogin );
                finish ();
            }
        } );
    }
    public void register(){
        gotoRegister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoRegister=new Intent ( ChoiceActivity.this,RegisterActivity.class );
                startActivity ( gotoRegister );
                finish ();
            }
        } );
    }
}
