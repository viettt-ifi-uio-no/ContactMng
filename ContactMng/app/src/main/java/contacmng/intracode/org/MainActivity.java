package contacmng.intracode.org;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText nameTxt, phoneTxt, emailTxt, addrTxt;
    List<Contact> contactsList = new ArrayList<Contact>();
    ListView contactListView;
    ImageView contactImgImageView;
    Uri imgURI;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgURI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getApplicationContext().getResources().getResourcePackageName(R.drawable.sun)
                + '/' + getApplicationContext().getResources().getResourceTypeName(R.drawable.sun)
                + '/' + getApplicationContext().getResources().getResourceEntryName(R.drawable.sun));

        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addrTxt = (EditText) findViewById(R.id.txtAddr);
        contactListView = (ListView) findViewById(R.id.listView);
        contactImgImageView = (ImageView) findViewById(R.id.imgViewContactImage);

        dbHandler = new DatabaseHandler(getApplicationContext());

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        assert(tabHost != null);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        final Button btnAdd = (Button) findViewById(R.id.btnAddContact);

        assert(btnAdd != null);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = new Contact(dbHandler.getContactsCount(), nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addrTxt.getText().toString(),imgURI);
                dbHandler.createContact(contact);
                contactsList.add(contact);
                populateList();
                Toast.makeText(getApplicationContext(),"Your contact has been created", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                assert (btnAdd != null);
                btnAdd.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contactImgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select contact image"),1);
            }
        });

        List<Contact> addableContacts = dbHandler.getAllContacts();
        int contactCount = dbHandler.getContactsCount();

        for (int i  = 0; i < contactCount; i++){
            contactsList.add(addableContacts.get(i));
        }

        if(!addableContacts.isEmpty()) populateList();

    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(resCode == RESULT_OK){
            if(reqCode == 1) {
                contactImgImageView.setImageURI(data.getData());
                imgURI = data.getData();
            }
        }
    }

    private void populateList(){
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<Contact>{
        public ContactListAdapter(){
            super(MainActivity.this, R.layout.listview_items, contactsList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.listview_items,parent,false);
            }

            Contact curContact = contactsList.get(position);

            TextView name = (TextView) view.findViewById(R.id.contactName);
            name.setText(curContact.getNameUser());

            TextView phone = (TextView) view.findViewById(R.id.phonelbl);
            phone.setText(curContact.getPhone());

            TextView email = (TextView) view.findViewById(R.id.emailLbl);
            email.setText(curContact.getEmail());

            TextView addr = (TextView) view.findViewById(R.id.addresslbl);
            addr.setText(curContact.getAddress());

            ImageView ivContactImge = (ImageView) view.findViewById(R.id.ivContactImg);
            ivContactImge.setImageURI(curContact.getImgURI());

            return view;
        }
    }
}
