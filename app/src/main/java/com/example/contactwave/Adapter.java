package com.example.contactwave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

public class Adapter extends BaseAdapter{
    protected Context context = null;
    protected List<Pessoa> usuarios;
    onClick click;

public Adapter(Context context, List<Pessoa> usuarios, onClick click)
{
    super();
    this.context = context;
    this.usuarios = usuarios;
    this.click = click;
}


    public interface onClick
{
void clickOnCall(Pessoa pessoa, View view);
void clickOnMenu(Pessoa pessoa, View view);
void emptyList(boolean isEmpty);

}


    @Override
    public int getCount() {
    if(usuarios.size()<1) this.click.emptyList(true);
    else this.click.emptyList(false);
    return usuarios.size() ;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        @SuppressLint("ViewHolder") View layoutInflater = LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        TextView name = layoutInflater.findViewById(R.id.text_name);
        TextView phone = layoutInflater.findViewById(R.id.text_phone);
        ImageButton btn_call = layoutInflater.findViewById(R.id.btn_call);
        name.setText(usuarios.get(i).name);
        phone.setText(formatPhone(usuarios.get(i).phone));

        btn_call.setOnClickListener(click ->this.click.clickOnCall(usuarios.get(i),layoutInflater));
       layoutInflater.setOnLongClickListener(l-> {this.click.clickOnMenu(usuarios.get(i),layoutInflater);
            return true;
        });


        return layoutInflater ;//


    }

    private String formatPhone(String phone)
    {
        String pattern ="";

        switch (phone.length())
        {
            case 9:  pattern = "#####-####";
                break;
            case 12:  pattern = "(###)#####-####";
                break;
            case 11:  pattern = "(##)#####-####";
                break;
            case 14:  pattern ="(#####)#####-####";
                break;
            default: pattern = "##############################";

        }

        return mask(pattern,phone);
    }

    String mask(String pattern, String object)
    {
        StringBuilder concat = new StringBuilder();
        char[] patternToChar = pattern.toCharArray();
        char[] objectToChar = object.toCharArray();
        int roll=0;


        for(char c : patternToChar)
        {
            if(c!='#')
            {
                concat.append(c);
            }
            else{
                concat.append(objectToChar[roll]);
                roll++;
                if(roll==objectToChar.length) break;
            }
        }

        return concat.toString();
    }

    public void update(){
        this.notifyDataSetChanged();

    }

}
