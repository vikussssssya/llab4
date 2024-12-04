package api;

import android.content.Context;
import android.util.Log;

import com.example.lab4.MainActivity;

import org.json.JSONObject;

import java.io.IOException;

import db.Trak;
import db.DbContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Response;

public class ApiTask {

    private Context context;
    private DbContext dbContext;
    private OkHttpClient client;

    public ApiTask(Context context, DbContext dbContext) {
        this.context = context;
        this.dbContext = dbContext;
        this.client = new OkHttpClient();  // Инициализируем OkHttpClient
    }

    // Выполнение запроса на получение данных сервера в фоновом потоке
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = fetchTrackData();
                    handleResponse(result);  // Обрабатываем ответ от сервера
                } catch (IOException e) {
                    e.printStackTrace();  // Логируем ошибки
                }
            }
        }).start();
    }

    // Метод для получения данных с API
    private String fetchTrackData() throws IOException {
        String login = "4707login";
        String password = "4707pass";

        // Формируем тело запроса
        RequestBody body = new FormBody.Builder()
                .add("login", login)
                .add("password", password)
                .build();

        // Строим запрос
        Request request = new Request.Builder()
                .url("http://media.ifmo.ru/api_get_current_song.php")
                .post(body)
                .build();

        // Отправляем запрос и получаем ответ
        Response response = client.newCall(request).execute();
        return response.body() != null ? response.body().string() : "";  // Возвращаем ответ сервера
    }

    // Расшифровка данных ответа сервера
    private void handleResponse(String result) {
        try {
            JSONObject jsonResponse = new JSONObject(result);
            String status = jsonResponse.getString("result");

            // Обрабатываем успешный ответ
            if ("success".equals(status)) {
                String trackInfo = jsonResponse.getString("info");
                String[] parts = trackInfo.split(" – ");  // Разделяем информацию о треке (исполнитель и название)

                // Создаем новый объект трека
                Trak trak = new Trak();
                trak.setExecutor(parts[0]);
                trak.setTitle(parts[1]);

                Trak lastTrack = dbContext.getLastTrack();  // Получаем последний трек из базы данных

                // Проверяем, отличается ли текущий трек от последнего в БД
                if (lastTrack == null || !lastTrack.getTitle().equals(trak.getTitle())) {
                    trak.setDate("2024-11-18");  // Устанавливаем дату трека
                    dbContext.Add(trak);  // Сохраняем новый трек в базе данных

                    // Обновляем RecyclerView в главной активности
                    if (context instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.loadOldTracks();  // Обновляем отображаемые данные в UI
                            }
                        });
                    }
                }
            } else {
                // Если произошла ошибка при получении данных, выводим информацию в лог
                Log.e("API", "Ошибка при получении данных: " + jsonResponse.getString("info"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
