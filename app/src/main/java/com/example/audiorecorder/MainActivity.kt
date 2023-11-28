package com.example.audiorecorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {

    var mediaRecord: MediaRecorder = MediaRecorder()
    var mediaPlayer: MediaPlayer = MediaPlayer()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(isMicrophonePresent()){
            getMicrophonePermission()
        }


        var start: Button = findViewById<Button>(R.id.startRecording)
        var stop: Button = findViewById(R.id.stopRecording)
        var play: Button = findViewById(R.id.playAudio)

        start.setOnClickListener {
            try{
                mediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecord.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecord.setOutputFile(getPath())
                mediaRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                mediaRecord.prepare()
                mediaRecord.start()

                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            }catch ( e:Exception){
                e.printStackTrace()
            }
        }

        stop.setOnClickListener {

            try{
                mediaRecord.stop()
                mediaRecord.release()
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()

            }catch (e:Exception){
                e.printStackTrace()
            }



        }

        play.setOnClickListener {

            try{
                mediaPlayer.setDataSource(getPath())
                mediaPlayer.prepare()
                mediaPlayer.start()
                Toast.makeText(this, "Recorded audio is playing", Toast.LENGTH_SHORT).show()

            }catch(e:Exception){e.printStackTrace()}

        }




    }


    fun isMicrophonePresent(): Boolean {

        if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
        {
            return true;
        }else{
            return false;
        }
    }

    fun getMicrophonePermission(){
        val permissionList = mutableListOf<String>()
        if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED) permissionList.add(android.Manifest.permission.RECORD_AUDIO)
        if(permissionList.size > 0) requestPermissions(permissionList.toTypedArray(), 101)

    }

    fun getPath(): String{
        var contextWrapper: ContextWrapper = ContextWrapper(this)
        var musicDirectory : File? = contextWrapper.getExternalFilesDir((Environment.DIRECTORY_MUSIC))
        var file = File(musicDirectory, "test.mp3")
        return file.path;

    }

}