package me.jameshunt.hierarchyexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.jameshunt.hierarchyview.DepthLevelFormat;
import me.jameshunt.hierarchyview.HierarchyData;
import me.jameshunt.hierarchyview.HierarchyDataHelper;
import me.jameshunt.hierarchyview.HierarchyListener;
import me.jameshunt.hierarchyview.HierarchyView;

/**
 * Created by James on 8/28/2017.
 */

public class NestedActivity extends AppCompatActivity implements HierarchyListener.Data {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String json = getJson();
        parseJSON(json);
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

        Type listType = new TypeToken<ArrayList<Category>>() {
        }.getType();
        List<Category> categories = new Gson().fromJson(json, listType);


        HierarchyData categoryData = new HierarchyData(categories);


        categoryData.addFormatter(new DepthLevelFormat(24, R.color.colorPrimary)); //depth level 0
        categoryData.addFormatter(new DepthLevelFormat(22, R.color.blueGray));  //depth level 1
        categoryData.addFormatter(new DepthLevelFormat(20, R.color.green)); //depth level 2
        categoryData.addFormatter(new DepthLevelFormat(18, R.color.colorAccent));  //depth level 3
        categoryData.addFormatter(new DepthLevelFormat(16, R.color.red));  //depth level 4

        HierarchyView hierarchyView = new HierarchyView(this); //needs a context
        hierarchyView.setHierarchyData(categoryData, this); //"this" is the listener

        setContentView(hierarchyView);


    }

    @Override
    public void hierarchyDataSelected(HierarchyDataHelper.Data data) {

    }
}
