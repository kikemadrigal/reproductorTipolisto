package es.tipolisto.reproductortipolisto;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Casa on 28/05/2018.
 */

public class HandlerMusic {
    private Activity activity;

    public HandlerMusic(Activity activity){
        this.activity =activity;
    }



    public ArrayList<File> getMusic(){
        ArrayList<File> files=new ArrayList<File>();
        String variableAyudaParaSaberElTipoArchivo="";
        boolean variableParaSaberSiContieneAudioEnLaRuta=false;
        ContentResolver contentResolver=this.activity.getContentResolver();
        Uri songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor=contentResolver.query(songUri,null,null,null,null);

        if(songCursor !=null && songCursor.moveToFirst()){
            int songTitle=songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist=songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation=songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do{
                //String currentTitle=songCursor.getString(songTitle);
               // String currentArtist=songCursor.getString(songArtist);
                String currentLocation=songCursor.getString(songLocation);
               variableAyudaParaSaberElTipoArchivo=currentLocation.substring(currentLocation.length()-3,currentLocation.length());
               variableParaSaberSiContieneAudioEnLaRuta=currentLocation.contains("Audios");
                //Log.d("LOG",currentLocation+"-->"+variableParaSaberSiContieneAudioEnLaRuta);
                if(variableAyudaParaSaberElTipoArchivo.equalsIgnoreCase("mp3") && variableParaSaberSiContieneAudioEnLaRuta){
                    files.add(new File(currentLocation));
                    Log.d("LOG", "aÑADIDO: "+currentLocation);
                }

                //arrayListUri.add(Uri.parse(currentLocation));
            }while(songCursor.moveToNext());
        }
        return files;
    }

    public ArrayList<String> getTitlesMusic(){
        ArrayList<String> files=new ArrayList<String>();
        String variableAyudaParaSaberElTipoArchivo;
        boolean variableParaSaberSiContieneAudioEnLaRuta=false;
        ContentResolver contentResolver=this.activity.getContentResolver();
        Uri songUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor=contentResolver.query(songUri,null,null,null,null);

        if(songCursor !=null && songCursor.moveToFirst()){
            int songTitle=songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist=songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation=songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do{
                //String currentTitle=songCursor.getString(songTitle);
                //String currentArtist=songCursor.getString(songArtist);
                String currentLocation=songCursor.getString(songLocation);
                variableAyudaParaSaberElTipoArchivo=currentLocation.substring(currentLocation.length()-3,currentLocation.length());
                variableParaSaberSiContieneAudioEnLaRuta=currentLocation.contains("Audios");
                if(variableAyudaParaSaberElTipoArchivo.equalsIgnoreCase("mp3")&& variableParaSaberSiContieneAudioEnLaRuta) {
                    // files.add(currentTitle);
                    files.add(currentLocation);
                }
                //arrayListUri.add(Uri.parse(currentLocation));
            }while(songCursor.moveToNext());
        }
        return files;
    }



    private boolean comprobarQueEsMp3(String ruta){
        boolean esMp3=false;
        String valor3Ultimoscaracteres=ruta.substring(ruta.length()-3, ruta.length());
        if(valor3Ultimoscaracteres.equalsIgnoreCase("mp3")){
            esMp3=true;
        }
        return esMp3;
    }

}
