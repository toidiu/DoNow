package toidiu.com.donow.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class SaveLoadHandler<T> {

    protected final Type type;
    private String Filename;

    public SaveLoadHandler(Type type, String filename){
        this.type = type;
        this.Filename = filename;
        //Log.d("------TYPE--------",           this.type.toString());
    }

    public void saveData(Context ctx, T data){
        Gson gson = new Gson();
        String write = gson.toJson(data, this.type);
        Log.d("Write", write);


        File file = new File(ctx.getFilesDir() + File.separator + this.Filename);
//Log.d("TEST","WRITING TO FILE: " + file.getAbsolutePath());
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, false);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(write);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("TEST","EXCEPTION WHEN WRITING FILE");
            e.printStackTrace();
        }
    }

    public T loadData(Context ctx) {
        StringBuilder builder = new StringBuilder("");
        File file = new File(ctx.getFilesDir() + File.separator + this.Filename);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String read;
            while((read = bufferedReader.readLine()) != null){
                builder.append(read);
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Log.d("READ", builder.toString());
        return gson.fromJson(builder.toString(), this.type);
    }

}
