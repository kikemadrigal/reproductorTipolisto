package es.tipolisto.reproductortipolisto;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.media.AudioManager;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Casa on 28/05/2018.
 */

public class InputProcessorActivity  {
    private final static String MENSAJE_INPUT="MENSAJEINPUT";
    private ControladorVirtual controladorVirtual;
    public InputProcessorActivity(ControladorVirtual controladorVirtual){
        this.controladorVirtual=controladorVirtual;
    }



    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // Log.d(MESNAJE_MAINACTIVITY, "Se presiono "+String.valueOf(keyCode));
        switch (keyCode) {
            case 96:
                Log.d(MENSAJE_INPUT, "start");
                controladorVirtual.play=true;
                return true;
            case 67:
                Log.d(MENSAJE_INPUT, "pause - reanudar");
                controladorVirtual.pause=true;
                return true;
            case 4:
                Log.d(MENSAJE_INPUT, "Pulsaste finish");
                controladorVirtual.finish=true;
                return true;
            case 100:
                return true;
            case 103:
                Log.d(MENSAJE_INPUT, "Subir volmen");
                controladorVirtual.subirVolmen=true;
                return true;
            case 102:
                Log.d(MENSAJE_INPUT, "Bajar volumen");
                controladorVirtual.bajarVolumen=true;
                return true;
            default:
                return true;
        }
    }

}
