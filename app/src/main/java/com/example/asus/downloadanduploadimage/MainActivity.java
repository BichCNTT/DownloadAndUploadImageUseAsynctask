package com.example.asus.downloadanduploadimage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    String string_url = "http://mobileadvertisingwatch.com/wp-content/uploads/2016/02/Can-You-Name-the-Best-Android-App-Dev-Companies-GoodFirms-Just-Released-Its-List.jpg";
//  String string_url = "https://yt3.ggpht.com/-2KXvguf9c8E/AAAAAAAAAAI/AAAAAAAAAAA/GKYOlzIEpHc/s900-c-k-no-mo-rj-c0xffffff/photo.jpg";
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.image_view)
    ImageView imageView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        DownLoadTask downLoadTask = new DownLoadTask();
        downLoadTask.execute(string_url);
    }

    class DownLoadTask extends AsyncTask<String, Integer, String> implements DownLoad {

        @Override
        protected String doInBackground(String... voids) {
            URL downloadURL=null;
            int counter=0;
            HttpURLConnection connection=null;
            InputStream inputStream=null;
            FileOutputStream fileOutputStream=null;
            File file=null;
            try{
                downloadURL=new URL(voids[0]);
                connection=(HttpURLConnection) downloadURL.openConnection();
                inputStream=connection.getInputStream();
                file=new File(Environment.getExternalStorageDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                        +"/"+ Uri.parse(voids[0]).getLastPathSegment());
                fileOutputStream=new FileOutputStream(file);
                int read=-1;
                byte[]buffer=new byte[1024];
                while ((read=inputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,read);
                    counter+=read;
                    publishProgress(counter);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            String path = voids[0];
//            int fileLength = 0;
//            try {
//                URL url = new URL(path);
//                URLConnection urlConnection = url.openConnection();
//                urlConnection.connect();
//                fileLength = urlConnection.getContentLength();
//                File newFolder = new File("Downloads");
//                if (!newFolder.exists()) {
//                    newFolder.mkdir();
//                }
//                File fileInput = new File(newFolder, "download_image.jpg");
//                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
//                byte[] data = new byte[1024];
//                int total = 0;
//                int count = 0;
//                int read=-1;
//                OutputStream outputStream = new FileOutputStream(newFolder);
//                while ((read = inputStream.read(data)) != -1) {
//                    total += count;
//                    outputStream.write(data, 0, read);
//                    int progress = (int) total * 100 / fileLength;
//                    publishProgress(progress);
//                }
//                inputStream.close();
//                outputStream.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return "Download complete";
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Download in progress...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), aVoid, Toast.LENGTH_LONG).show();
            String path = "Downloads/download_image.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(path));
        }
    }
}
