package melius.nathan.character_sheet;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private static int saves = 0;

    static final String FILE_NAME = "FileName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        File sdCardRoot = Environment.getExternalStorageDirectory();
        File XmlDirectory = new File(sdCardRoot, "/data/");
        for(File f: XmlDirectory.listFiles()){
            if(f.isFile()){
                saves++;
                String name = f.getName();

                Button savedChar = new Button(this);
                savedChar.setText(name);
                savedChar.setOnClickListener(this);
                savedChar.setId(saves);
            }
        }
    }
    @Override
    public void onClick(View view){
        Intent loadCharacter= new Intent(this,Character_edit.class);
        Button button = (Button) findViewById(view.getId());
        String fileName = button.getText().toString();
        loadCharacter.putExtra(FILE_NAME,fileName);
        startActivity(loadCharacter);

    }

    public void onNewCharacter(View view){
        Intent intent = new Intent(this, Character_edit.class);
        startActivity(intent);
    }
}
