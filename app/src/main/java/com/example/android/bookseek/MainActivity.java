package com.example.android.bookseek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // BookAdapter.java custom ArrayAdapter
    private BookAdapter mAdapter;


    /**
     * Inflates App bar (e.g. SearchView action view).
     * @param menu toolbar_menu.xml
     * @return SearchView
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate toolbar_menu.xml
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        // find SearchView in the menu
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // set a custom search hint on it
        searchView.setQueryHint(getString(R.string.search_book));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Book> testList = new ArrayList<>();
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai és még egyszer a kőszívű ember fiai", "Jókai Mór ráadásul nagyon sokan írták ezt a könyvet", "Édesvíz édesvíz édes édes", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni, fel lehet írni, fel lehet írni, fel lehet írni, fel lehet írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));
        testList.add(new Book("A helyiség kalapácsa", "Petőfi Sándor", "Móra", "1989",
                "szahar könyv"));
        testList.add(new Book("A kőszívű ember fiai", "Jókai Mór", "Édesvíz", "2005",
                "egy másik kötelező olvasmány, melyet óvodásoknak is fel akartak már írni"));


        // find ListView in the layout
        ListView listView = (ListView) findViewById(R.id.list);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, testList);
        // set adapter on the ListView to be populated with Book.java objects
        listView.setAdapter(mAdapter);

    }
}
