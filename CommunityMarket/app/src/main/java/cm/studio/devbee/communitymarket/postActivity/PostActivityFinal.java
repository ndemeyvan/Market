package cm.studio.devbee.communitymarket.postActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;

public class PostActivityFinal extends AppCompatActivity {
    private Toolbar postfinaltoolbar;
    private EditText nomProduit;
    private EditText descriptionProduit;
    private EditText prixPorduit;
    private ImageView imageProduit;
    private String categoryName ,nom_du_produit,decription_du_produit,prix_du_produit,saveCurrentTime,saveCurrentDate;
    private Button vendreButton;
    private ProgressBar progressBar_post;
    private Uri mImageUri;
    private String randomKey;
    private String current_user_id;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post_final );
        firebaseAuth=FirebaseAuth.getInstance ();
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        current_user_id=firebaseAuth.getCurrentUser ().getUid ();
        postfinaltoolbar=findViewById ( R.id.final_toolbar );
        imageProduit=findViewById ( R.id.imageProduit );
        nomProduit=findViewById ( R.id.post_product_name );
        descriptionProduit=findViewById ( R.id.post_product_description );
        prixPorduit=findViewById ( R.id.post_production_prix );
        vendreButton=findViewById ( R.id.post_button );
        setSupportActionBar ( postfinaltoolbar );
        progressBar_post=findViewById ( R.id.progressBar_post );
        categoryName=getIntent ().getExtras ().get ( "categoryName" ).toString ();
        Toast.makeText ( PostActivityFinal.this,categoryName,Toast.LENGTH_LONG ).show ();
        setimage ();
        prendreDonner ();
       /* ActionBar ab=getSupportActionBar ();
        ab.setDisplayHomeAsUpEnabled ( true );*/

    }
    public void setimage(){
        imageProduit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);

                    }catch (Exception e){
                        e.printStackTrace ();
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(PostActivityFinal.this);
                }
            }
        } );

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                imageProduit.setImageURI ( mImageUri );
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void prendreDonner(){
        vendreButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                progressBar_post.setVisibility ( View.VISIBLE );
                prendreDonnerDevente ();
            }
        } );
    }
    public void prendreDonnerDevente(){
        nom_du_produit=nomProduit.getText ().toString ();
        decription_du_produit=descriptionProduit.getText ().toString ();
        prix_du_produit=prixPorduit.getText ().toString ();
        if (!TextUtils.isEmpty ( nom_du_produit )&&!TextUtils.isEmpty ( decription_du_produit )&&!TextUtils.isEmpty ( prix_du_produit )&&mImageUri!=null){
            stocker();
        }else{
            Toast.makeText ( PostActivityFinal.this,"Veuillez remplir tous les champs",Toast.LENGTH_LONG ).show ();
        }
    }
    public void stocker(){
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" MMM dd,yyyy" );
        saveCurrentDate=currentDate.format ( calendar.getTime () );
        /*SimpleDateFormat curntTime=new SimpleDateFormat (" HH:mm" );
        saveCurrentTime=curntTime.format ( calendar.getTime () );*/
        randomKey=saveCurrentDate;
        final StorageReference image_product_post=storageReference.child ( "image_des_produits" ).child ( current_user_id+".jpg" );
        UploadTask uploadTask =image_product_post.putFile ( mImageUri );
        Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return image_product_post.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri> () {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    progressBar_post.setVisibility (View.INVISIBLE);
                    Map <String,Object> user_post = new HashMap ();
                    user_post.put ( "nom_du_produit",nom_du_produit );
                    user_post.put ( "decription_du_produit",decription_du_produit );
                    user_post.put ( "prix_du_produit",prix_du_produit );
                    user_post.put ( "date_de_publication",randomKey );
                    user_post.put ( "utilisateur",current_user_id );
                    user_post.put ( "image_du_produit",downloadUri.toString() );
                    firebaseFirestore.collection ( categoryName ).add(user_post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){

                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(PostActivityFinal.this,error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    firebaseFirestore.collection ( "nouveau_produits" ).add(user_post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){

                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(PostActivityFinal.this,error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    firebaseFirestore.collection ( current_user_id ).add(user_post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Intent gotoRecherche=new Intent(PostActivityFinal.this,Accueil.class);
                                startActivity(gotoRecherche);
                                finish();
                                Toast.makeText(PostActivityFinal.this,"envoie effectuer",Toast.LENGTH_LONG).show();
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(PostActivityFinal.this,error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {

                }
            }
        });
    }
}
