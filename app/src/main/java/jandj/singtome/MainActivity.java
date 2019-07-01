package jandj.singtome;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // TODO postavi btn za snimanje i potpis svoj


    MediaRecorder recorder;
    File audioFile;
    private EditText edEmail;
    private EditText edSubject;
    private EditText edMessage;
    private Button btnStartRec;
    private Button btnStopRec;
    private Button btnAddAudio;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edEmail = findViewById(R.id.editAddres);
        edSubject = findViewById(R.id.editSubject);
        edMessage = findViewById(R.id.editMessage);
        btnAddAudio = findViewById(R.id.floatingActionButton);

        btnStartRec = (Button) findViewById(R.id.btnStartRec);
        btnStopRec = (Button) findViewById(R.id.btnStopRec);

        btnStartRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO napravi za poćetak
                if (view.getId() == R.id.btnStartRec) {
                    startRecording();
                }
                if (view.getId() == R.id.btnStopRec) {
                    stopRecording();
                }
            }
        });
        btnStopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO napravi za kraj
                if (view.getId() == R.id.btnStartRec) {
                    startRecording();
                }
                if (view.getId() == R.id.btnStopRec) {
                    stopRecording();
                }
            }
        });

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputOk()) {
                    sendEmail(edEmail.getText().toString(),
                            edSubject.getText().toString(),
                            edMessage.getText().toString());
                }
            }
        });

        btnAddAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO dodaj snimku

            }
        });


    }

    private boolean inputOk() {
        if (edEmail.getText().length() == 0
                || edSubject.getText().length() == 0
                || edMessage.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please fill all data!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendEmail(String address, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(emailIntent, "Please select application"));
    }

    /*public void startOrStop(View view) {
        if (view.getId() == R.id.btnStartRec) {
            startRecording();
        }
        if (view.getId() == R.id.btnStopRec) {
            stopRecording();
        }
    }*/

    private void startRecording() {
        btnStartRec.setEnabled(false);// Zatamni gumb
        btnStopRec.setEnabled(true);

        File sampleDir = Environment.getExternalStorageDirectory();
        try {
            audioFile = File.createTempFile("zvuk", ".3gp", sampleDir);// postaviš naziv, format i dopuštenje za rec u direktori na SD
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        recorder = new MediaRecorder(); // Kreiraš recoder
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // Pomoću njega snima
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// Format u kojem snima
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // Encoder
        recorder.setOutputFile(audioFile.getAbsolutePath()); // Put gdje da spremi

        try {
            recorder.prepare();// Priprema ako dođe do pogreške
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();

    }

    private void stopRecording() {
        btnStartRec.setEnabled(true);
        btnStopRec.setEnabled(false);// Zatamni gumb

        recorder.stop();
        recorder.release();// Otpuštaš u memoriji

        addRecordToMediaLibrary();// Spremaš u library i pretražuješ ju pomoću MediaScannera
    }

    private void addRecordToMediaLibrary() {
        try {
            Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);// Stvoriš intent za Media Scanner
            i.setData(Uri.fromFile(audioFile)); // Postaviš uri koju datoteku želiš pretražiti
            sendBroadcast(i);// Pošalješ intent
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
