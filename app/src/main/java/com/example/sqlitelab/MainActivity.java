package com.example.sqlitelab;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SQLiteLab";
    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new FeedReaderDbHelper(this);
        FeedDao dao = new FeedDao(dbHelper);

        long id1 = dao.insert("Titre 1", "Sous-titre 1");
        long id2 = dao.insert("Titre 2", "Sous-titre 2");
        Log.d(TAG, "INSERT -> id1 = " + id1 + ", id2 = " + id2);
        logAll(dao, "Après INSERT");

        Log.d(TAG, "SELECT WHERE title = 'Titre 1' -> ids = " + dao.findIdsByTitle("Titre 1"));

        int updated = dao.updateTitle("Titre 1", "Titre 1 modifié");
        Log.d(TAG, "UPDATE -> " + updated + " ligne(s) modifiée(s)");
        logAll(dao, "Après UPDATE");

        int deleted = dao.deleteByTitle("Titre 2");
        Log.d(TAG, "DELETE -> " + deleted + " ligne(s) supprimée(s)");
        logAll(dao, "Après DELETE");
    }

    private void logAll(FeedDao dao, String label) {
        Log.d(TAG, "--- " + label + " ---");
        for (String row : dao.findAll()) {
            Log.d(TAG, row);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}