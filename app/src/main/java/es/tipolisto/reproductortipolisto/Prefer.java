package es.tipolisto.reproductortipolisto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Casa on 15/06/2018.
 */

public class Prefer extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText editTextBotonX, editTextBotonY,editTextBotonA,editTextBotonB;
    private Button botonX, botonY,botonA,botonB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_preferencias);
        /**
         * Obtenemos las preferencias y el editor para manjarlas
         */
        sharedPreferences=getPreferences(MODE_PRIVATE);
        editor=sharedPreferences.edit();
        /**
         * Obtenemos los views
         */
        botonX=(Button) findViewById(R.id.buttonCambiarBotonX);
        botonY=(Button) findViewById(R.id.buttonCambiarBotonY);
        botonA=(Button) findViewById(R.id.buttonCambiarBotonA);
        botonB=(Button) findViewById(R.id.buttonCambiarBotonB);

        editTextBotonX=(EditText) findViewById(R.id.editText1BotonX);
        editTextBotonY=(EditText) findViewById(R.id.editText1BotonY);
        editTextBotonA=(EditText) findViewById(R.id.editText1BotonA);
        editTextBotonB=(EditText) findViewById(R.id.editText1BotonB);
        /**
         * Asignamos los escuchadores
         */
        botonX.setOnClickListener(this);
        botonY.setOnClickListener(this);
        botonA.setOnClickListener(this);
        botonB.setOnClickListener(this);
        /**
         * Asgnamos los valores de las preferencias a los textView
         */
        editTextBotonX.setText(sharedPreferences.getString("botonX", "0"));
        editTextBotonY.setText(sharedPreferences.getString("botonY", "0"));
        editTextBotonA.setText(sharedPreferences.getString("botonA", "0"));
        editTextBotonB.setText(sharedPreferences.getString("botonB", "0"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCambiarBotonX:
                editor.putString("botonX",editTextBotonX.getText().toString());
                Toast.makeText(this, "Cambiado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonCambiarBotonY:
                editor.putString("botonY",editTextBotonY.getText().toString());
                Toast.makeText(this, "Cambiado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonCambiarBotonA:
                editor.putString("botonA",editTextBotonA.getText().toString());
                Toast.makeText(this, "Cambiado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonCambiarBotonB:
                editor.putString("botonB",editTextBotonB.getText().toString());
                Toast.makeText(this, "Cambiado",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //Toast.makeText(this, "CPulsado el "+String.valueOf(keyCode),Toast.LENGTH_SHORT).show();
        View view=getCurrentFocus();
        int idView=view.getId();
        switch (idView){
            case R.id.editText1BotonX:
                //editTextBotonX.setText(String.valueOf(keyCode));
                break;
            case R.id.editText1BotonY:
               // editTextBotonY.setText(String.valueOf(keyCode));
                break;
            case R.id.editText1BotonA:
                //editTextBotonA.setText(String.valueOf(keyCode));
                break;
            case R.id.editText1BotonB:
               // editTextBotonB.setText(String.valueOf(keyCode));
                break;
        }
        return true;
    }



}
