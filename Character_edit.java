package melius.nathan.character_sheet;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Attr;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Character_edit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView StrView,ConView,DexView,IntView,WisView,ChaView,StrModView,ConModView,DexModView,IntModView,WisModView,ChaModView, AC,
                     Fort,Ref,Will, WeaponView;
    private RadioButton Ath, End, Acro, Stealth, Thievery, Arcana, History, Religion, Dungeoneering,Heal, Insight, Nature, Perception,
                     Bluff, Diplomacy, Intimidate, Streetwise;
    private EditText CharName, CharLvl;

    private String   raceString, classString, nameString;

    private Spinner RaceSpin,ClassSpin;

    private Button addStr, addCon, addDex, addInt, addWis, addCha;

    private int[] CharAttri= new int[6];

    private int previousPos;

    static final File xmlDir = Environment.getExternalStorageDirectory();
    static File xmlPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_edit);

        //set up the spinners so the user has the options available
        RaceSpin = (Spinner) findViewById(R.id.race_spinner);
        ClassSpin = (Spinner) findViewById(R.id.class_spinner);
        ArrayAdapter<CharSequence> raceAdapter = ArrayAdapter.createFromResource(this,R.array.races_array,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this, R.array.class_array,android.R.layout.simple_spinner_item);

        raceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RaceSpin.setAdapter(raceAdapter);
        RaceSpin.setOnItemSelectedListener(this);
        ClassSpin.setAdapter(classAdapter);

        addStr = (Button) findViewById(R.id.addstr);
        addCon = (Button) findViewById(R.id.addcon);
        addDex = (Button) findViewById(R.id.addDex);
        addInt = (Button) findViewById(R.id.addint);
        addWis = (Button) findViewById(R.id.addWis);
        addCha= (Button) findViewById(R.id.addcha);

        StrView = (TextView) findViewById(R.id.Str);
        ConView = (TextView) findViewById(R.id.Con);
        DexView = (TextView) findViewById(R.id.Dex);
        IntView = (TextView) findViewById(R.id.Int);
        WisView = (TextView) findViewById(R.id.Wis);
        ChaView = (TextView) findViewById(R.id.Cha);
        StrModView = (TextView) findViewById(R.id.StrMod);
        ConModView = (TextView) findViewById(R.id.ConMod);
        DexModView = (TextView) findViewById(R.id.DexMod);
        IntModView = (TextView) findViewById(R.id.IntMod);
        WisModView = (TextView) findViewById(R.id.WisMod);
        ChaModView = (TextView) findViewById(R.id.ChaMod);

        AC = (EditText) findViewById(R.id.ACNo);
        Fort = (TextView) findViewById(R.id.FortitudeNo);
        Ref = (TextView) findViewById(R.id.ReflexNo);
        Will = (TextView) findViewById(R.id.WillNo);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            String fileName = intent.getStringExtra(MainMenu.FILE_NAME);


            xmlPath = new File(xmlDir, "/data/" + fileName + ".xml");

            try {
                loadCharacter(xmlPath);
            }catch(Exception e){
                throw new RuntimeException(e);
            }
        }


    }

    public void GetAttributes(View view){
        Random RNG = new Random();


        //Assign a random number to each attribute
        //0=Str, 1=Con, 2=Dex, 3=Int, 5=Wis, 6=Cha
        for(int i = 0; i < CharAttri.length; i++) {
            CharAttri[i] = RNG.nextInt(15) + 3;
        }//for

        StrView.setText(CharAttri[0]);
        ConView.setText(CharAttri[1]);
        DexView.setText(CharAttri[2]);
        IntView.setText(CharAttri[3]);
        WisView.setText(CharAttri[4]);
        ChaView.setText(CharAttri[5]);

        for(int i = 0; i < CharAttri.length;i++){

            CharAttri[i] = (CharAttri[i]-10)/2;
        }

        StrModView.setText(CharAttri[0]);
        ConModView.setText(CharAttri[1]);
        DexModView.setText(CharAttri[2]);
        IntModView.setText(CharAttri[3]);
        WisModView.setText(CharAttri[4]);
        ChaModView.setText(CharAttri[5]);

        CharLvl = (EditText) findViewById(R.id.CharLvl);

        int Level = Integer.parseInt(CharLvl.getText().toString());

        int FortVal, RefVal, WillVal;

        if(CharAttri[0] >= CharAttri[1] ){
            FortVal = 10 + CharAttri[0] + (Level/2);
        }else{
            FortVal = 10 + CharAttri[1] + (Level/2);
        }
        if(CharAttri[2] >= CharAttri[3] ){
            RefVal = 10 + CharAttri[2] + (Level/2);
        }else{
            RefVal = 10 + CharAttri[3] + (Level/2);
        }
        if(CharAttri[4] >= CharAttri[5] ){
            WillVal = 10 + CharAttri[4] + (Level/2);
        }else{
            WillVal = 10 + CharAttri[5] + (Level/2);
        }

        Fort.setText(FortVal);
        Ref.setText(RefVal);
        Will.setText(WillVal);







    }//GetAttributes()

    private void addAttribute(View view){

        int counter = 0;
        switch(view.getId()){

            case R.id.addcha:

                counter++;

            case R.id.addWis:

                counter++;

            case R.id.addint:

                counter++;

            case R.id.addDex:

                counter++;

            case R.id.addcon:

                counter++;

            case R.id.addstr:



            default:

                CharAttri[counter] = CharAttri[counter]+2;


        }
        addStr.setEnabled(false);
        addCon.setEnabled(false);
        addDex.setEnabled(false);
        addInt.setEnabled(false);
        addWis.setEnabled(false);
        addCha.setEnabled(false);


    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch(pos){

            //human
            case 0:

                addStr.setEnabled(true);
                addCon.setEnabled(true);
                addDex.setEnabled(true);
                addInt.setEnabled(true);
                addWis.setEnabled(true);
                addCha.setEnabled(true);

                break;
            //Dwarf
            case 1:
                CharAttri[1] = CharAttri[1]+2;

                addStr.setEnabled(true);
                addWis.setEnabled(true);

                break;
            //Elf
            case 2:
                CharAttri[2] = CharAttri[2]+2;

                addInt.setEnabled(true);
                addWis.setEnabled(true);

                //Halfling
            case 3:
                CharAttri[2] = CharAttri[2]+2;

                addCon.setEnabled(true);
                addCha.setEnabled(true);

            default:
                Toast.makeText(this,"No Race Selected",Toast.LENGTH_SHORT);
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void setModifiers(int[] Attributes){

        for(int i = 0; i < Attributes.length; i++){

            Attributes[i] = (Attributes[i]-10)/2;
        }

        StrModView.setText(Attributes[0]);
        ConModView.setText(Attributes[1]);
        DexModView.setText(Attributes[2]);
        IntModView.setText(Attributes[3]);
        WisModView.setText(Attributes[4]);
        ChaModView.setText(Attributes[5]);
    }
    public void onSave(View view){

        nameString = CharName.getText().toString();



        File newFile = new File( xmlDir,"/data/"+nameString+".xml");

        try{
            newFile.createNewFile();
        }catch(IOException e){
            Log.e("IOException","Could not create file to filepath");
        }
        FileOutputStream xmlOutput = null;
        try{
            xmlOutput = new FileOutputStream(xmlDir);
        }catch(IOException e){
            Log.e("IOException","Unable to create FileOutputStream using file directory");
        }
        XmlSerializer xmlSer = Xml.newSerializer();
        try{
            xmlSer.setOutput(xmlOutput,"UTF-8");
            xmlSer.startDocument(null,Boolean.valueOf(true));
            xmlSer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);


            xmlSer.startTag(null,"Character Stats");

            //start adding the character's basic Bio information
            xmlSer.startTag(null, "Character Bio");
            xmlSer.startTag(null, "Character Name");
            xmlSer.text(CharName.getText().toString());
            xmlSer.endTag(null,"Character Name");
            xmlSer.startTag(null, "Race");
            xmlSer.text(raceString);
            xmlSer.endTag(null,"Race");
            xmlSer.startTag(null, "Class");
            xmlSer.text(classString);
            xmlSer.endTag(null, "Class");
            xmlSer.startTag(null, "Level");
            xmlSer.text(CharLvl.getText().toString());
            xmlSer.endTag(null,"Level");
            xmlSer.endTag(null, "Character Bio");

            //start doing Attributes now
            xmlSer.startTag(null, "Attributes");
            xmlSer.startTag(null, "Strength");
            xmlSer.text(StrView.getText().toString());
            xmlSer.endTag(null, "Strength");
            xmlSer.startTag(null, "Constitution");
            xmlSer.text(ConView.getText().toString());
            xmlSer.endTag(null, "Constitution");
            xmlSer.startTag(null, "Dexterity");
            xmlSer.text(DexView.getText().toString());
            xmlSer.endTag(null, "Dexterity");
            xmlSer.startTag(null, "Intelligence");
            xmlSer.text(IntView.getText().toString());
            xmlSer.endTag(null, "Intelligence");
            xmlSer.startTag(null, "Wisdom");
            xmlSer.text(WisView.getText().toString());
            xmlSer.endTag(null, "Wisdom");
            xmlSer.startTag(null, "Charisma");
            xmlSer.text(ChaView.getText().toString());
            xmlSer.endTag(null, "Charisma");
            xmlSer.endTag(null, "Attributes");

            //start doing skills now
            xmlSer.startTag(null,"Skills");
            xmlSer.startTag(null,"Athletics");
            xmlSer.text( String.valueOf(Ath.isChecked()));
            xmlSer.endTag(null,"Athletics");
            xmlSer.startTag(null,"Endurance");
            xmlSer.text( String.valueOf(End.isChecked()));
            xmlSer.endTag(null,"Endurance");
            xmlSer.startTag(null,"Acrobatics");
            xmlSer.text( String.valueOf(Acro.isChecked()));
            xmlSer.endTag(null,"Acrobatics");
            xmlSer.startTag(null,"Stealth");
            xmlSer.text( String.valueOf(Stealth.isChecked()));
            xmlSer.endTag(null,"Stealth");
            xmlSer.startTag(null,"Thievery");
            xmlSer.text( String.valueOf(Thievery.isChecked()));
            xmlSer.endTag(null,"Thievery");
            xmlSer.startTag(null,"Arcana");
            xmlSer.text( String.valueOf(Arcana.isChecked()));
            xmlSer.endTag(null,"Arcana");
            xmlSer.startTag(null,"History");
            xmlSer.text( String.valueOf(History.isChecked()));
            xmlSer.endTag(null,"History");
            xmlSer.startTag(null,"Religion");
            xmlSer.text( String.valueOf(Religion.isChecked()));
            xmlSer.endTag(null,"Religion");
            xmlSer.startTag(null,"Dungeoneering");
            xmlSer.text( String.valueOf(Dungeoneering.isChecked()));
            xmlSer.endTag(null,"Dungeoneering");
            xmlSer.startTag(null,"Heal");
            xmlSer.text( String.valueOf(Heal.isChecked()));
            xmlSer.endTag(null,"Heal");
            xmlSer.startTag(null,"Insight");
            xmlSer.text( String.valueOf(Insight.isChecked()));
            xmlSer.endTag(null,"Insight");
            xmlSer.startTag(null,"Nature");
            xmlSer.text( String.valueOf(Nature.isChecked()));
            xmlSer.endTag(null,"Nature");
            xmlSer.startTag(null,"Perception");
            xmlSer.text( String.valueOf(Perception.isChecked()));
            xmlSer.endTag(null,"Perception");
            xmlSer.startTag(null,"Bluff");
            xmlSer.text( String.valueOf(Bluff.isChecked()));
            xmlSer.endTag(null,"Bluff");
            xmlSer.startTag(null,"Diplomacy");
            xmlSer.text( String.valueOf(Diplomacy.isChecked()));
            xmlSer.endTag(null,"Diplomacy");
            xmlSer.startTag(null,"Intimidate");
            xmlSer.text( String.valueOf(Intimidate.isChecked()));
            xmlSer.endTag(null,"Intimidate");
            xmlSer.startTag(null,"Streetwise");
            xmlSer.text( String.valueOf(Streetwise.isChecked()));
            xmlSer.endTag(null,"Streetwise");
            xmlSer.endTag(null, "Skills");

            //and the rest!
            xmlSer.startTag(null, "Defenses and Weapon");
            xmlSer.startTag(null, "Armor Class");
            xmlSer.text(AC.getText().toString());
            xmlSer.endTag(null, "ArmorClass");
            xmlSer.startTag(null, "Reflex");
            xmlSer.text(Ref.getText().toString());
            xmlSer.endTag(null, "Reflex");
            xmlSer.startTag(null, "Fortitude");
            xmlSer.text(Fort.getText().toString());
            xmlSer.endTag(null, "Fortitude");
            xmlSer.startTag(null, "Will");
            xmlSer.text(Will.getText().toString());
            xmlSer.endTag(null, "Will");
            xmlSer.endTag(null, "Defenses and Weapon");

            //Everything has been added to the Serializer.
            //Flush everything and save it to the new file
            xmlSer.endTag(null, "Character Stats");
            xmlSer.endDocument();

            xmlSer.flush();
            xmlOutput.close();


        }catch(Exception e){
            Log.e("Exception","XMLserializer failed to write, or was unable to use the fileoutputstream!");
            throw new RuntimeException(e);
        }//

    }//onSave()

    public void loadCharacter(File file)throws IOException,XmlPullParserException{
        XmlPullParserFactory xmlFactory = null;
        XmlPullParser parser = null;
        InputStream stream = null;
        ArrayList<String> list = new ArrayList();
        String duration = null;
        String distance = null;
        String instructions = null;

        try{
            stream = new FileInputStream(file);
            xmlFactory = XmlPullParserFactory.newInstance();
            parser = xmlFactory.newPullParser();
            parser.setInput(stream,null);
            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                String name = parser.getName();
                switch(event){
                    case XmlPullParser.START_TAG: break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("Character Name")){
                            CharName.setText(parser.getText());
                        }else if(name.equals("Character Level")){
                             CharLvl.setText(parser.getText());
                        }else if(name.equals("Race")){
                            String race = parser.getText();
                            int selection = 0;
                            if(race.equals("Human")){selection = 0;}
                            if(race.equals("Dwarf")){selection = 1;}
                            if(race.equals("Elf")){selection = 2;}
                            if(race.equals("Halfling")){selection = 3;}
                            RaceSpin.setSelection(selection);
                        }else if(name.equals("Class")){
                            String Charclass = parser.getText();
                            int selection = 0;
                            if(Charclass.equals("Fighter")){selection = 0;}
                            if(Charclass.equals("Wizard")){selection = 1;}
                            if(Charclass.equals("Rogue")){selection = 2;}
                            if(Charclass.equals("Cleric")){selection = 3;}
                            ClassSpin.setSelection(selection);
                        }


                        else if(name.equals("Strength")){
                            StrView.setText(parser.getText());
                            CharAttri[0] = Integer.parseInt(parser.getText());
                        }
                        else if(name.equals("Constitution")){
                             ConView.setText(parser.getText());
                            CharAttri[1] = Integer.parseInt(parser.getText());
                        }
                        else if(name.equals("Dexterity")){
                            DexView.setText(parser.getText());
                            CharAttri[2] = Integer.parseInt(parser.getText());
                        }
                        else if(name.equals("Intelligence")){
                            IntView.setText(parser.getText());
                            CharAttri[3] = Integer.parseInt(parser.getText());
                        }
                        else if(name.equals("Wisdom")){
                            WisView.setText(parser.getText());
                            CharAttri[4] = Integer.parseInt(parser.getText());
                        }
                        else if(name.equals("Charisma")){
                            ChaView.setText(parser.getText());
                            CharAttri[5] = Integer.parseInt(parser.getText());
                        }

                        else if(name.equals("Athletics")){
                            Ath.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Endurance")){
                            End.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Acrobatics")){
                            Acro.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Stealth")){
                            Stealth.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Thievery")){
                            Thievery.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Arcana")){
                            Arcana.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Histery")){
                            History.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Religion")){
                            Religion.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Dungeoneering")){
                            Dungeoneering.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Heal")){
                            Heal.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Insight")){
                            Insight.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Nature")){
                            Nature.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Perception")){
                            Perception.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Bluff")){
                            Bluff.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Diplomacy")){
                            Diplomacy.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Intimidate")){
                            Intimidate.setSelected(Boolean.valueOf(parser.getText()));
                        }
                        else if(name.equals("Streetwise")){
                            Streetwise.setSelected(Boolean.valueOf(parser.getText()));
                        }

                        else if(name.equals("Armor Class")){
                            AC.setText(parser.getText());
                        }
                        else if(name.equals("Fortitude")){
                            Fort.setText(parser.getText());
                        }
                        else if(name.equals("Reflex")){
                            Ref.setText(parser.getText());
                        }
                        else if(name.equals("Will")){
                            Will.setText(parser.getText());
                        }


                }//switch(event
                event = parser.next();


            }//while


        }finally{
            setModifiers(CharAttri);
            stream.close();
        }//finally

    }

}//Activity
