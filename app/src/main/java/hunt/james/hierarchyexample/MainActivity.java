package hunt.james.hierarchyexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import hunt.james.hierarchyview.DepthLevelFormat;
import hunt.james.hierarchyview.HierarchyData;
import hunt.james.hierarchyview.HierarchyListener;
import hunt.james.hierarchyview.HierarchyView;


public class MainActivity extends AppCompatActivity implements HierarchyListener.Data{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Data data = new Data();

        HierarchyData hierarchyData = new HierarchyData(data);

        hierarchyData.addFormatter(new DepthLevelFormat(24,R.color.colorPrimary)); //depth level 0
        hierarchyData.addFormatter(new DepthLevelFormat(20,R.color.colorAccent));  //depth level 1


        HierarchyView hierarchyView = new HierarchyView(this);
        hierarchyView.setHierarchyData(hierarchyData);

        setContentView(hierarchyView);

    }

    @Override
    public void hierarchyDataSelected(String data) {
        Log.d("clicked",data);
    }
}
