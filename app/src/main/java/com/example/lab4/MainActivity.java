package com.example.lab4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import db.DbContext;
import api.ApiTask;
import db.Trak;


public class MainActivity extends AppCompatActivity {

    private ScheduledExecutorService scheduler;  // Объявляем scheduler
    private DbContext dbContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Trak trak1 = new Trak();
        Trak trak2 = new Trak();
        Trak trak3 = new Trak();
        Trak trak4 = new Trak();
        Trak trak5 = new Trak();
        Trak trak6 = new Trak();
        setContentView(R.layout.activity_main);
        trak1.setTitle("Lights Camera Action");
        trak1.setExecutor("Kylie Minogue");
        trak1.setDate("04.12.2024");
        trak2.setTitle("Glow In The Dark");
        trak2.setExecutor("Tom Gregory");
        trak2.setDate("04.12.2024");
        trak3.setTitle("A Bar Song (Tipsy)");
        trak3.setExecutor("Shaboozey");
        trak3.setDate("04.12.2024");
        trak4.setTitle("I Adore You (feat. Daecolm)");
        trak4.setExecutor("HUGEL & Topic & Arash");
        trak4.setDate("04.12.2024");
        trak5.setTitle("Spot a Fake");
        trak5.setExecutor("Ava Max");
        trak5.setDate("04.12.2024");
        trak6.setTitle("Disease");
        trak6.setExecutor("Lady Gaga");
        trak6.setDate("04.11.2024");
        List<Trak> tracks = new ArrayList<Trak>();
        tracks.add(trak1);
        tracks.add(trak2);
        tracks.add(trak3);
        tracks.add(trak4);
        tracks.add(trak5);
        tracks.add(trak6);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создаем адаптер и прикрепляем его к RecyclerView
        com.example.lab4.TrackAdapter adapter = new com.example.lab4.TrackAdapter(tracks);
        recyclerView.setAdapter(adapter);
//        // Инициализация DbContext для работы с базой данных
//        dbContext = new DbContext(this);
//        dbContext.OpenDb();
//
//        // Инициализация scheduler
//        scheduler = Executors.newScheduledThreadPool(1);  // Создаем новый поток для выполнения задач
//
//        // Проверяем наличие интернета
//        if (!isInternetAvailable()) {
//            // Показываем уведомление об автономном режиме
//            Toast.makeText(this, "Работа в автономном режиме", Toast.LENGTH_LONG).show();
//        } else {
//            startApiRequest();  // Запускаем периодические запросы только при наличии интернета
//        }
//
//        // Загружаем старые записи
    //    loadOldTracks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Останавливаем периодические задачи при уничтожении активности
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();  // Останавливаем scheduler
        }
        dbContext.CloseDb();
    }

    // Метод для проверки наличия интернета
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Проверяем версию Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Для API 23 и выше
            Network currentNetwork = connectivityManager.getActiveNetwork();
            if (currentNetwork != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(currentNetwork);
                return networkCapabilities != null &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        } else {  // Для API до 23 (до Marshmallow)
            android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isConnected();  // Проверяем, подключен ли к интернету
            }
        }
        return false;
    }

    // Метод для запуска периодического опроса API каждые 20 секунд
    private void startApiRequest() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Выполнение асинхронного запроса
                ApiTask apiTask = new ApiTask(MainActivity.this, dbContext);
                apiTask.execute();
            }
        }, 0, 20, TimeUnit.SECONDS); // Запуск сразу и повторение каждые 20 секунд
    }

    // Метод для загрузки старых записей
    // Метод для загрузки старых записей
    public void loadOldTracks() {
        // Проверяем наличие интернета
        if (isInternetAvailable()) {
            // Уведомляем пользователя, что приложение работает оффлайн
            Toast.makeText(this, "Работа в автономном режиме: невозможно загрузить данные из сети", Toast.LENGTH_LONG).show();
        }

        // Получаем список треков из базы данных
        List<Trak> tracks = dbContext.GetTraks();

        // Проверяем, что список не пустой
        if (tracks != null && !tracks.isEmpty()) {
            // Инициализируем RecyclerView
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Создаем адаптер и прикрепляем его к RecyclerView
            TrackAdapter adapter = new TrackAdapter(tracks);
            recyclerView.setAdapter(adapter);
        } else {
            // Если список пуст или произошла ошибка, показываем уведомление
            Toast.makeText(this, "Нет записей для отображения", Toast.LENGTH_LONG).show();
        }
    }
}
