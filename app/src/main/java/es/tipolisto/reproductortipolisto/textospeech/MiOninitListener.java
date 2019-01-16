package es.tipolisto.reproductortipolisto.textospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Iniciallizador del lector que habla
 */

public class MiOninitListener implements TextToSpeech.OnInitListener {
    private static final String MESNAJE_ONINITLISTENER="MENSAJETTSOIL";
    TextToSpeech textToSpeech;
    public MiOninitListener(Context context){
        this.textToSpeech=new TextToSpeech(context, this);
    }
    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int result=this.textToSpeech.setLanguage(new Locale("spa", "ESP"));
            if(result==TextToSpeech.LANG_MISSING_DATA ||
                    result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.d(MESNAJE_ONINITLISTENER, "This Language is not supported");
            }
        }
        else
            Log.d(MESNAJE_ONINITLISTENER, "No se pudo inicializar el textToSpeech");
    }
}
