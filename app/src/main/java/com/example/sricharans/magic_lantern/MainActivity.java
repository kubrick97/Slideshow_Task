
        package com.example.sricharans.magic_lantern;

        import android.content.ContentResolver;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView timerTextView;
    Spinner musicSelector;
    ImageView mImageView;

    private String selectedMusicPath;
    List<music_info> musicPaths = new ArrayList<>();
    ArrayAdapter<music_info> adapter;

    private static final List<Integer> pictures = new ArrayList<Integer>(){
        {
            add(R.drawable.p1);
            add(R.drawable.p2);
            add(R.drawable.p3);
            add(R.drawable.p4);
            add(R.drawable.p5);
            add(R.drawable.p6);
            add(R.drawable.p7);

        }
    };
    private int counter = 0;

    boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerTextView = (TextView) findViewById(R.id.show_timer);
        mImageView = (ImageView) findViewById(R.id.imageView);
        LoadMusic();
        mImageView.setImageResource(pictures.get(0));
        mImageView.setAdjustViewBounds(true);


    }



    void startSlideShow(){
        startTimer(1000*3*pictures.size());
        CountDownTimer t;
        t = new CountDownTimer(1000 * 3 * pictures.size(), 3 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageResource(pictures.get(counter));
                        counter = counter + 1 >= pictures.size()?0:counter + 1;
                    }
                });
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"End of slideshow",Toast.LENGTH_SHORT).show();
                isStarted = false;
            }
        };
        t.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 1:
                Intent playMusic = new Intent(this,ServiceMusic.class);
                playMusic.putExtra("musicPath",selectedMusicPath);
                startService(playMusic);
                break;
            case 2:
                ServiceMusic.getInstance().pauseMusic();
                break;
            case 3:
                ServiceMusic.getInstance().resumeMusic();
                break;
            case 4:
                ServiceMusic.getInstance().StopMusic();
                break;
            case 5:
                if(!isStarted){
                    startSlideShow();
                    isStarted = true;
                }

                break;

            default: break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int groupID = 1;

        menu.add(groupID,1,1,"Music, pls!").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(groupID,2,2,"||").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(groupID,3,3,"|>").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(groupID,4,4,"[]").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(groupID,5,5,"Slideshow").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }


    private void LoadMusic(){


        musicSelector = (Spinner) findViewById(R.id.music_choice);
        adapter = new adapter_music(this,musicPaths);

        musicSelector.setAdapter(adapter);
        musicSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                music_info data = (music_info)adapterView.getItemAtPosition(position);
                selectedMusicPath = data.filePath;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new Thread(new Runnable() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void run() {
                ContentResolver cr = getContentResolver();
                String SortOrder = MediaStore.Audio.Media.TITLE + " ASC";
                Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Audio.Media.IS_MUSIC + " = 1",null,SortOrder);

                if(cursor.getCount() >0){
                    //
                    int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int dataColumn  = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    while (cursor.moveToNext()){
                        String musicPath = cursor.getString(dataColumn);
                        String title = cursor.getString(titleColumn);
                        adapter.add(new music_info(title,musicPath));
                    }
                }
                cursor.close();
            }
        }).run();
    }

    private void startTimer(int value){
        final int _value = value;
        new CountDownTimer(value, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Enjoy the magic lantern for: " + (_value - millisUntilFinished) / 1000);
            }

            public void onFinish() {
                timerTextView.setText("That's it, folks!");
            }
        }.start();
    }
}
