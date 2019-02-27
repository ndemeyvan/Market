package cm.studio.devbee.communitymarket.utilsForNouveautes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapteNouveaux extends RecyclerView.Adapter<CategoriesAdapteNouveaux.ViewHolder> {
    List<CategoriesModelNouveaux> categoriesModelNouveauxList;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public CategoriesAdapteNouveaux(List<CategoriesModelNouveaux> categoriesModelNouveauxList, Context context) {
        this.categoriesModelNouveauxList = categoriesModelNouveauxList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_nouveautes ,viewGroup,false);
        viewGroup.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //viewHolder.categories_text.setText ( categoriesModelList.get ( i ).getPost_titre_categories () );
        String desc =categoriesModelNouveauxList.get ( i).getDecription_du_produit();
        String nvxPrix=categoriesModelNouveauxList.get(i).getPrix_du_produit();
        String nvxtemps=categoriesModelNouveauxList.get(i).getDate_de_publication();
        String nvxlike=categoriesModelNouveauxList.get(i).getLike();
        String nvxUser=categoriesModelNouveauxList.get(i).getUsername();
       // viewHolder.image_categories.setImageResource ( categoriesModelNouveauxList.get ( i ).getPost_image_categories () );
        viewHolder.setNom ( desc );
        viewHolder.setPrix(nvxPrix);
        viewHolder.temps(nvxtemps);
        viewHolder.user_id(nvxUser);
        firebaseFirestore.collection("mes donnees utilisateur").document(nvxUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){


                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModelNouveauxList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        TextView prix;
        TextView tempsDePublication;
        TextView liker;
        ImageView imageDuproduit;
        TextView user_name;
        //TextView user_name;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            description=itemView.findViewById ( R.id.nouveaux_description_du_produit );
            prix=itemView.findViewById(R.id.nouveaux_prix);
            tempsDePublication=itemView.findViewById(R.id.nouveaux_temps );
            liker=itemView.findViewById(R.id.nouveaux_nombre_de_like);
            imageDuproduit=itemView.findViewById(R.id.nouveaute_image);
            user_name=itemView.findViewById(R.id.nouveaux_user_name);
            //user_image=itemView.findViewById ( R.id.nouveaux_image_profile );
        }
        public void setNom(final String name){
            description.setText(name);
            /*itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) { Intent categoryIntent=new Intent ( itemView.getContext (),PostActivityFinal.class );
                   categoryIntent.putExtra ( "categoryName",name );
                   itemView.getContext ().startActivity ( categoryIntent );
                }
            } );*/
        }
        public void setPrix(String nouveauxPrix){
            prix.setText(nouveauxPrix);
        }
        public void temps(String postTemps){
            tempsDePublication.setText(postTemps);
        }
        public void likexa(String lelike){
            liker.setText(lelike);
        }
        public void imageproduitxi(ImageView image){
            //Picasso.with(context).load(image)
        }
        public void user_id(String use){
            user_name.setText(use);
        }

    }
}
