# AndroidStudio
there are no words


Библиотеки:------------------------------------------------------------------------------------------------------------------------------------------------------
    implementation "androidx.room:room-runtime:2.3.0"
    annotationProcessor "androidx.room:room-compiler:2.3.0"

    implementation 'com.google.code.gson:gson:2.8.8'
    implementation 'com.squareup.retrofit2:retrofit:2.7.1'
    implementation 'com.squareup.retrofit2:converter-scalars:2.7.2'
    implementation 'com.squareup.retrofit2:converter-gson:2.7.2'

    implementation 'androidx.navigation:navigation-fragment:2.5.2'
    implementation 'androidx.navigation:navigation-ui:2.5.2'

    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'androidx.recyclerview:recyclerview-selection:1.1.0'

 buildFeatures {
        viewBinding true
    }

BottomNavigation:-------------------------------------------------------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);
        replaceFragment(new BlankFragmentHome());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        binding.navView.setOnItemSelectedListener(item->{

            switch (item.getItemId()){
                case R.id.home:{
                    replaceFragment(new BlankFragmentHome());
                }break;
                case R.id.sound:{
                    replaceFragment(new BlankFragmentSound());
                }break;
                case R.id.profile:{
                    replaceFragment(new BlankFragmentProfile());
                }break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

}

RecyclerView:-----------------------------------------------------------------------------------------------------------------------------------------------------
        // Создание RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView = root.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
Adapter:---------------------------------------------------------------------------------------------------------------------------------------------------------

package com.example.thirdproject.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirdproject.App;
import com.example.thirdproject.R;
import com.example.thirdproject.data.Session;
import com.example.thirdproject.database.AppDatabase;
import com.example.thirdproject.database.DbSaveTown;
import com.example.thirdproject.database.DbTown;
import com.example.thirdproject.database.DbSaveTownDao;
import com.example.thirdproject.database.DbTownDao;

import java.util.List;

public class HomeTownsRecyclerViewAdapter extends RecyclerView.Adapter<HomeTownsRecyclerViewAdapter.MyViewHolder> {
    private static List<DbTown> mDataset; // Список городов для RecyclerView
    private Context context; // Контекст

    // Внутренний класс, предназначенный для хранения информации о View-элементах списка
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName;
        //public TextView mTextViewCountry;
        //public TextView mTextViewPopulation;
        public MyViewHolder(View v) {
            super(v);
            mTextViewName = v.findViewById(R.id.textViewName);
           // mTextViewCountry = v.findViewById(R.id.textViewCountry);
            //mTextViewPopulation = v.findViewById(R.id.textViewPopulation);

            // Обработчик нажатия на элемент списка
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle myBundle = new Bundle();

                    AppDatabase dbRoom = App.getInstance().getDatabase();
                    DbTownDao dbTownDao = dbRoom.dbTownDao();
                    List<DbTown> townsList = dbTownDao.getTowns();

                    for (int i = 0; i < townsList.size(); i++) {
                        if (townsList.get(i).name.equals(mTextViewName.getText())) {
                            myBundle.putString("town_name", townsList.get(i).name);
                            myBundle.putString("town_country",  townsList.get(i).country);
                            myBundle.putInt("town_population",townsList.get(i).population);
                            myBundle.putString("town_language",townsList.get(i).language);
                            myBundle.putInt("town_square", townsList.get(i).square);
                        }
                    }

                    Navigation.findNavController(view).navigate(R.id.nav_town, myBundle, new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_home, true)
                            .build()
                    );
                }
            });

            // Обработчик долгого нажатия на элемент списка
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("MyRecyclerViewAdapter", "onLongClick");

                    // Создание диалогового окна
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true)
                            .setIcon(R.drawable.ic_baseline_home_24)
                            .setMessage("Вы действительно хотите добавить город " + mTextViewName.getText() + "?")
                            .setTitle("Добавление города")
                            .setPositiveButton("В избранное", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("AlertDialog", "Да");

                                    Session session = Session.getInstance();

                                    AppDatabase dbRoom = App.getInstance().getDatabase();
                                    DbTownDao dbTownDao = dbRoom.dbTownDao();
                                    DbSaveTownDao dbSaveTownDao = dbRoom.dbSaveTownDao();

                                    //saveTownDao.deleteSaveTowns();

                                    DbTown SelectedTown = dbTownDao.getTownByName(mTextViewName.getText().toString());
                                    List<DbSaveTown> userDbSaveTowns = dbSaveTownDao.getSaveTownsByUserId(session.getId());

                                    for (int i = 0; i < userDbSaveTowns.size(); i++) {
                                        if (userDbSaveTowns.get(i).town_id == SelectedTown.id) {
                                            Toast.makeText(context, "Город уже добавлен в избранное", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    DbSaveTown dbSaveTown = new DbSaveTown();
                                    dbSaveTown.town_id = SelectedTown.id;
                                    dbSaveTown.user_id = session.getId();
                                    dbSaveTownDao.insertSaveTown(dbSaveTown);

                                    Toast.makeText(context, "Город добавлен в избранное", Toast.LENGTH_SHORT).show();

                                    List<DbSaveTown> st = dbSaveTownDao.getSaveTowns();
                                    for (int i = 0; i < st.size(); i++) {
                                        Log.i("MyRecyclerViewAdapter", st.get(i).id + " " + st.get(i).town_id + " " + st.get(i).user_id);
                                    }
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("AlertDialog", "Нет");
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
                }
            });
        }
    }

    public HomeTownsRecyclerViewAdapter(List<DbTown> myDataset) { mDataset = myDataset; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return  vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextViewName.setText(mDataset.get(position).name);
        //holder.mTextViewCountry.setText(mDataset.get(position).country);
        //holder.mTextViewPopulation.setText(mDataset.get(position).population.toString());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


SheredPreference:-------------------------------------------------------------------------------------------------------------------------------------------------
Write:
SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
SharedPreferences.Editor editor = preferences.edit();
editor.putString("Authentication_Id",userid.getText().toString());
editor.putString("Authentication_Password",password.getText().toString());
editor.putString("Authentication_Status","true");
editor.apply();
  
Read:
SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
String Astatus = prfs.getString("Authentication_Status", "");

Retrofit:---------------------------------------------------------------------------------------------------------------------------------------------------------
  
  
   //--------------- Получение данных с Api ---------------
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                TownService service = retrofit.create(TownService.class);
                Call<List<Town>> getTownsRequest = service.getTowns();

                // Выполение веб-запроса
                // .execute() - синхронный
                // .enqueue() - асинхронный
                getTownsRequest.enqueue(new Callback<List<Town>>() {
                    @Override
                    public void onResponse(Call<List<Town>> call, Response<List<Town>> response) {
                        if (response.isSuccessful()) {
                            // Обработка ответа
                            Log.i("[onResponse] call = ", call.toString());
                            Log.i("[onResponse] response = ", response.toString());

                            mTownsData = response.body();

                            // Заполнение БД
                            if (dbTownsList.size() == 0) {
                                for (Town townData : mTownsData) {
                                    DbTown town = new DbTown();
                                    town.country = townData.country;
                                    town.population = townData.population;
                                    town.language = townData.language;
                                    town.name = townData.name;
                                    town.square = townData.square;
                                    dbTownDao.insertTown(town);
                                }
                            }
                            else {
                                for (int i = 0; i < mTownsData.size(); i++) {
                                    for (int j = 0; j < dbTownsList.size(); j++) {
                                        if (dbTownsList.get(j).name.equals(mTownsData.get(i).name)) {
                                            break;
                                        }
                                        if (j == dbTownsList.size() - 1) {
                                            DbTown town = new DbTown();
                                            town.country = mTownsData.get(i).country;
                                            town.population = mTownsData.get(i).population;
                                            town.language = mTownsData.get(i).language;
                                            town.name = mTownsData.get(i).name;
                                            town.square = mTownsData.get(i).square;
                                            dbTownDao.insertTown(town);
                                        }
                                    }
                                }
                            }

                            dbTownsList = dbTownDao.getTowns();
                            for (DbTown town : dbTownsList) {
                                Log.i("DbTowns", town.name + " " + town.country + " " + town.population + " " + town.language + " " + town.square);
                            }

                            // Добавление объектов в RecyclerView
                            if (dbTownsList.size() != 0) {
                                mRecyclerData.clear();
                                mRecyclerData.addAll(dbTownsList);
                                mAdapter = new HomeTownsRecyclerViewAdapter(mRecyclerData);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            else {
                                Toast.makeText(getContext(), "Нет данных!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            ResponseBody errorBody = response.errorBody();
                            Toast.makeText(getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Town>> call, Throwable t) {
                        // Обработка ошибок
                        Log.i("[onFailure] t = ", t.toString());
                        Toast.makeText(getContext(), "Ошибка получения данных!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
  
  Класс запросов: -----------------------------------------------------------------------
  
package com.example.thirdproject.data;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

import com.example.thirdproject.data.Town;

public interface TownService {
    @GET("City2022.json")
    Call<List<Town>> getTowns();
}
  
  
GrideView:------------------------------------------------------------------------------------------------------------------------------------------------------
  public class HomePage extends Activity  {
    private ArrayList<SingleElementDetails> allElementDetails=new ArrayList<SingleElementDetails>();
    DBAdapter db=new DBAdapter(this);
    String category, description;
    String data;
    String data1;
    GridView gridview;
    Button  menu;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        menu=(Button)findViewById(R.id.menus);

        menu.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                gridview=(GridView)findViewById(R.id.gridview);
                allElementDetails.clear();
                db.open();
                long id;
                //id=db1.insertTitle1(category, description,r_photo);
                Cursor cursor = db.getAllTitles1();
                while (cursor.moveToNext())
                {
                    SingleElementDetails single=new SingleElementDetails();
                    single.setCateogry(cursor.getString(1));
                    single.setDescription(cursor.getString(2));
                    single.setImage(cursor.getBlob(3));
                    allElementDetails.add(single);
                }
                db.close();
                CustomListAdapter adapter=new CustomListAdapter(HomePage.this,allElementDetails);
                gridview.setAdapter(adapter);
            }
        });
    }
}
  
  GrideViewAdapter:-------------------------------------------------------------------------------------------------------------------------------------------
  import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    private  ArrayList<SingleElementDetails> allElementDetails;

    private LayoutInflater mInflater;

    public CustomListAdapter(Context context, ArrayList<SingleElementDetails> results) {
        allElementDetails = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return allElementDetails.size();        
    }

    public Object getItem(int position) {
        return allElementDetails.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) 
    {
        convertView = mInflater.inflate(R.layout.listview1, null);
        ImageView imageview = (ImageView) convertView.findViewById(R.id.image);
        TextView textview = (TextView) convertView.findViewById(R.id.category_entry);
        TextView textview1 = (TextView) convertView.findViewById(R.id.description_entry);
        textview.setText(allElementDetails.get(position).getCategory());
        textview1.setText(allElementDetails.get(position).getDescription());

        byte[] byteimage=allElementDetails.get(position).getImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(byteimage);
        BitmapFactory.Options op=new BitmapFactory.Options();
        op.inSampleSize=12;
        Bitmap theImage= BitmapFactory.decodeStream(imageStream,null,op);
        imageview.setImageBitmap(theImage);
        return convertView;
    }
}
