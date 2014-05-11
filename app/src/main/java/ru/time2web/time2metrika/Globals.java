package ru.time2web.time2metrika;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import ru.metrika4j.ApiFactory;
import ru.metrika4j.MetrikaApi;
import ru.metrika4j.error.AuthException;
import ru.metrika4j.json.jackson.JacksonMapper;
import ru.metrika4j.json.org.OrgJsonMapper;


/** @author Artur Suilin */
public class Globals {
    public static final String PREF_FILE = "preferences";
    public static final String PREF_TOKEN = "token";
    private static MetrikaApi api;

    /**
     * Возвращает глобальный instance MetrikaApi, при необходимости создавая его. Eсли instance не существует и его невозможно создать
     * (нет сохранённого в настройках OAuth токена), бросает {@link AuthException}
     */
    public static synchronized MetrikaApi getApi(Context context) {
        if (api == null) {
            String token = getOAuthToken(context);
            if (token == null) {
                throw new AuthException();
            } else {
                api = ApiFactory.createMetrikaAPI(token, new OrgJsonMapper());
            }
        }
        return api;
    }

    private static String getOAuthToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Globals.PREF_FILE, Context.MODE_PRIVATE);
        return prefs.getString(Globals.PREF_TOKEN, null);
    }

    /**
     * Проверяет, существует ли сохранённый в настройках OAuth токен. Если не существует, иницирует процедуру получения OAuth токена.
     *
     * @param activity Активность, от имени которой будет происходить переход на получение OAuth токена
     * @return true, если OAuth токен существует, иначе false.
     */
    public static boolean checkOAuthTokenExists(final Activity activity) {
        if (getOAuthToken(activity) == null) {
            requestAuth(activity);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Иницирует процедуру получения OAuth токена. Когда токен будет получен, запустится {@link AuthTokenActivity}
     *
     * @param activity Активность, от имени которой будет происходить получение OAuth токена
     */
    public static void requestAuth(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.authDialogTitle)
                .setMessage(R.string.authFirstTime)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "https://oauth.yandex.ru/authorize?response_type=token&client_id=54c7b6eb472b413f8428bce48eb819d5"));
                        activity.startActivity(intent);
                        //    finish();
                    }
                }).show();
    }


    public static void resetAPI() {
        api = null;
    }
}