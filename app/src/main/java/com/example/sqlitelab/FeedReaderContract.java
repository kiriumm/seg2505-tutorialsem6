package com.example.sqlitelab;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    // Constructeur privé : on ne crée jamais d'objet de cette classe
    private FeedReaderContract() {}

    // Une classe interne = une table
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
