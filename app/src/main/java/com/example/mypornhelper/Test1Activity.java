package com.example.mypornhelper;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pornhelper.actor.ActorLite;
import com.example.pornhelper.actor.ActorProfile;
import com.example.pornhelper.actor.ActorProfileConfig;
import com.example.pornhelper.actor.ActorProfileView;
import com.example.pornhelper.cast.*;
import com.example.pornhelper.dao.FavoriteActorDAO;
import com.example.pornhelper.helper.*;
import com.example.pornhelper.magnet.Magnet;
import com.example.pornhelper.playSource.PlaySource;
import com.example.pornhelper.tag.Tag;
import com.example.pornhelper.video.VideoLite;
import com.example.pornhelper.video.VideoProfile;
import com.example.pornhelper.video.VideoProfileConfig;
import com.example.pornhelper.video.VideoProfileView;

import java.util.ArrayList;
import java.util.List;

public class Test1Activity extends AppCompatActivity{

    private static final String COVER = "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);


        ActorProfileView actorProfileView = findViewById(R.id.actorProfileView);
        actorProfileView.enableLog("Test");
        FavoriteActorDAO.enableLog("Test");
        actorProfileView.setConfig(new ActorProfileConfig.Builder()
                .nameTextSize(20)
                .avatarRadius(20)
                .arrowColor(Colors.of(this, R.color.red))
                .favBackground(Colors.of(this, R.color.white))
                .favTextColor(Colors.of(this, R.color.black))
                .build()
        );
        actorProfileView.setData(new ActorProfile(
                new ActorLite("", "波多野结衣", COVER, "198部", "23190次", "87%", "", true, ""),
                null,
                "女",
                "28岁",
                "167cm",
                "45kg",
                "38B",
                "109cm",
                "1995-12-24",
                "日本神户",
                "日本",
                "",
                ""
        ));



        VideoProfileView videoProfileView = findViewById(R.id.videoProfileView);
        videoProfileView.enableLog("Test");
        videoProfileView.setConfig(new VideoProfileConfig.Builder()
                .titleTextSize(20)
                .coverRadius(20)
                .arrowColor(Colors.of(this, R.color.red))
                .build()
        );
        List<Cast> castList = new ArrayList<>();
        castList.add(new Cast("","波多野结衣",null,""));
        castList.add(new Cast("","空天川",null,""));

        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("", "美丽", "",""));
        tagList.add(new Tag("", "丰满", "",""));
        tagList.add(new Tag("", "长腿", "",""));
        tagList.add(new Tag("", "汽车痴汉", "",""));

        List<PlaySource> playSourceList = new ArrayList<>();
        playSourceList.add(new PlaySource("420p","",false,""));
        playSourceList.add(new PlaySource("720p","",false,""));
        playSourceList.add(new PlaySource("1020p","",false,""));
        playSourceList.add(new PlaySource("10800p","",true,""));

        List<Magnet> magnetList = new ArrayList<>();
        magnetList.add(new Magnet("我的姐姐真好", "大小：1.5G", "下载：20次", ""));
        magnetList.add(new Magnet("我的姐姐真好", "大小：4.5G", "下载：28次", ""));


        videoProfileView.setCastSpan(2);
        // 外部完全控制 CastConfig
        CastConfig castConfig = new CastConfig.Builder()
                .avatarSize(48)                    // 大头像
                .avatarRadius(24)                  // 圆形
                .nameTextSize(14)
                .nameTextColor(0xFFFFFFFF)
                .strokeColor(0xFFFF4081)           // 粉色描边
                .strokeWidth(2)
                .background(0xFF1A1A1A)
                .backgroundRadius(8)
                .skipMemoryCache(false)
                .build();

        CastAdapter castAdapter = new CastAdapter(castConfig);

        // 注入
        videoProfileView.setCastAdapter(castAdapter);


        videoProfileView.setData(new VideoProfile(
                new VideoLite("","今天的天气真是好，我在公园里遇上了邻居大姐姐，她非要带我去她家",COVER,"时长：45分56秒","观看：567次","收藏：89次","好评率：78%","","",true,""),
                "sdfafefweewedds", "", null,null,null,castList,tagList,magnetList,playSourceList,""));


    }
}
