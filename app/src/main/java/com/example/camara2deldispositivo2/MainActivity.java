package com.example.camara2deldispositivo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//luego de hacer el diseño vamos a la carpeta y al archivo manifest para agregar los ermisos
// ecesarios para usar a camara
//<uses-feature android:name="android.hardware.camera2" android:required="true" />
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
// si sale error o en rojo hayq ue darle alt+enter y darle crear xml
// luego en el archiivo que se creo xml filepath vamos a modficarlos desde el codigo
// ya no deberia saltar error porque le asignamos la rota donde guardara las fotos


public class MainActivity extends AppCompatActivity {



    ImageView img_foto;
    ImageButton btn_camara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_foto = (ImageView)findViewById(R.id.img_foto);
        btn_camara = (ImageButton) findViewById(R.id.btn_camara);

        // ahora tenemos que validar los permisos que asignamos si sale error con alt+enter para importar la clase
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        //hacemos el metodo onclick para que realice una haccion de tomar foto en el imageview
        btn_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                tomarFoto();
            }
        });
    }

    final int REQUEST_TAKE_PHOTO = 1;
    private void tomarFoto(){
        //esta linea nos envia a la pantalla donde nos permite tomar la foto
        Intent tomarfotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //ahora hacemos una validacion por si hay algun tipo de error
        if (tomarfotoIntent.resolveActivity(getPackageManager()) != null){
            //entonces lo que hacemos es que creamos un archivo para la foto
            File photoFile = null;
            try {
                // si aparece en rojo es porque no se a creado el metodo
                photoFile = crearArchivoImg();
            }catch (Exception e){
                e.getStackTrace();
            }
            //ahora validadmos y decimos si foto file es diferente de nulo
            if (photoFile != null){
                // si la img que se tomo no retorno un valor vacio vamos a crear una uri
                Uri fotoUri = FileProvider.getUriForFile(MainActivity.this,
                        "com.example.camara2deldispositivo2",
                        photoFile);
                //tomarfotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                tomarfotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri.toString());
                startActivityForResult(tomarfotoIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String rutaAbsolutaImagen; //variable global que guarda la ruta de la imagen
    //metodo para crear el archivo
    private File crearArchivoImg() throws IOException {
        //ahora le decimos que cuadno tome la foto le asigne un nombre
        String fecha = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
        String nombreFoto = "Imagen_" + fecha;

        //ahora establecems en que directorio se guardara la imagen
        File directArchivo = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // aqui asignamos la ruta cone xtencion d ela imagen
        File foto = File.createTempFile( // cremos una execiopn con alt+enter
                nombreFoto,  /* prefix */
                ".jpg",         /* suffix */
                directArchivo      /* directory */
        );
        rutaAbsolutaImagen = foto.getAbsolutePath();
        return foto;

    }


    //con este metodo obtenemos una miniatura o una vsta previa d ela imagen

    final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img_foto.setImageBitmap(imageBitmap); // el img es el de la variable o id que declaramos arriba

        }
    }



}
