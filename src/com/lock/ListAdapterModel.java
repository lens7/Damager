package com.lock;

import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapterModel extends ArrayAdapter<String>{
int groupid;
String[] names;
Context context;
String path;
public ListAdapterModel(Context context, int vg, int id, String[] names, String parentPath){
super(context,vg, id, names);
this.context=context;
groupid=vg;
this.names=names;
this.path=parentPath;
}
public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(groupid, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.icon);
        TextView textView = (TextView) itemView.findViewById(R.id.label);
        String item=names[position];
        textView.setText(item);
        File lockedfile=new File(context.getFilesDir(),item);
        if(lockedfile.exists()){
        //set the locked icon to the file that was already locked.
        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.locked_icon));
        }
        else{//set the directory and file icon to the unlocked file
        File f=new File(path+"/"+item);
        if(f.isDirectory())
        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.diricon));
        else
        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.fileicon));
        }
        return itemView;
}

}

