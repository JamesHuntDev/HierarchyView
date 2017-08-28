package me.jameshunt.hierarchyexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.jameshunt.hierarchyexample.old.Data;
import me.jameshunt.hierarchyview.DepthLevelFormat;
import me.jameshunt.hierarchyview.HierarchyData;
import me.jameshunt.hierarchyview.HierarchyDataHelper;
import me.jameshunt.hierarchyview.HierarchyListener;
import me.jameshunt.hierarchyview.HierarchyView;
import me.jameshunt.hierarchyview.HierarchyViewIncompatibleTypeException;


public class MainActivity extends AppCompatActivity implements HierarchyListener.Data {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /*Data data = new Data();

        HierarchyData hierarchyData = new HierarchyData(data);

        hierarchyData.addFormatter(new DepthLevelFormat(24,R.color.colorPrimary)); //depth level 0
        hierarchyData.addFormatter(new DepthLevelFormat(20,R.color.colorAccent));  //depth level 1


        HierarchyView hierarchyView = new HierarchyView(this);
        hierarchyView.setHierarchyData(hierarchyData);

        setContentView(hierarchyView);*/


        String json = getJson();
        parseJSON(json);

    }

    @Override
    public void hierarchyDataSelected(String data) {
        Log.d("clicked", data);
    }


    private String getJson() {

        try {

            InputStream is = getResources().openRawResource(R.raw.categories);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseJSON(String json) {

        Type listType = new TypeToken<ArrayList<Category>>() {}.getType();
        List<Category> categories = new Gson().fromJson(json, listType);


        HierarchyData categoryData = null;
        try {
            categoryData = new HierarchyData(categories);


            categoryData.addFormatter(new DepthLevelFormat(24, R.color.colorPrimary)); //depth level 0
            categoryData.addFormatter(new DepthLevelFormat(22, R.color.blueGray));  //depth level 1
            categoryData.addFormatter(new DepthLevelFormat(20, R.color.green)); //depth level 0
            categoryData.addFormatter(new DepthLevelFormat(18, R.color.colorAccent));  //depth level 1
            categoryData.addFormatter(new DepthLevelFormat(16, R.color.red));  //depth level 1

            HierarchyView hierarchyView = new HierarchyView(this);
            hierarchyView.setHierarchyData(categoryData);

            setContentView(hierarchyView);


        } catch (HierarchyViewIncompatibleTypeException e) {
            e.printStackTrace();
        }
    }
}
