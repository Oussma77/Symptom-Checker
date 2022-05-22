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

import com.example.symptomchecker.data.IllnessContracr.IllnessEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IllnessActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the Pharmacy data loader */
    private static final int ILLNESS_LOADER = 0;

    /** Adapter for the ListView */
    IllnessCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illness);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IllnessActivity.this, IllnessEditorActivity.class);
                startActivity(intent);
            }
        });


        // Find the ListView which will be populated with the Pharmacy data
        ListView illnessListView = (ListView) findViewById(R.id.list_illness);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view_illness);
        illnessListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of Pharmacy data in the Cursor.
        // There is no Pharmacy data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new IllnessCursorAdapter(this, null);
        illnessListView.setAdapter(mCursorAdapter);

        illnessListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(IllnessActivity.this, IllnessEditorActivity.class);

                Uri currentlUri = ContentUris.withAppendedId(IllnessEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentlUri);

                // Launch the {@link EditorActivity} to display the data for the current doctor.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(ILLNESS_LOADER, null, this);

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
                insertIllness();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllIllnesses();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                IllnessEntry._ID,
                IllnessEntry.COLUMN_ILLNESS_NAME,
                IllnessEntry.COLUMN_ILLNESS_TYPE,
                IllnessEntry.COLUMN_ILLNESS_DESC,
                IllnessEntry.COLUMN_ILLNESS_CAUSES,
                IllnessEntry.COLUMN_ILLNESS_RISK,
                IllnessEntry.COLUMN_ILLNESS_SYMPTOMS,
                IllnessEntry.COLUMN_ILLNESS_MEDICINES,
                IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                IllnessEntry.CONTENT_URI,   // Provider content URI to query
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

    private void insertIllness() {
        ContentValues values = new ContentValues();
        values.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Болезнь Альцгеймера");
        values.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Нервная система");
        values.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Болезнь Альцгеймера — наиболее распространенная форма приобретенного слабоумия (деменции), являющаяся проявлением длительно текущего нейродегенеративного процесса.");
        values.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "Заболевание получило название в честь немецкого психиатра, Алоиса Альцгеймера, который впервые его описал.\n" +
                "\n" +
                "Существует несколько конкурирующих гипотез, которые объясняют возможные причины развития Альцгеймера. К наиболее популярным относят амилоидную и тау-гипотезу. Общая черта обеих гипотез — накопление патологического белка (бета-амилоида и тау-белка) в головном мозге.\n" +
                "\n" +
                "В норме в здоровом организме постоянно образуется бета-амилоид. Существует ряд специальных механизмов, благодаря которым избыточный амилоид разрушается. Вследствие не до конца выясненных причин данный механизм может нарушиться. В результате формируются амилоидные бляшки.");
        values.put(IllnessEntry.COLUMN_ILLNESS_RISK, "•\tПациенты пожилого возраста;\n" +
                "•\tте, чьи близкие родственники страдают БА;\n" +
                "•\tпациенты с полиморфизмом гена ApoE (гена аполипротеина Е);\n" +
                "•\tстрадающие синдромом Дауна;\n" +
                "•\tстрадающие умеренными когнитивными нарушениями;\n" +
                "•\tперенесшие тяжелую черепно-мозговую травму;\n" +
                "•\tпациенты с нарушениями сна;\n" +
                "•\tстрадающие ожирением, артериальной гипертензией, сахарным диабетом, с повышенным уровнем холестерина.\n");
        values.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "В течение БА выделяют 3 стадии:\n" +
                "\n" +
                "Бессимптомная (доклиническая БА) — в этой стадии не наблюдается никаких симптомов.\n" +
                "\n" +
                "•\tСтадия умеренных когнитивных расстройств (умеренная БА) характеризуется ухудшением познавательных функций, нарушением когнитивных (отвечающих за восприятие и анализ информации) функций и отсутствием деменции:\n" +
                "•\tухудшение памяти;\n" +
                "•\tсложность организации мыслей и логического мышления;\n" +
                "•\tнеузнавание знакомых мест, трудности с ориентацией;\n" +
                "•\tнеузнавание друзей и членов семьи;\n" +
                "•\tзатруднения с речью, проблемы с чтением, письмом, работой с числами;\n" +
                "•\tвыполнение ежедневных дел занимает больше времени, чем ранее;\n" +
                "•\tзатруднения при оплате счетов и взаимодействии с деньгами;\n");
        values.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "•\tИнгибиторы холинэстеразы — группа препаратов, которые блокируют фермент, разрушающий ацетилхолин. Это молекула связи между нейронами. Предотвращение разрушения и накопление ацетилхолина улучшает работу нейронов.\n" +
                "•\tАнтагонист N-метил D-аспартат(NMDA)-рецепторов — препарат мемантина гидрохлорид. Мемантин подавляет избыточную активацию NMDA-рецепторов.\n" +
                "•\tАнтидепрессанты и анксиолитики — вторично на фоне БА всегда развивается депрессия и тревожность. При оценке психического статуса определяется целесообразность назначения этих препаратов.\n" +
                "•\tПротивопаркинсонические препараты при БА применяют для коррекции поведенческих расстройств.\n");
        values.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tКакова причина моих симптомов?\n" +
                "•\tКак быстро будут прогрессировать симптомы?\n" +
                "•\tЧто нужно делать, чтобы симптомы развивались медленнее?\n" +
                "•\tМне когда-нибудь понадобится помощь в повседневной жизни?\n" +
                "•\tМожет ли БА быть у моих родственников?\n");
        Uri newUri = getContentResolver().insert(IllnessEntry.CONTENT_URI, values);

        //list illness 2
        ContentValues values2 = new ContentValues();
        values2.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Головная боль напряжения");
        values2.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Нервная система");
        values2.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Головная боль напряжения (ГБН) — это давящая или сжимающая боль умеренной интенсивности, которая локализуется на всей поверхности головы. Ведущим фактором развития является эмоциональный стресс.");
        values2.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "Эмоциональный стресс приводит к повышению тонуса перикраниальных мышц (мышц головы и шеи). Избыточный тонус мышц раздражает и активирует поверхностные болевые рецепторы, приводя к сенситизации (повышенной болевой чувствительности). В результате ранее не ощущаемые раздражители начинают восприниматься пациентом как болевые.\n" +
                "\n" +
                "Длительный тонический спазм мускулатуры приводит к ухудшению кровоснабжения и выделению медиаторов воспаления, что еще больше усугубляет болевой синдром");
        values2.put(IllnessEntry.COLUMN_ILLNESS_RISK, "Факторы риска ГБН:\n" +
                "•\tженский пол;\n" +
                "•\tдлительное пребывание в состоянии стресса;\n" +
                "•\tхроническое недосыпание;\n" +
                "•\tсидячая работа;\n" +
                "•\tфизическое утомление;\n" +
                "•\tработа, связанная с повышенной концентрацией внимания\n");
        values2.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "Боль при головной боли напряжения двухсторонняя, давящая или сжимающая. Чаще всего болевой синдром возникает во второй половине дня после пережитого стресса или утомительной работы, однако может присутствовать и с утра. Переключение внимания на другие раздражители может приводить к снижению болевых ощущений. Физическая нагрузка не приводит к усилению боли.\n" +
                "\n" +
                "Неярко выраженная фотобоязнь и звукобоязнь может быть при высокой интенсивности боли, тошнота и рвота не характерны. Может ощущаться напряжение мышц шеи, тяжесть, повышенная чувствительность при прикосновениях к коже головы.");
        values2.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "•\tНестероидные противовоспалительные препараты (НПВП) .\n" +
                "•\tМиорелаксанты снижают напряжение перикраниальных мышц. \n" +
                "•\tАнтидепрессанты.\n" +
                "•\tАнксиолитики используются при тревоге и нарушении сна. \n");
        values2.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tКак отличить головную боль напряжения от других видов головной боли?\n" +
                "•\tКак изменить образ жизни, чтобы чувствовать себя лучше?\n" +
                "•\tКакие методы лечения могут мне помочь?\n" +
                "•\tКакие существуют осложнения ГБН и вероятно ли их развитие в моем случае?\n");
        Uri newUri2 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values2);

        //list illness 3
        ContentValues values3 = new ContentValues();
        values3.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Миопия");
        values3.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Глаза ");
        values3.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Миопия, или близорукость, — это расстройство зрения, когда свет фокусируется перед сетчаткой, а не на ней. Это приводит к тому, что удаленные объекты становятся размытыми, в то время как близкие объекты видны хорошо.");
        values3.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "Оптическая система глаза представлена:\n" +
                "•\tводянистой влагой — желеобразная жидкость, заполняющая переднюю и заднюю камеры глаза;\n" +
                "•\tроговицей, или роговой оболочкой, — прозрачная куполообразная передняя часть глазного яблока;\n" +
                "•\tхрусталик — двояковыпуклая линза, расположенная внутри глазного яблока;\n" +
                "•\tстекловидное тело — гелеобразное вещество, заполняющее пространство между хрусталиком и сетчаткой.\n");
        values3.put(IllnessEntry.COLUMN_ILLNESS_RISK, "Подростки и взрослые, у кого один или оба родителя страдают от миопии. Наследуется не сама миопия, а физиологическая предрасположенность к ней, включая размер глазного яблока или кривизну хрусталика.\n" +
                "Начиная с детского возраста все, кто:\n" +
                "•\tмного сидит за компьютером;\n" +
                "•\tиспользует максимальную освещенность монитора и высокую контрастность;\n" +
                "•\tчитает при плохом освещении, лежа или в движущемся транспорте;\n" +
                "•\tнеправильно сидит за рабочим столом во время чтения и письма;\n" +
                "•\tпостоянно находится в плохо освещенном помещении.\n");
        values3.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "Основной симптом миопии — размытость отдаленных предметов. При миопии высокой степени дальность видения может ограничиваться 10 см.\n" +
                "Выделяют три степени миопии:\n" +
                "\n" +
                "•\tслабую (до 3,0 диоптрий (D);\n" +
                "•\tсреднюю (от 3,25 до 6,0 D);\n" +
                "•\tвысокую (более 6 D).\n" +
                "•\tДругие симптомы:\n" +
                "головная боль;\n" +
                "•\tнапряжение глаз;\n" +
                "•\tдети с миопией часто испытывают проблемы с учебой, потому что не видят написанное на доске;\n" +
                "•\tпри миопии высокой степени пациенты могут жаловаться на появление “мушек” перед глазами.\n");
        values3.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "Капли в глаза при миопии назначают для снятия спазма ресничной мышцы и устранения симптомов синдрома усталых глаз обычно на срок не более месяца и после курса лечения снова проводят измерение остроты зрения, чтобы мышечный спазм не маскировал истинную миопию.\n" +
                "\n" +
                "При тяжелой прогрессирующей миопии могут развиваться дистрофических процессы, снижается поступление кислорода к тканям и на этом фоне запускается процесс патологического роста сосудов. Для лечения данного состояния применяют препараты, которые блокируют фактор роста эндотелия сосудов.");
        values3.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tМожно ли избежать миопии у ребенка, если она есть у родителей?\n" +
                "•\tВлияет ли курение на миопию?\n" +
                "•\tКакие методы лечения существуют?\n" +
                "•\tМогут ли очки ухудшить миопию?\n" +
                "•\tКак избежать осложнений?\n");
        Uri newUri3 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values3);

        //list illness 4
        ContentValues values4 = new ContentValues();
        values4.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Гиперметропия");
        values4.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Глаза ");
        values4.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Гиперметропия — медицинский термин для обозначения дальнозоркости. При данной патологии луч света фокусируется не на сетчатке, а за ней, в результате ухудшается видимость предметов на близком расстоянии");
        values4.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "Для формирования изображения луч света должен пройти через оптические среды глаза и сфокусироваться на сетчатке. Основные органы глаза, которые преломляют свет, — роговица и хрусталик. Их выпуклая форма позволяет правильно рассеивать световые лучи, чтобы они фокусировались ровно на сетчатке.\n" +
                "\n" +
                "При гиперметропии роговица более плоская или осевая длина глазного яблока слишком короткая, поэтому изображения не фокусируются, когда они достигают сетчатки, а луч света проходит за сетчатку. Для ясного зрения гиперметропический глаз должен приспосабливаться, чтобы увеличить свою фокусировочную способность. Это требует сокращения ресничной мышцы, и поэтому дальнозоркий глаз никогда не отдыхает и работает еще интенсивнее, чтобы лучше видеть близкие объекты.\n" +
                "\n" +
                "Различают врожденную гиперметропию, которая связана с генетически обусловленным коротким глазным яблоком или уплощенной роговицей. Такой вид гиперметропии будет проявляться еще с детства.");
        values4.put(IllnessEntry.COLUMN_ILLNESS_RISK, "•\tБлизкие родственники людей с гиперметропией;\n" +
                "•\tлюди старше 45 лет;\n" +
                "•\tдети, чьи матери курили во время беременности; согласно последним исследованиям, даже пассивное курение, когда беременная женщина часто попадает под действие сигаретного дыма, повышает риск развития гиперметропии у детей;\n" +
                "•\tпациенты с катарактой;\n" +
                "•\tте, у кого была травма глаза;\n" +
                "•\tпациенты с хроническими кератитами;\n" +
                "•\tпациенты с сахарным диабетом.\n");
        values4.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "Выделяют три степени гиперметропии:\n" +
                "•\tслабая гиперметропия — до 2 диоптрий;\n" +
                "•\tсредняя гиперметропия — от 2 до 5 диоптрий;\n" +
                "•\tвысокая гиперметропия — 5,25 и более диоптрий.\n" +
                "Основной симптом — расплывчатое видение предметов вблизи.\n" +
                "Другие симптомы могут возникнуть при любой степени дальнозоркости:\n" +
                "•\tнапряжение в глазах;\n" +
                "•\tголовная боль;\n" +
                "•\tумеренная светочувствительность;\n" +
                "•\tкраснота глаз;\n" +
                "•\tумеренное слезотечение;\n" +
                "•\tчастое моргание.");
        values4.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "Лекарственная терапия неэффективна в лечении гиперметропии.\n" +
                "\n" +
                "Для подбора очков назначают глазные капли для создания циклоплегии — полного паралича ресничной мышцы. Это необходимо для оценки оптических сред глаза при полностью расширенном зрачке.");
        values4.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tЧто приводит к возникновению гиперметропии?\n" +
                "•\tМожет ли гиперметропия у ребенка пройти без лечения?\n" +
                "•\tКакие методы лечения существуют?\n" +
                "•\tС какого возраста можно проводить лазерное лечение?\n" +
                "•\tКак избежать осложнений?");
        Uri newUri4 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values4);

        //list illness 5
        ContentValues values5 = new ContentValues();
        values5.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Туберкулез");
        values5.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Дыхательная система");
        values5.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Туберкулез — это инфекционное, специфическое, хроническое заболевание, вызываемое микобактериями туберкулеза.");
        values5.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "Заболевание вызывают микобактерии туберкулезного комплекса — M. tuberculosis (палочка Коха), M. africanum, M. microti, M. canetti, M. pinnipedii, M. caprae, M. bovis, M. bovis BCG, — которые обладают высокой устойчивостью во внешней среде, постоянной изменчивостью, медленно размножаются в организме и способны вызывать специфическое воспаление");
        values5.put(IllnessEntry.COLUMN_ILLNESS_RISK, "•\tЛица, контактировавшие с больными туберкулезом;\n" +
                "•\tлица, проживающие в эндемических регионах по отношению к туберкулезной инфекции;\n" +
                "•\tлица с ВИЧ-инфекцией, онкологическими заболеваниями;\n" +
                "•\tпациенты, принимающие препараты, подавляющие иммунитет (гормоны, биологические препараты);\n" +
                "•\tлица с хроническими заболеваниями легких;\n" +
                "•\tпациенты, находящиеся на гемодиализе;\n" +
                "•\tпациенты с сахарным диабетом;\n" +
                "•\tупотребляющие внутривенные наркотики;\n" +
                "•\tлица с недостаточным питанием;\n" +
                "•\tстрадающие алкоголизмом;\n" +
                "•\tкурящие лица.\n");
        values5.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "Инкубационный период туберкулеза составляет от 7 до 12 недель, но бывают случаи, когда он может длиться несколько лет.\n" +
                "\n" +
                "К основным симптомам туберкулеза легких относят кашель — изначально сухой, позже более продуктивный продолжительностью от 2-3 недель, температура тела повышается в пределах 37,1-38,0 °C. До 20 % пациентов не имеют повышения температуры.");
        values5.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "Химиотерапия противотуберкулезными препаратами — основная часть лечения туберкулеза, заключается в длительном применении лекарств, подавляющих размножение или уничтожающих в организме микобактерии туберкулеза. Вид химиотерапии выбирается с учетом данных заболевания и лекарственной устойчивости выделенного возбудителя.");
        values5.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tЧто такое туберкулез?\n" +
                "•\tКакие факторы повышают риск развития туберкулеза?\n" +
                "•\tКакие органы может поражать туберкулез?\n" +
                "•\tКаковы признаки и симптомы туберкулеза легких?\n" +
                "•\tКакие существуют методы скринингового обследования на туберкулез?\n");
        Uri newUri5 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values5);

        //list illness 6
        ContentValues values6 = new ContentValues();
        values6.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Пневмония");
        values6.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Дыхательная система");
        values6.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Пневмония — это острое воспаление легочной ткани с преимущественным поражением альвеол (легочные структуры в форме пузырьков, в которых происходит газообмен между воздухом и кровью), проявляющееся лихорадкой и кашлем");
        values6.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "Причины возникновения пневмонии:\n" +
                "•\tбактериями (пневмококк, стрептококк, стафилококк);\n" +
                "•\tвирусами (вирус гриппа, парагриппа, респираторно-синцитиальный вирус);\n" +
                "•\tгрибами или паразитами (редко).");
        values6.put(IllnessEntry.COLUMN_ILLNESS_RISK, "•\tДети младше 2 лет и пожилые люди старше 65. Это связано с незрелостью или истощением иммунной защиты дыхательных путей;\n" +
                "•\tпациенты больниц, которые долго лежат в стационаре\n");
        values6.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "Первыми симптомами при пневмонии могут быть:\n" +
                "•\tлихорадка свыше 38 °С;\n" +
                "•\tострый кашель;\n" +
                "•\tодышка;\n" +
                "•\tболь в грудной клетке;\n" +
                "•\tвыделение мокроты с кашлем;\n" +
                "•\tучащенное сердцебиение;\n" +
                "•\tсинюшность конечностей;\n" +
                "•\tслабость, утомляемость.\n");
        values6.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "Для разных возбудителей пневмонии есть своя терапия.\n" +
                "•\tАнтимикробные препараты.\n" +
                "•\tПротивовирусные препараты .\n" +
                "•\tЖаропонижающие препараты.\n" +
                "•\tМукоактивные средства .\n");
        values6.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tМеня госпитализируют или я смогу лечиться на дому?\n" +
                "•\tУ меня есть хронические заболевания. Это повлияет на течение и исход пневмонии?\n" +
                "•\tКакие признаки указывают на то, что состояние ухудшается?\n");
        Uri newUri6 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values6);

        //list illness 7
        ContentValues values7 = new ContentValues();
        values7.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Бронхиальная астма\n");
        values7.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Дыхательная система");
        values7.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Бронхиальная астма — это заболевание легких, которое проявляется приступами удушья. В его основе лежит сужение бронхов в ответ на разнообразные раздражители: аллергены, химические вещества, холод, физическую нагрузку. Астма может поражать как взрослых, так и детей и часто связана с аллергией");
        values7.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "•\tаллергены — клещи домашней пыли, шерсть домашних животных, тараканы, пыльца растений, дрожжевые и плесневые грибки, ряд пищевых продуктов;\n" +
                "•\tхимические вещества — сажа, копоть, дым, выхлопные газы и др.;\n" +
                "•\tнекоторые лекарства — аспирин (аспириновая астма), НПВС;\n" +
                "•\tхолод;\n" +
                "•\tфизическая нагрузка.\n");
        values7.put(IllnessEntry.COLUMN_ILLNESS_RISK, "•\tЛюди, у которых есть родственники с любыми болезнями аллергической природы;\n" +
                "•\tсреди детей — мальчики, в молодом и взрослом возрасте — девушки и женщины;\n" +
                "•\tрабочие вредных производств;\n" +
                "•\tжители городов — влажность, копоть и выхлопные газы;\n" +
                "•\tкурильщики, в том числе и пассивные.\n");
        values7.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "Обычно астма проявляется приступами сильного удушья, которые начинаются, когда в бронхи попадает раздражающее вещество: аллергены, пыль, дым, а также после физической нагрузки и любого другого пускового фактора.\n" +
                "\n" +
                "Во время приступа человек ощущает резкую нехватку воздуха, ему трудно сделать выдох, губы синеют, он старается сесть, оперевшись руками на что-нибудь. Свистящие хрипы слышно на расстоянии. Удушье может длиться от 10 минут до получаса и более, но постепенно проходит, после чего появляется кашель с мокротой, свидетельствующий о завершении приступа.\n" +
                "\n" +
                "В промежутках между приступами никаких симптомов не наблюдается.\n" +
                "\n" +
                "У детей астма может протекать в кашлевой форме, без приступов, ограничиваясь навязчивым сухим кашлем.");
        values7.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "•\tоснова ступенчатой схемы — текущий уровень контроля за болезнью и объем получаемой терапии;\n" +
                "•\tесли контроль с помощью проводимого лечения не достигнут, то оно должен быть повышен “на ступень вверх” (step up);\n" +
                "•\tпри хорошем контроле за заболеванием в течение как минимум трех месяцев возможно снижение объема терапии “на ступень вниз” (step down).\n");
        values7.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tКакова вероятность развития бронхиальной астмы у меня / моего ребенка?\n" +
                "•\tКакие исследования необходимо выполнить, чтобы уточнить причину бронхиальной астмы?\n" +
                "•\tНужно ли пройти обследование родственникам?\n" +
                "•\tКак я могу изменить образ жизни, чтобы предотвратить развитие приступов бронхиальной астмы?\n" +
                "•\tКакое ингаляционное устройство мне подходит?\n" +
                "•\tКак правильно пользоваться ингалятором?\n");
        Uri newUri7 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values7);

        //list illness 8
        ContentValues values8 = new ContentValues();
        values8.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Болезнь Меньера");
        values8.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Ухо, горло, нос ");
        values8.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Болезнь Меньера — это заболевание, поражающее внутреннее ухо. Патогенез болезни изучен недостаточно, предполагают, что в основе лежит патологическое увеличение объема эндолимфы — жидкости, заполняющей перепончатый лабиринт; это приводит к разрыву его оболочек, что проявляется симптоматикой нарушения функции органов слуха и равновесия.");
        values8.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "Не существует общепризнанной теории, объясняющей механизм возникновения болезни Меньера. Предполагают, что заболевание генетически обусловлено, так как существует закономерность в его развитии у близких родственников и у них обнаруживается специфическая мутация. Также в числе предполагаемых факторов, вызывающих это заболевание, называют аутоиммунные процессы, нарушение анатомии височной кости, строения сосудов, обмена ионов в эндолимфе и перилимфе (жидкость, заполняющая пространство между костным и перепончатым лабиринтом внутреннего уха).");
        values8.put(IllnessEntry.COLUMN_ILLNESS_RISK, "•\tНаличие родственников первой линии с этим заболеванием (родители, братья, сестры);\n" +
                "•\tперенесенные травмы или инфекции среднего уха;\n" +
                "•\tмигрень — отмечают частое сочетание с болезнью Меньера;\n" +
                "•\tаллергия;\n" +
                "•\tаутоиммунные заболевания.\n");
        values8.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "•\tПриступы системного головокружения, длящиеся от 20 минут до 12 часов;\n" +
                "•\tнарушения слуха;\n" +
                "•\tтиннитус (беспричинный шум в ушах);\n" +
                "•\tощущение заложенности уха.\n");
        values8.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "•\tДиуретики .\n" +
                "•\tГистаминомиметики .\n" +
                "•\tГлюкокортикостероиды .\n" +
                "•\tГентамицин");
        values8.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "•\tКакие меры предпринимать при возникновении приступа?\n" +
                "•\tЧем может быть вызвано мое состояние?\n" +
                "•\tПоказано ли мне хирургическое лечение?\n" +
                "•\tКакие меры предпринять, чтобы снизить частоту развития приступов?\n" +
                "•\tКаков прогноз для моей трудоспособности? Для слуховой функции?\n");
        Uri newUri8 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values8);

        //list illness 09
        ContentValues values9 = new ContentValues();
        values9.put(IllnessEntry.COLUMN_ILLNESS_NAME, "Ларингит");
        values9.put(IllnessEntry.COLUMN_ILLNESS_TYPE, "Ухо, горло, нос ");
        values9.put(IllnessEntry.COLUMN_ILLNESS_DESC, "Ларингит — это воспаление гортани. Оно развивается на слизистых оболочках гортани и голосовых связок, вызывая их отек.");
        values9.put(IllnessEntry.COLUMN_ILLNESS_CAUSES, "•\tПереохлаждение;\n" +
                "•\tинфекция — бактериальная или вирусная;\n" +
                "•\tперенапряжение голосовых связок;\n" +
                "•\tповреждения химическими, термическими или другими раздражителями;\n" +
                "•\tаллергия.");
        values9.put(IllnessEntry.COLUMN_ILLNESS_RISK, "•\tДети;\n" +
                "•\tкурящие;\n" +
                "•\tте, чья деятельность подразумевает нагрузку на голосовые связки;\n" +
                "•\tконтактирующие с раздражающими веществами (пыль, химикаты);\n" +
                "•\tживущие в условиях резких перепадов температур.");
        values9.put(IllnessEntry.COLUMN_ILLNESS_SYMPTOMS, "Сухой кашель, боль в горле, осиплость голоса. Если заболевание было спровоцировано инфекцией, то возможны повышение температуры, головная боль, ломота в мышцах. Очень тяжелое течение заболевания может сопровождаться одышкой");
        values9.put(IllnessEntry.COLUMN_ILLNESS_MEDICINES, "антибактериальные препараты — местные (для слизистой гортани) и системные (в таблетках и уколах — при выраженной интоксикации, неэффективности местного лечения); чаще всего используют грамицидин, амоксиклав, макролиды, фторхинолоны;\n" +
                "антигистаминовые препараты — для устранения аллергической реакции (дифенгидрамин, циметидин, клемастил);\n" +
                "противовоспалительные средства — как правило, применяются ингаляции глюкокортикостероидов;\n" +
                "муколитики — средства, меняющие химические свойства слизи, делающие ее более жидкой и способствующие ее отхождению (амброксан, АЦЦ, бромгексин), чаще всего вводятся ингаляцией.");
        values9.put(IllnessEntry.COLUMN_ILLNESS_ASKDOCTOR, "Какие меры предпринять, чтобы заболевание не прогрессировало?\n" +
                "Когда и насколько у меня восстановится голос?\n" +
                "Нужны ли какие-либо реабилитационные мероприятия после выздоровления?\n" +
                "Какова вероятность развития осложнений?\n");
        Uri newUri9 = getContentResolver().insert(IllnessEntry.CONTENT_URI, values9);

    }

    private void deleteAllIllnesses() {
        int rowsDeleted = getContentResolver().delete(IllnessEntry.CONTENT_URI, null, null);
        Log.v("IllnessActivity", rowsDeleted + " rows deleted from table database");
    }

}