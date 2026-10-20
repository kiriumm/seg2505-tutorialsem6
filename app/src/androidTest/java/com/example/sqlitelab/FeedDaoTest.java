package com.example.sqlitelab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FeedDaoTest {

    private Context context;
    private FeedReaderDbHelper dbHelper;
    private FeedDao dao;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.deleteDatabase(FeedReaderDbHelper.DATABASE_NAME);
        dbHelper = new FeedReaderDbHelper(context);
        dao = new FeedDao(dbHelper);
    }

    @After
    public void tearDown() {
        dbHelper.close();
        context.deleteDatabase(FeedReaderDbHelper.DATABASE_NAME);
    }

    @Test
    public void insert_returnsValidId() {
        long id = dao.insert("Titre", "Sous-titre");
        assertNotEquals(-1L, id);
    }

    @Test
    public void insertThenFindAll_containsRow() {
        long id = dao.insert("Titre", "Sous-titre");
        List<String> rows = dao.findAll();
        assertEquals(1, rows.size());
        assertEquals(id + " | Titre | Sous-titre", rows.get(0));
    }

    @Test
    public void findIdsByTitle_returnsOnlyMatchingRows() {
        long id1 = dao.insert("A", "x");
        dao.insert("B", "y");
        List<Long> ids = dao.findIdsByTitle("A");
        assertEquals(1, ids.size());
        assertEquals(Long.valueOf(id1), ids.get(0));
    }

    @Test
    public void updateTitle_changesTitle() {
        dao.insert("Ancien", "x");
        int updated = dao.updateTitle("Ancien", "Nouveau");
        assertEquals(1, updated);
        assertTrue(dao.findIdsByTitle("Ancien").isEmpty());
        assertEquals(1, dao.findIdsByTitle("Nouveau").size());
    }

    @Test
    public void deleteByTitle_removesRow() {
        dao.insert("A supprimer", "x");
        int deleted = dao.deleteByTitle("A supprimer");
        assertEquals(1, deleted);
        assertTrue(dao.findAll().isEmpty());
    }
}
