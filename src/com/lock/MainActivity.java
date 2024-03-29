package com.lock;

import java.io.File;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
//import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {


   private String path="";
   private String selectedFile="";
   private Context context;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.context=this;
 
    }

    protected void onStart(){
    super.onStart();
    ListView lv=(ListView) findViewById(R.id.files_list);
if(lv!=null){
lv.setSelector(R.drawable.selection_style);
lv.setOnItemClickListener(new ClickListener());
}
path="/mnt";
listDirContents();
    }
 
    public void onBackPressed(){
    if(path.length()>1){ //up one level of directory structure
    File f=new File(path);
    path=f.getParent();
    listDirContents();
    }
    else{
    refreshThumbnails();
    System.exit(0); //exit app
   
    }
    }
 
 
    private void refreshThumbnails(){
context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
}

    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

 
    private class ClickListener implements OnItemClickListener{
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //selected item      
            ViewGroup vg=(ViewGroup)view;
      String selectedItem = ((TextView) vg.findViewById(R.id.label)).getText().toString();
            path=path+"/"+selectedItem;
            //et.setText(path);          
            listDirContents();
      }
     
     
      }
 
 
 
    private void listDirContents(){
    ListView l=(ListView) findViewById(R.id.files_list);
    if(path!=null){
    try{
    File f=new File(path);
    if(f!=null){
    if(f.isDirectory()){
    String[] contents=f.list();
    if(contents.length>0){
    //create the data source for the list
    ListAdapterModel lm=new ListAdapterModel(this,R.layout.listlayout,R.id.label,contents,path);
    //supply the data source to the list so that they are ready to display
    l.setAdapter(lm);
    }
    else
    {
    //keep track the parent directory of empty directory
    path=f.getParent();
    }
    }
    else{
    //capture the selected file path
    selectedFile=path;
    //keep track the parent directory of the selected file
    path=f.getParent();
   
    }
    }
    }catch(Exception e){}
    }    
 
   
    }
 
    public void lockFile(View view){
    EditText txtpwd=(EditText)findViewById(R.id.txt_input);
String pwd=txtpwd.getText().toString();
if(pwd.length()>0){

if(selectedFile.length()>0){
BackTaskLock btlock=new BackTaskLock();
btlock.execute(pwd,null,null);

}
else{
MessageAlert.showAlert("Please a select a file to lock",context);
}
}
else{
MessageAlert.showAlert("Please enter password",context);
}
    }
 
    public void startLock(String pwd){
    Locker locker=new Locker(context,selectedFile,pwd);
locker.lock();
    }

    public void unlockFile(View view){
    EditText txtpwd=(EditText)findViewById(R.id.txt_input);
String pwd=txtpwd.getText().toString();
if(pwd.length()>0){

if(selectedFile.length()>0){

BackTaskUnlock btunlock=new BackTaskUnlock();
btunlock.execute(pwd,null,null);
   
}
else{
MessageAlert.showAlert("Please select a file to unlock",context);
}
}
else{
MessageAlert.showAlert("Please enter password",context);
}

    }
 
    public void startUnlock(String pwd){
    Locker locker=new Locker(context,selectedFile,pwd);
locker.unlock();
    }
 
    private class BackTaskLock extends AsyncTask<String,Void,Void>{
    ProgressDialog pd;
    protected void onPreExecute(){
super.onPreExecute();
//show process dialog
pd = new ProgressDialog(context);
pd.setTitle("Locking the file");
pd.setMessage("Please wait.");
pd.setCancelable(true);
pd.setIndeterminate(true);
pd.show();


}
protected Void doInBackground(String...params){    
try{

startLock(params[0]);

}catch(Exception e){
pd.dismiss();   //close the dialog if error occurs
}
return null;

}
protected void onPostExecute(Void result){
pd.dismiss();
}


}
 
    private class BackTaskUnlock extends AsyncTask<String,Void,Void>{
    ProgressDialog pd;
    protected void onPreExecute(){
super.onPreExecute();
//show process dialog
pd = new ProgressDialog(context);
pd.setTitle("UnLocking the file");
pd.setMessage("Please wait.");
pd.setCancelable(true);
pd.setIndeterminate(true);
pd.show();


}
protected Void doInBackground(String...params){    
try{

startUnlock(params[0]);

}catch(Exception e){
pd.dismiss();   //close the dialog if error occurs
}
return null;

}
protected void onPostExecute(Void result){
pd.dismiss();
}


}

 
}


