package com.makgyber.villagebuys.ui.gallery;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makgyber.villagebuys.Home;
import com.makgyber.villagebuys.R;
import com.makgyber.villagebuys.Tindahan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private final static String COLLECTION = "tindahan";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    CollectionReference dbRef = db.collection(COLLECTION);

    TextView mWelcome, mNameHelp, mAddressHelp, mContactInfoHelp, mServiceAreaHelp;
    EditText mTindahanName, mTindahanId, mOwner, mAddress, mContactInfo, mServiceArea;
    Button register;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);


        mTindahanName = (EditText) root.findViewById(R.id.txt_tindahan_name);
        mAddress = (EditText) root.findViewById(R.id.txt_address);
        mContactInfo = (EditText) root.findViewById(R.id.txt_contact_info);
        mServiceArea = (EditText) root.findViewById(R.id.txt_service_area);

        generateLabels(root);
        populateTindahanView();
        tindahanRegistration(root);
        return root;
    }

    private void generateLabels(View root) {
        mWelcome = (TextView) root.findViewById(R.id.txt_tindahan_welcome);
        mNameHelp = (TextView) root.findViewById(R.id.txt_tindahan_name_help);
        mAddressHelp = (TextView) root.findViewById(R.id.txt_address_help);
        mContactInfoHelp = (TextView) root.findViewById(R.id.txt_contact_info_help);
        mServiceAreaHelp = (TextView) root.findViewById(R.id.txt_service_area_help);

        mWelcome.setText(Html.fromHtml(mWelcome.getText().toString()));
        mNameHelp.setText(Html.fromHtml(mNameHelp.getText().toString()));
        mAddressHelp.setText(Html.fromHtml(mAddressHelp.getText().toString()));
        mContactInfoHelp.setText(Html.fromHtml(mContactInfoHelp.getText().toString()));
        mServiceAreaHelp.setText(Html.fromHtml(mServiceAreaHelp.getText().toString()));
    }


    private void tindahanRegistration(View root) {
        register = (Button) root.findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tindahanName = mTindahanName.getText().toString();
                String address = mAddress.getText().toString();
                String contactInfo = mContactInfo.getText().toString();
                String serviceArea = mServiceArea.getText().toString();
                String owner = mUser.getUid();
                String tindahanId = COLLECTION + owner;

                Tindahan tindahan = new Tindahan(tindahanId, tindahanName, owner, contactInfo, address,
                        true, new ArrayList<String>(Arrays.asList(serviceArea.split(","))));

                dbRef.document(tindahanId).set(tindahan)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Tindahan saved", Toast.LENGTH_SHORT).show();
                                addProducts();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Tindahan saved", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void populateTindahanView() {

        DocumentReference docRef = db.collection(COLLECTION).document(COLLECTION + mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mTindahanName.setText(document.get("tindahanName").toString());
                        mAddress.setText(document.get("address").toString());
                        mContactInfo.setText(document.get("contactInfo").toString());
                        String sArea = document.get("serviceArea").toString();
                        mServiceArea.setText(sArea.replace("[", "").replace("]",""));
                    }
                }
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void addProducts() {
        CollectionReference product = db.collection("product");
        String tindahanId = "tindahan" + mAuth.getCurrentUser().getUid();
        String tindahanName = mTindahanName.getText().toString();

        Map<String, Object> data1 = new HashMap<>();
        data1.put("productName", "Bananas");
        data1.put("description", "Imported from DOLE Mindanao");
        data1.put("price", 200);
        data1.put("tindahanName", tindahanName);
        data1.put("tindahanId", tindahanId);
        data1.put("publish", true);
        data1.put("tags", Arrays.asList("fruits", "bananas", "fresh produce", "saging"));
        data1.put("serviceArea", Arrays.asList("Las Pinas", "Pasay", "Paranaque"));
        product.document().set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("productName", "Apples");
        data2.put("description", "1 dozen red washington apples");
        data2.put("price", 590);
        data2.put("tindahanName", tindahanName);
        data2.put("tindahanId", tindahanId);
        data2.put("publish", true);
        data2.put("tags", Arrays.asList("fruits", "apples", "fresh produce", "mansanas"));
        data2.put("serviceArea", Arrays.asList("Las Pinas", "Pasay", "Paranaque"));
        product.document().set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("productName", "Face Masks");
        data3.put("description", "Box of 50. Imported from China");
        data3.put("price", 840);
        data3.put("tindahanName", tindahanName);
        data3.put("tindahanId", tindahanId);
        data3.put("publish", true);
        data3.put("tags", Arrays.asList("safety", "medical supplies", "face masks", "facemask"));
        data3.put("serviceArea", Arrays.asList("Las Pinas", "Pasay", "Paranaque"));
        product.document().set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("productName", "Lysol Spray");
        data4.put("description", "100g Lysol brand disinfectant spray");
        data4.put("price", 100);
        data4.put("tindahanName", tindahanName);
        data4.put("tindahanId", tindahanId);
        data4.put("publish", true);
        data4.put("tags", Arrays.asList("safety", "medical supplies", "disinfectant", "spray", "lysol"));
        data4.put("serviceArea", Arrays.asList("Las Pinas", "Pasay", "Paranaque"));
        product.document().set(data4);

        Map<String, Object> data6 = new HashMap<>();
        data6.put("productName", "Green Cross Alcohol");
        data6.put("description", "70% Isopropyl Alcohol 200ml green bottle");
        data6.put("price", 40);
        data6.put("tindahanName", tindahanName);
        data6.put("tindahanId", tindahanId);
        data6.put("publish", true);
        data6.put("tags", Arrays.asList("safety", "medical supplies", "disinfectant", "alcohol", "isopropyl"));
        data6.put("serviceArea", Arrays.asList("Las Pinas", "Pasay", "Paranaque"));
        product.document().set(data6);
    }
}
