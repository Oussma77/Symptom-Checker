package com.example.symptomchecker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.symptomchecker.data.SymptomContract.SymptomEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SymptomActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the Pharmacy data loader */
    private static final int SYMPOTM_LOADER = 0;

    /** Adapter for the ListView */
    SymptomCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SymptomActivity.this, SymptomEditorActivity.class);
                startActivity(intent);
            }
        });


        // Find the ListView which will be populated with the Pharmacy data
        ListView symptomListView = (ListView) findViewById(R.id.list_symptom);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view_symptom);
        symptomListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of Pharmacy data in the Cursor.
        // There is no Pharmacy data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new SymptomCursorAdapter(this, null);
        symptomListView.setAdapter(mCursorAdapter);

        symptomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(SymptomActivity.this, SymptomEditorActivity.class);

                Uri currentSymptomlUri = ContentUris.withAppendedId(SymptomEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentSymptomlUri);

                // Launch the {@link EditorActivity} to display the data for the current doctor.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(SYMPOTM_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertSymptom();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllSymptoms();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                SymptomEntry._ID,
                SymptomEntry.COLUMN_SYMPTOM_NAME,
                SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION,
                SymptomEntry.COLUMN_SYMPTOM_NEEDHELP,
                SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION,
                SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED,
                SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED,
                SymptomEntry.COLUMN_SYMPTOM_REASONS,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                SymptomEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    private void insertSymptom() {

        //list symptom 08
        ContentValues values8 = new ContentValues();
        values8.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Запор");
        values8.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Запор — это комплекс симптомов, характеризующийся уменьшением частоты актов дефекации, изменением консистенции каловых масс (твердый, фрагментированный кал), чувством неполного опорожнения кишечника, а также необходимостью в избыточном натуживании для совершения акта дефекации ");
        values8.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Экстренная помощь не требуется ");
        values8.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "•\tПервичные запоры (врождённые или приобретённые аномалии толстой кишки).\n" +
                "•\tВторичные запоры (развиваются в результате заболевания, травмы).\n" +
                "•\tИдиопатические запоры (нарушение моторики кишечника без установленной причины)\n ");
        values8.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "•\tВести активный образ жизни.\n" +
                "•\tУвеличить объем потребляемой жидкости до 1,5-2 литров в день.\n" +
                "•\tНе пропускать приёмы пищи.\n" +
                "•\tЕсть продукты, богатые клетчаткой (овощи и фрукты, крупы).\n ");
        values8.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tОграничивать приём жидкости и пищи.\n" +
                "•\tПринимать слабительные препараты без назначения врача.\n ");
        values8.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tНеспецифические воспалительные заболевания кишечника.\n" +
                "•\tПервичный билиарный холангит.\n" +
                "•\tСиндром раздраженного кишечника.\n" +
                "•\tНовообразование толстой кишки.\n" +
                "•\tГеморрой.\n" +
                "•\tКишечная непроходимость.\n" +
                "•\tОстрый панкреатит.\n" +
                "•\tХронический холецистит.\n" +
                "•\tКишечная инфекция.\n" +
                "•\tХронический панкреатит.\n" +
                "•\tЦелиакия.\n" +
                "•\tГипотиреоз.\n ");
        Uri newUri8 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values8);

        //list symptom 02
        ContentValues values2 = new ContentValues();
        values2.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Боль в грудной клетке");
        values2.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Боль в грудной клетке — это болезненные ощущения или дискомфорт в грудной клетке различной локализации, глубины, интенсивности, длительности, условиями возникновения и сопутствующими симптомами боли. Синонимы: грудная боль, торакалгия");
        values2.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, " Боль в грудной клетке не является показанием для оказания экстренной медицинской помощи.\n" +
                "\n" +
                "Немедленное обращение к врачу необходимо при интенсивной боли в грудной клетке, не проходящей после приема нитратов, резко возникшей при физической нагрузке, а также в сочетании с такими жалобами, как бледность кожных покровов, учащенный пульс, одышка, удушье, головокружение, обморок, выраженная потливость");
        values2.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "Боли, обусловленные:\n" +
                "•\tзаболеваниями сердечно-сосудистой системы (ишемические и не ишемические боли);\n" +
                "•\tзаболеваниями легких;\n" +
                "•\tзаболеваниями желудочно-кишечного тракта;\n" +
                "•\tкостно-мышечными и нервно-психическими заболеваниями.");
        values2.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "•\tCоздать физический и психоэмоциональный покой.\n" +
                "•\tЕсли имеется заболевание сердца, ранее диагностированное врачом, необходимо принять назначавшиеся препараты.\n" +
                "•\tПри появлении жгучих, загрудинных болей показано применение нитросодержащих препаратов.\n" +
                "•\tВызвать бригаду скорой медицинской помощи.\n");
        values2.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tВыполнять физические нагрузки.\n" +
                "•\tПроводить самолечение боли в грудной клетке.\n ");
        values2.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tНейроциркуляторная дистония.\n" +
                "•\tМиокардит.\n" +
                "•\tРевматическая лихорадка с поражением сердца.\n" +
                "•\tСтенокардия.\n" +
                "•\tТромбоэмболия легочной артерии.\n" +
                "•\tГастроэзофагеальная рефлюксная болезнь (ГЭРБ).\n" +
                "•\tОпоясывающий герпес.\n" +
                "•\tСистемная красная волчанка.\n" +
                "•\tРадикулопатии.\n" +
                "•\tПневмония.\n" +
                "•\tОстрый бронхит.\n");
        Uri newUri2 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values2);

        //list symptom 03
        ContentValues values3 = new ContentValues();
        values3.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Боль в спине");
        values3.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Боль в спине — это неприятные, болезненные ощущения в области спины различные по локализации, интенсивности и длительности болевого синдрома. Синонимы: дорсалгия, люмбаго");
        values3.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Боль в спине не является показанием для оказания экстренной медицинской помощи.\n" +
                "\n" +
                "Немедленная медицинская помощь необходима при быстром нарастании болевого синдрома, а также в сочетании с такими жалобами, как болезненное мочеиспускание, одышка, затрудненное глотание, общая слабость, онемение конечностей, лихорадка");
        values3.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "По продолжительности:\n" +
                "•\tострая (менее 4 недель);\n" +
                "•\tподострая (4-12 недель);\n" +
                "•\tхроническая (более 12 недель).\n" +
                "По степени выраженности:\n" +
                "•\tслабая;\n" +
                "•\tумеренная;\n" +
                "•\tсильная;\n" +
                "•\tнестерпимая.\n ");
        values3.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "•\tОбеспечить полный физический покой.\n" +
                "•\tНосить фиксирующий пояс (корсет).\n" +
                "•\tОграничить употребление острой и соленой пищи, алкоголя.\n" +
                "•\tПроконсультироваться у специалиста.\n ");
        values3.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tВыполнять физические нагрузки, включая наклоны.\n" +
                "•\tПринимать ванну, париться в бане и сауне.\n" +
                "•\tСпать на жесткой поверхности.\n ");
        values3.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tНовообразование толстой кишки.\n" +
                "•\tЭндометриоз.\n" +
                "•\tРадикулопатии.\n" +
                "•\tПочечная колика.\n" +
                "•\tПиелонефрит.\n ");
        Uri newUri3 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values3);

        //list symptom 04
        ContentValues values4 = new ContentValues();
        values4.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Боль в суставах");
        values4.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Боль в суставах — это неприятные болезненные ощущения в в одном или нескольких суставах, затрагивающие сам сустав, костные структуры, сухожилия или окружающие мягкие ткани. Синоним — артралгия. ");
        values4.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Немедленная медицинская помощь необходима при болях в суставах в сочетании с такими жалобами, как покраснение, отек, повышение местной температуры в области сустава, повреждения кожи с признаками воспаления подкожной клетчатки в непосредственной близости от пораженного сустава, сильная тугоподвижность или неподвижность соответствующего сустава, деформация сустава, изменение походки, хромота, слабость и онемение конечностей ");
        values4.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "В зависимости от количества пораженных суставов, болевой синдром подразделяют на:\n" +
                "•\tмоноартралгии — болевые ощущения локализуются только в одном суставе;\n" +
                "•\tолигоартралгии — боль менее чем в пяти суставах;\n" +
                "•\tполиартралгии — боль больше чем в пяти суставах.\n ");
        values4.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "•\tНаложить лед на болезненный и опухший участок сустава.\n" +
                "•\tОграничить физические нагрузки в пораженном суставе.\n" +
                "•\tКомпрессионная повязка на сустав.\n" +
                "•\tПоднять конечности с поврежденными суставами.\n" +
                "•\tОграничить употребление алкоголя и курение.\n" +
                "•\tПроконсультироваться у специалиста.\n ");
        values4.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tПереохлаждать пораженный сустав.\n" +
                "•\tВыполнять интенсивные физические нагрузки.\n" +
                "•\tЗаниматься самолечением при травмировании сустава.\n ");
        values4.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tРевматическая лихорадка с поражением сердца.\n" +
                "•\tНеспецифические воспалительные заболевания кишечника.\n" +
                "•\tРевматоидный артрит.\n" +
                "•\tОстеоартроз.\n" +
                "•\tПодагра.\n" +
                "•\tСистемная красная волчанка.\n" +
                "•\tПсориатический артрит.\n ");
        Uri newUri4 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values4);

        //list symptom 05
        ContentValues values5 = new ContentValues();
        values5.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Головная боль\n");
        values5.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Головная боль — это неприятные болезненные ощущения в любой части головы, включая волосистую часть кожи головы, верхнюю часть шеи, лицо и внутреннюю часть головы, различной силы и продолжительности. Синоним — цефалгия ");
        values5.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Головная боль не является показанием для оказания экстренной медицинской помощи.\n" +
                "\n" +
                "Немедленная медицинская помощь необходима при головной боли в сочетании с такими жалобами, как ухудшение зрения, покраснение глаз, появление радужных кругов, внезапная слабость, онемение конечностей, потеря координации, дезориентация, невозможность при сгибании головы достать подбородком до грудины (ригидность затылочных мышц), тошнота, рвота ");
        values5.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "Первичные головные боли:\n" +
                "•\tмигрень;\n" +
                "•\tголовная боль напряжения;\n" +
                "•\tкластерная головная боль. ");
        values5.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "•\tРасслабиться, отдохнуть или поспать в темном тихом помещении с приподнятой головой.\n" +
                "•\tНаложить прохладный компресс на голову.\n" +
                "•\tМассаж мышц шеи и плеч.\n" +
                "•\tОтказ от алкоголя, курения и кофеинсодержащих продуктов.\n" +
                "•\tКонсультация специалиста.\n ");
        values5.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tГолодать или пропускать приемы пищи.\n" +
                "•\tИспытывать сильные физические и психоэмоциональные нагрузки.\n" +
                "•\tКурить, употреблять алкоголь и кофеинсодержащие продукты.\n" +
                "•\tНедостаточный или избыточный сон.\n ");
        values5.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tАнемия.\n" +
                "•\tАртериальная гипертензия.\n" +
                "•\tНейроциркуляторная дистония.\n" +
                "•\tМиокардит.\n" +
                "•\tСредний отит.\n" +
                "•\tЛабиринтит.\n" +
                "•\tМастоидит.\n" +
                "•\tСинусит.\n" +
                "•\tГипотиреоз.\n" +
                "•\tГиперкортицизм.\n" +
                "•\tГипертиреоз.\n ");
        Uri newUri5 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values5);

        //list symptom 06
        ContentValues values6 = new ContentValues();
        values6.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Боль в ухе");
        values6.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Боль в ухе — это неприятные болезненные ощущения, передаваемые нервными окончаниями различных частей слухового аппарата (внутреннего уха, ушных раковин и барабанных перепонок), разные по интенсивности и продолжительности. Синоним — оталгия ");
        values6.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Боль в ухе не является показанием для оказания экстренной медицинской помощи.\n" +
                "\n" +
                "Немедленная медицинская помощь необходима при интенсивной, нарастающей боли в ухе, а также в сочетании с такими жалобами, как покраснение и припухлость за ушной раковиной, выраженная отечность в области наружного слухового прохода, потеря слуха, шум в ушах, головокружение, гнойные или кровянистые выделения из наружного слухового прохода ");
        values6.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "Боль в ухе делят на:\n" +
                "•\tпервичную (возникает из-за патологических процессов во внутреннем, среднем или наружном ухе);\n" +
                "•\tвторичную, или иррадиирующую (возникает из-за патологических процессов в других частях тела).\n" +
                "По стороне поражения:\n" +
                "•\tодностороннюю (в одном ухе);\n" +
                "•\tдвустороннюю (в обоих ушах).\n" +
                "По продолжительности:\n" +
                "•\tострую;\n" +
                "•\tхроническую.\n ");
        values6.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "Консультация ЛОР-врача. ");
        values6.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "Прогревать уши.\n" +
                "Без назначений врача закапывать ушные капли.\n" +
                "Пытаться самостоятельно промыть пробку или удалить застрявшее инородное тело из уха. ");
        values6.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tСредний отит.\n" +
                "•\tЛабиринтит.\n" +
                "•\tНаружный отит.\n" +
                "•\tСерная пробка.\n" +
                "•\tФурункул (гнойник) наружного слухового прохода.\n" +
                "•\tРожистое воспаление (рожа) ушной раковины.\n" +
                "•\tОпоясывающий лишай уха (синдром Рамсея — Ханта).\n" +
                "•\tЭкзема (аллергическая, контактная).\n ");
        Uri newUri6 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values6);

        //list symptom 07
        ContentValues values7 = new ContentValues();
        values7.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Головокружение");
        values7.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Головокружение — состояние, при котором человек ощущает либо мнимое вращение тела вокруг пространства, либо вращение окружающего пространства вокруг собственного тела. Также под головокружением понимают состояние дурноты, нечеткости зрения, слабости в ногах и другие симптомы.");
        values7.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Головокружение, как правило, не требует экстренной медицинской помощи. Следует сразу обратиться к врачу, если оно сопровождается резкой головной болью, слабостью в одной половине тела, давящей болью в области груди.");
        values7.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "Системное головокружение (вертиго) — ощущение движения пространства вокруг неподвижного тела либо движения тела вокруг неподвижного пространства.\n" +
                "Несистемное головокружение — чувство дурноты, шаткости, слабости в ногах, нечеткость зрения и т.д. Такое головокружение часто сопровождается эмоциональными реакциями: тревогой, страхом, беспокойством. ");
        values7.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "•\tПринять горизонтальное положение тела.\n" +
                "•\tЕсли приступ застал на улице — сесть, чтобы избежать возможного падения.\n" +
                "•\tОбеспечить приток свежего воздуха.\n" +
                "•\tПостараться сконцентрироваться на одной точке в пространстве.\n" +
                "•\tОграничить действие слуховых и зрительных раздражителей\n ");
        values7.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tКурить.\n" +
                "•\tПытаться продолжать движение.\n" +
                "•\tНосить тесную и стягивающую одежду.\n ");
        values7.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tАнемия.\n" +
                "•\tНейроциркуляторная дистония.\n" +
                "•\tНарушение ритма сердца.\n" +
                "•\tХроническая сердечная недостаточность.\n" +
                "•\tЛабиринтит.\n" +
                "•\tОтосклероз.\n" +
                "•\tБолезнь Меньера.\n" +
                "•\tГипокортицизм.\n ");
        Uri newUri7 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values7);



        //list symptom 09
        ContentValues values9 = new ContentValues();
        values9.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Зубная боль");
        values9.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Зубная боль — это неприятные болезненные ощущения в зубе или окружающих его тканях, различные по распространенности, выраженности и продолжительности болевого синдрома. Синоним — денталгия. ");
        values9.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Немедленная медицинская помощь необходима при зубной боли в сочетании с такими жалобами, как:\n" +
                "•\tинтенсивная головная боль;\n" +
                "•\tлихорадка;\n" +
                "•\tотек или болезненность дна ротовой полости;\n" +
                "•\tзагрудинная боль с распространением в левую руку, нижнюю челюсть, шею;\n" +
                "•\tпотливость, нарастающая слабость;\n" +
                "•\tучащенное сердцебиение;\n" +
                "•\tобморочное состояние;\n" +
                "•\tнарастающая одышка.\n ");
        values9.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "Зубную боль классифицируют как:\n" +
                "•\tсамопроизвольную;\n" +
                "•\tпричинную.\n" +
                "По локализации выделяют:\n" +
                "•\tлокализованную;\n" +
                "•\tобширную;\n" +
                "•\tповерхностную;\n" +
                "•\tглубокую;\n" +
                "•\tиррадиирующую.\n ");
        values9.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "Проконсультироваться у специалиста.\n ");
        values9.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tНагревать больной зуб (исключить из рациона все горячие напитки, не прикладывать горячие компрессы).\n" +
                "•\tПринимать горизонтальное положение.\n" +
                "•\tТрогать больное место руками.\n ");
        values9.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tСинусит.\n" +
                "•\tКариес.\n" +
                "•\tВоспаление сосудисто-нервного пучка зуба (пульпит).\n" +
                "•\tВоспаление связочного аппарата зуба (периодонтит).\n" +
                "•\tНевриты тройничного нерва.\n" +
                "•\tАртриты височно-нижнечелюстного сустава.\n" +
                "•\tОстрое воспаление надкостницы в альвеолярных участках челюстей (периостит, “флюс”).\n" +
                "•\tОстеомиелит.\n ");
        Uri newUri9 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values9);

        //list symptom 10
        ContentValues values10 = new ContentValues();
        values10.put(SymptomEntry.COLUMN_SYMPTOM_NAME, "Зуд в глазах");
        values10.put(SymptomEntry.COLUMN_SYMPTOM_DESCRIPTION, "Зуд в глазах (зуд в области век) сопровождает большое количество офтальмологических заболеваний. Он может возникать по причине воспаления, аллергической реакции или быть симптомом переутомления зрительного аппарата ");
        values10.put(SymptomEntry.COLUMN_SYMPTOM_NEEDHELP, "Экстренная помощь не требуется ");
        values10.put(SymptomEntry.COLUMN_SYMPTOM_CLASIFICATION, "По стороне поражения:\n" +
                "\n" +
                "односторонний (одного глаза);\n" +
                "двусторонний (обоих глаз). ");
        values10.put(SymptomEntry.COLUMN_SYMPTOM_ISRECOMMENDED, "•\tПо возможности минимизировать контакт с провоцирующим фактором.\n" +
                "•\tЗаписаться на прием к аллергологу.\n ");
        values10.put(SymptomEntry.COLUMN_SYMPTOM_ISNOTRECOMMENDED, "•\tЧесать/растирать глаза — это увеличивает риск присоединения бактериальной инфекции.\n" +
                "•\tСамостоятельно подбирать терапию при воспалительных заболеваниях глаза.\n" +
                "•\tПользоваться косметикой на веках, ресницах.\n" +
                "•\tНакладывать повязки на глаза при конъюнктивите.\n ");
        values10.put(SymptomEntry.COLUMN_SYMPTOM_REASONS, "•\tАллергический конъюнктивит.\n" +
                "•\tИнфекционный конъюнктивит.\n" +
                "•\tСиндром сухого глаза.\n" +
                "•\tРеакция на ношение контактных линз (при некачественном уходе).\n" +
                "•\tБлефарит.\n" +
                "•\tГорделоум (ячмень).\n" +
                "•\tХалязион.\n ");
        Uri newUri10 = getContentResolver().insert(SymptomEntry.CONTENT_URI, values10);


    }

    private void deleteAllSymptoms() {
        int rowsDeleted = getContentResolver().delete(SymptomEntry.CONTENT_URI, null, null);
        Log.v("SymptomActivity", rowsDeleted + " rows deleted from table database");
    }
}