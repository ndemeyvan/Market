package cm.studio.devbee.communitymarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Accueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private CircleImageView acceuille_image;
    private TextView drawer_user_name;
    private TextView content_welcome_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_accueil );
        Toolbar toolbar =findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        NavigationView navigationView =findViewById ( R.id.nav_view );
        mAuth=FirebaseAuth.getInstance ();
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        acceuille_image=navigationView.getHeaderView(0).findViewById(R.id.acceuille_image);
        drawer_user_name=navigationView.getHeaderView(0).findViewById(R.id.drawer_user_name);
        content_welcome_user=findViewById(R.id.content_welcome_user);
        //acceuille_image.setImageDrawable(R.drawable.use);
        DrawerLayout drawer =findViewById ( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ();

        navigationView.setNavigationItemSelectedListener ( this );
        recup();
    }
    public void recup(){
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String image_profil_user =task.getResult ().getString ("user_profil_image");
                        String nom_user = task.getResult ().getString ("user_name");
                        String prenomuser =task.getResult ().getString ("user_prenom");
                        drawer_user_name.setText ( nom_user + " " + prenomuser);
                        content_welcome_user.setText(nom_user + " " + prenomuser);
                        Log.d("cle",image_profil_user);
                        Picasso.with ( Accueil.this ).load ( image_profil_user ).placeholder(R.drawable.use).into ( acceuille_image );
                    }
                }else{


                }
            }
        } );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
        if (drawer.isDrawerOpen ( GravityCompat.START )) {
            drawer.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.accueil, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == R.id.setting) {
            Intent parametre=new Intent(Accueil.this,ParametrePorfilActivity.class);
            startActivity(parametre);
            return true;
        }

        return super.onOptionsItemSelected ( item );
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId ();

        if (id == R.id.ic_user) {
            Intent intent = new Intent ( Accueil.this,ProfileActivity.class );
            startActivity ( intent );
        } else if (id == R.id.ic_logout) {
            Intent intenttwo = new Intent ( Accueil.this,LoginActivity.class );
            startActivity ( intenttwo );
            finish ();
        }else if (id == R.id.setting) {
            Intent parametre=new Intent(Accueil.this,ParametrePorfilActivity.class);
            startActivity(parametre);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
        drawer.closeDrawer ( GravityCompat.START );
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        recup();
    }
}
