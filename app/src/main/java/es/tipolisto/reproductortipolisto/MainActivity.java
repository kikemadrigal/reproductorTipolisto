package es.tipolisto.reproductortipolisto;


import android.Manifest;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import es.tipolisto.reproductortipolisto.textospeech.MiOninitListener;

/**
 * Pantalla principal que se mostrará al iniciar la aplicación
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Para mostrar elementos en la pantalla
    ListView list;
    int itemSeleccionado;
    ArrayList<File> arrayListFiles;
    ArrayList<String> arrayListTitles;
   // ArrayList<Uri> arrayListUri=new ArrayList<Uri>();
    //Para que nos de las canciones
    MediaPlayer mediaPlayer;
    //SoundPool para reproducir mp3 de ayuda
    SoundPool soundPool;
    int sonido1,sonido2,sonido3;
    //Para manejar el audio
    AudioManager audioManager;
    //Para manejar la música
    HandlerMusic handlerMusic;
    boolean permitir;
    //Contante par alos mensajes del log
    private static final String MESNAJE_MAINACTIVITY="MENSAJEMAINACTIVTY";
    //Manejador de eventos
    ControladorVirtual controladorVirtual;
    InputProcessorActivity inputProcessorActivity;

    //Tontuna que habla el txto que le padad
    TextToSpeech textToSpeech;

    //Para que temuestre si has conectado el mando, que dispositivos hay onectados,etc
    BlueToothBroadCastReceiver blueToothBroadCastReceiver;

    //Varibale para dejar la pantalla encendida
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos el layout a la activity
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        //Obtenemos los componentes
        list=(ListView) findViewById(R.id.list);

        //Ponemos los eventos a la escucha
        list.setOnItemSelectedListener(this);
        //list.setOnItemClickListener(this);


        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        /********Rollo para reproducir son idos de menos de 1 mega*******/
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes=new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool=new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }else{
            soundPool=new SoundPool(3, AudioManager.STREAM_MUSIC,0);
        }
        sonido1=soundPool.load(this,R.raw.cope,1);
        sonido2=soundPool.load(this,R.raw.estarinformado,1);
        sonido3=soundPool.load(this,R.raw.lanoche,1);
        /*************Fin de rollo soundpool para sonidos menores de 1mega****/


        handlerMusic =new HandlerMusic(this);
        arrayListFiles=new ArrayList<File>();
        arrayListTitles=new ArrayList<String>();
        arrayListFiles.clear();
        arrayListTitles.clear();
        checkPermissionReadStorage();
        arrayListFiles=handlerMusic.getMusic();
        arrayListTitles=handlerMusic.getTitlesMusic();


        permitir=true;

        //Rellenamos la mierda del list
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayListTitles );
        list.setAdapter(arrayAdapter);

        //Esto mostrará en el Log los dispositivos bluetooth vinculados
        blueToothBroadCastReceiver=new BlueToothBroadCastReceiver(getApplicationContext());
        blueToothBroadCastReceiver.registrarBlueToothBroadCastReceiver();

        //Para que la activity reponda a los envents de las pulsaciones de teclas...
        takeKeyEvents(true);

        //Para que hable la maricona
        textToSpeech=new TextToSpeech(this, new MiOninitListener(this));


        //Eventos
        controladorVirtual=new ControladorVirtual();
        inputProcessorActivity=new InputProcessorActivity(controladorVirtual);

        itemSeleccionado=0;
        int tiempoDeloqueoPantalla=Integer.valueOf(getPreferences(MODE_PRIVATE).getString("botonX", "0"));
        turnOnScreen(tiempoDeloqueoPantalla);
        Toast.makeText(this, "El tiempo de bloqueo es: "+getPreferences(MODE_PRIVATE).getString("botonX", "0"),Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this, Preferencias.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_show_preferences) {
            Intent intent=new Intent(this, Prefer.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        textToSpeech.shutdown();
        soundPool.release();
        //Liberamos la pantalla para que no esté siempre encendida

        wakeLock=null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //wakeLock.release();
    }

    //Checkeo de los permisos del manifest
    public void checkPermissionReadStorage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }
    }




    //Respuesta de los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getApplicationContext(),"Permiso de lectura SD concedido!!!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Permiso de lectura denegado!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }




    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        itemSeleccionado= list.getSelectedItemPosition();
        Log.d(MESNAJE_MAINACTIVITY, "tecla: "+String.valueOf(keyCode));
        switch (keyCode) {
            /*****************INICIO DE FLECHAS*******************************************/
            case 19: //Flecha izquierda

                Toast.makeText(getApplicationContext(),"Izquierda - retroceder cancion", Toast.LENGTH_SHORT).show();
                if(mediaPlayer.isPlaying()){
                    int segundosDeLaCancion= mediaPlayer.getCurrentPosition();
                    segundosDeLaCancion +=20;
                    mediaPlayer.seekTo(segundosDeLaCancion);
                }
                Log.d(MESNAJE_MAINACTIVITY, "Izquierda la cancion a avanzado de : ");
                return true;
            case 20: // Flecha derecha
                Toast.makeText(getApplicationContext(),"Derecha - avanzar cancion", Toast.LENGTH_SHORT).show();
                if(mediaPlayer.isPlaying()){
                    int segundosDeLaCancion= mediaPlayer.getCurrentPosition();
                    segundosDeLaCancion -=20;
                    mediaPlayer.seekTo(segundosDeLaCancion);
                }
                return true;
            case 21: //Flecha abajo
                Log.d(MESNAJE_MAINACTIVITY, "Abajo, posicion item: "+String.valueOf(itemSeleccionado));
                list.setSelection(++itemSeleccionado);
                return true;
            case 22: //Flecha arriba
                Log.d(MESNAJE_MAINACTIVITY, "Arriba posicion item: "+ String.valueOf(itemSeleccionado));
                list.setSelection(--itemSeleccionado);
                return true;
            /*****************************FIN DE FLECHAS**********************************/



            case 96: //Boton A
                // soundPool.play(sonido1,1,1,0,0,1);
                if(!mediaPlayer.isPlaying()) mediaPlayer.start();
                return true;
            case 99: //Boton X
                //soundPool.play(sonido2,1,1,0,0,1);
                Log.d(MESNAJE_MAINACTIVITY, "pause");
                mediaPlayer.pause();
                return true;
            case 97: //Boton B
                Toast.makeText(getApplicationContext(),"Boton B - avanzar cancion", Toast.LENGTH_SHORT).show();
                int saltoCancion=0;
                if(mediaPlayer.isPlaying()){
                    saltoCancion= mediaPlayer.getDuration()/20;
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+saltoCancion);
                }
                Log.d(MESNAJE_MAINACTIVITY, "Salto en cacion de "+saltoCancion+", ahora estas en la posicion: "+mediaPlayer.getCurrentPosition());
                return true;
            case 100: //Boton Y
                Toast.makeText(getApplicationContext(),"Boton Y retroceder cancion", Toast.LENGTH_SHORT).show();
                int saltoCancionRetroceder=0;
                if(mediaPlayer.isPlaying()){
                    saltoCancionRetroceder= mediaPlayer.getDuration()/20;
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-saltoCancionRetroceder);
                }
                Log.d(MESNAJE_MAINACTIVITY, "Salto en cacion de "+saltoCancionRetroceder+", ahora estas en la posicion: "+mediaPlayer.getCurrentPosition());

                return true;



            case 103: // Boton lateral R
                Log.d(MESNAJE_MAINACTIVITY, "Subir volmen");
                Toast.makeText(getApplicationContext(),"Pulsaste subir volmen", Toast.LENGTH_SHORT).show();

                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case 102: //Boton lateral L
                Log.d(MESNAJE_MAINACTIVITY, "Bajar volumen");
                Toast.makeText(getApplicationContext(),"Pulsaste bajar volmen", Toast.LENGTH_SHORT).show();
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;
            case 108: //Boton start
                Log.d(MESNAJE_MAINACTIVITY, "start");
                mediaPlayer.stop();
                mediaPlayer.reset();
                return true;
            case 109: //Boton Select
                Log.d(MESNAJE_MAINACTIVITY, "Pulsaste finish");
                finish();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        File archivo=arrayListFiles.get(i);
        Uri urisong=Uri.parse(archivo.getPath());
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }else{
            mediaPlayer=MediaPlayer.create(getApplicationContext(), urisong);
            Toast.makeText(getApplicationContext(), "duracion= "+mediaPlayer.getDuration(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //mediaPlayer.release();
    }


    private void obtenerDispositivos(){
        Set<BluetoothDevice> bluetoothDevices=blueToothBroadCastReceiver.mostrarDispositivosBlueThoodConectadosPorLog();
        for (BluetoothDevice device : bluetoothDevices) {
            // Add the name and address to an array adapter to show in a ListView
            Log.d(MESNAJE_MAINACTIVITY,device.getName() + "-->" + device.getAddress());
        }
        Log.d(MESNAJE_MAINACTIVITY, "pulsaste mostrar dispositivos bluetooth");
    }


    /*public void mostrarPreferencias(){

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String botonA=preferences.getString("botonA","?");
        Toast.makeText(getApplicationContext(), "Boton A: "+botonA,Toast.LENGTH_LONG).show();
    }*/
    private void turnOnScreen(int tiempoPantallaActivada) {
        /*PowerManager.WakeLock screenLock = null;

        if ((getSystemService(POWER_SERVICE)) != null) {
            screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire(10*60*1000L);
            screenLock.release();
        }else{
            Toast.makeText(this, "No se pudo bloquear la pantalla y ponerla siempre activa", Toast.LENGTH_LONG).show();
        }*/
        //Vamos a dejar la pantalla siempre encendida para eso obtenemos el objeto wakelock:
        wakeLock=null;
        PowerManager myPowerManager=(PowerManager)getSystemService(POWER_SERVICE);
        wakeLock=myPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "myapp:mywakelocktag");
        wakeLock.acquire(tiempoPantallaActivada*60*1000L);
    }


}
