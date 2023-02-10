package com.example.contactwave;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.contactwave.DB.DataBase;
import com.example.contactwave.databinding.ActivityMainBinding;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, Adapter.onClick {
    ActivityMainBinding bind = null;
    ListView listView = null;
    Adapter listAdapter = null;
   SearchView searchView = null;
    DataBase mDatabase = null;
    List<Pessoa> contacts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        mDatabase = DataBase.getDatabase(this);
        handleBtn();
        contacts = getList();
        listAdapter =  new Adapter(this,contacts,this);
        listView = findViewById(R.id.my_listview);
        listView.setAdapter(listAdapter);
        searchContact();
    }

    @Override
    public void clickOnCall(Pessoa pessoa, View view) {
    call(pessoa.phone);
    }

    @Override
    public void clickOnMenu(Pessoa pessoa, View view) {
      menu(pessoa,view);
    }

    @Override
    public void emptyList(boolean isEmpty) {
        if(isEmpty) bind.listEmpty.setVisibility(View.VISIBLE);
        else bind.listEmpty.setVisibility(View.INVISIBLE);

    }


    void handleBtn()
    {
        bind.btnAddContact.setOnClickListener(this);
       searchView = findViewById(R.id.searchview);
    }

    void newContact()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_contact);
        dialog.setCancelable(true);
        dialog.setTitle("Novo Contato");
        EditText name = dialog.findViewById(R.id.editText_Name);
        EditText phone =dialog.findViewById(R.id.editText_Phone);
        Button btn = dialog.findViewById(R.id.btn_save_newContact);

        btn.setOnClickListener(click->{
            switch (validadeNewContact(name,phone))
            {
                case 1:  name.setError("Informe o nome do contado");
                    break;
                case 2: phone.setError("Informe o telefone do contato");
                    break;
                case 3: saveNewContact(name.getText().toString(),phone.getText().toString());
                    dialog.cancel();
                    attview();
                    break;
            }
        });
        dialog.show();
}

void attview()
{
    contacts.clear();
    contacts.addAll(getList());
    listAdapter.update();
}


void menu(Pessoa pessoa,View view)
{
    PopupMenu popupMenu = new PopupMenu(this,view);

    popupMenu.getMenu().add(Menu.NONE,0,0,"Chamar");
    popupMenu.getMenu().add(Menu.NONE,1,1,"Editar");
    popupMenu.getMenu().add(Menu.NONE,2,2,"Deletar");

    popupMenu.setOnMenuItemClickListener(onMenuItemSelected->{
        int idM = onMenuItemSelected.getItemId();

        switch (idM){
            case 0: call(pessoa.phone);
                break;
            case 1: attContact(pessoa);
                break;
            case 2: mDatabase.userDAO().deleteContact(pessoa);
                     attview();
                break;
        }

        return false;
    });

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) popupMenu.setGravity(Gravity.END|Gravity.TOP);
    popupMenu.show();

}


    private void saveNewContact(String name, String phone) {
        try {
            mDatabase.userDAO().insertContact(new Pessoa(capitalize(name),phone.trim()));
            Toast.makeText(this, "Salvo", Toast.LENGTH_SHORT).show();

        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateContact(int id,String name, String phone) {

        try {
            mDatabase.userDAO().updateContact(new Pessoa(id,capitalize(name),phone.trim()));
            Toast.makeText(this, "Atualizado", Toast.LENGTH_SHORT).show();

        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    List<Pessoa> getList()
    {
        return mDatabase.userDAO().getAll();
    }


    String capitalize(String str)
    {
        return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase().trim();
    }

    int validadeNewContact(EditText name, EditText phone)
{
    return (name.getText().toString().isEmpty())?1 : (phone.getText().toString().isEmpty())? 2 : 3;
}

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.btn_add_contact: newContact();
            break;
        }
    }

    void call(String phone)
    {
        Uri uri = Uri.parse("tel:"+phone);
        Intent intent = new Intent(Intent.ACTION_CALL,uri);
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);

        }
        startActivity(intent);
    }


    void attContact(Pessoa pessoa)
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_contact);
        dialog.setCancelable(true);
        dialog.setTitle("Editar Contato");
        EditText name = dialog.findViewById(R.id.editText_Name);
        EditText phone =dialog.findViewById(R.id.editText_Phone);
        Button btn = dialog.findViewById(R.id.btn_save_newContact);
        btn.setText("Editar");
        name.setText(pessoa.name);
        phone.setText(pessoa.phone);


        btn.setOnClickListener(click->{
            switch (validadeNewContact(name,phone))
            {
                case 1:  name.setError("Informe o nome do contado");
                    break;
                case 2: phone.setError("Informe o telefone do contato");
                    break;
                case 3: updateContact(pessoa.id,name.getText().toString(),phone.getText().toString());
                    dialog.cancel();
                    attview();
                    break;

            }
        });


        dialog.show();

    }

    private void searchContact(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contacts.clear();
                if(!s.isEmpty()){
                        for(Pessoa p : getList())
                        {
                            if(p.name.toLowerCase().contains(s.toLowerCase()) || p.phone.contains(s))
                            {
                                contacts.add(p);
                            }
                        }
                             listAdapter.update();
                        }
                else{
                   attview();

                }
                return false;
            }
        });
    }




}