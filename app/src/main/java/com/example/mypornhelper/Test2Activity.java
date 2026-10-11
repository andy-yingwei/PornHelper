package com.example.mypornhelper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pornhelper.actor.*;
import com.example.pornhelper.helper.*;
import com.example.pornhelper.studio.*;
import com.example.pornhelper.category.*;
import com.example.pornhelper.video.VideoLite;
import com.example.pornhelper.video.*;


import java.util.ArrayList;
import java.util.List;

public class Test2Activity extends AppCompatActivity{

    private static final String COVER = "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("大漂亮1",COVER,"视频：2893部","排名：89",""));
        categoryList.add(new Category("大漂亮2",COVER,"视频：2893部","排名：89",""));
        categoryList.add(new Category("大漂亮3",COVER,"视频：2893部","排名：89",""));
        categoryList.add(new Category("大漂亮4",COVER,"视频：2893部","排名：89",""));
        categoryList.add(new Category("大漂亮5",COVER,"视频：2893部","排名：89",""));
        categoryList.add(new Category("大漂亮6",COVER,"视频：2893部","排名：89",""));
        categoryList.add(new Category("大漂亮7",COVER,"视频：2893部","排名：89",""));
        CategoryConfig config = new CategoryConfig.Builder()
                .backgroundColor(0xFFFFFFFF)
                .backgroundRadius(8)
                .coverHeight(220)
                .coverRoundRadius(20)
                .titleTextSize(16)
                .titleTextColor(0xFF000000)
                .infoTextSize(12)
                .infoTextColor(0xFF666666)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .build();
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList,config);
        recyclerView1.setAdapter(categoryAdapter);


        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
        List<VideoLite> videoLiteList = new ArrayList<>();
        videoLiteList.add(new VideoLite("","今天的天气真是好，要是去公园的话，一定能遇上我喜欢的大姐姐1",null,"时长：16分钟","观看：45次","","好评：78%","","",true,""));
        videoLiteList.add(new VideoLite("","今天的天气真是好，要是去公园的话，一定能遇上我喜欢的大姐姐2",null,"时长：16分钟","观看：45次","","好评：78%","","",false,""));
        videoLiteList.add(new VideoLite("","今天的天气真是好，要是去公园的话，一定能遇上我喜欢的大姐姐3",null,"时长：16分钟","观看：45次","","好评：78%","","",false,""));
        videoLiteList.add(new VideoLite("","今天的天气真是好，要是去公园的话，一定能遇上我喜欢的大姐姐4",COVER,"时长：16分钟","观看：45次","","好评：78%","","",false,""));
        videoLiteList.add(new VideoLite("","今天的天气真是好，要是去公园的话，一定能遇上我喜欢的大姐姐5",COVER,"时长：16分钟","观看：45次","","好评：78%","","",false,""));
        videoLiteList.add(new VideoLite("","今天的天气真是好，要是去公园的话，一定能遇上我喜欢的大姐姐6",COVER,"时长：16分钟","观看：45次","","好评：78%","","",false,""));
        videoLiteList.add(new VideoLite("","今天的天气真是好，要是去公园的话，一定能遇上我喜欢的大姐姐7",COVER,"时长：16分钟","观看：45次","","好评：78%","","",false,""));
        VideoLiteAdapter videoLiteAdapter = new VideoLiteAdapter(videoLiteList);
        videoLiteAdapter.enableLog("Test");
        recyclerView2.setAdapter(videoLiteAdapter);





        RecyclerView recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        List<Studio> studioList = new ArrayList<>();
        studioList.add(new Studio("","魔域1",COVER,"视频：1342部","","",true,""));
        studioList.add(new Studio("","魔域2",COVER,"视频：1342部","好评：90%","排名：2",true,""));
        studioList.add(new Studio("","魔域3",COVER,"视频：1342部","好评：90%","排名：2",true,""));
        studioList.add(new Studio("","魔域4",COVER,"视频：1342部","好评：90%","排名：2",true,""));
        studioList.add(new Studio("","魔域5",COVER,"视频：1342部","好评：90%","排名：2",true,""));
        StudioConfig studioConfig = new StudioConfig.Builder()
                .backgroundColor(Colors.of("#1e1e1e"))
                .infoTextColor(Colors.of("#9e9e9e"))
                .nameTextColor(Colors.of("#ededed"))
                .favoriteIcon(R.drawable.ic_heart_fill)
                .favoriteIconSize(30)
                .build();
        StudioAdapter studioAdapter = new StudioAdapter(studioList, studioConfig);
        studioAdapter.enableLog("Test");
        recyclerView3.setAdapter(studioAdapter);




        RecyclerView recyclerView4 = findViewById(R.id.recyclerView4);
        recyclerView4.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        List<ActorLite> acotrLiteList = new ArrayList<>();
        acotrLiteList.add(new ActorLite("","波多野结衣1",COVER,"视频:76部","","","排名3",true,""));
        acotrLiteList.add(new ActorLite("","波多野结衣2",COVER,"视频:76部","","","排名3",false,""));
        acotrLiteList.add(new ActorLite("","波多野结衣3",COVER,"视频:76部","","","排名3",false,""));
        acotrLiteList.add(new ActorLite("","波多野结衣4",COVER,"视频:76部","","","排名3",true,""));
        acotrLiteList.add(new ActorLite("","波多野结衣5",COVER,"视频:76部","","","排名3",false,""));
        acotrLiteList.add(new ActorLite("","波多野结衣6",COVER,"视频:76部","","","排名3",true,""));
        acotrLiteList.add(new ActorLite("","波多野结衣7",null,"视频:76部","","","排名3",true,""));
        ActorLiteConfig actorLiteConfig = new ActorLiteConfig.Builder()
                .backgroundColor(Colors.of("#1e1e1e"))
                .infoTextColor(Colors.of("#9e9e9e"))
                .nameTextColor(Colors.of("#ededed"))
                .favoriteIcon(R.drawable.ic_heart_fill)
                .build();
        ActorLiteAdapter actorLiteAdapter = new ActorLiteAdapter(acotrLiteList,actorLiteConfig);
        actorLiteAdapter.enableLog("Test");
        recyclerView4.setAdapter(actorLiteAdapter);
    }
}