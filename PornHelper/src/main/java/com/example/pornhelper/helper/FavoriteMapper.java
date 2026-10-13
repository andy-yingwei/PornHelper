package com.example.pornhelper.helper;

import com.example.pornhelper.actor.ActorLite;
import com.example.pornhelper.video.VideoLite;
import com.example.pornhelper.studio.Studio;
import com.example.pornhelper.table.FavoriteActor;
import com.example.pornhelper.table.FavoriteVideo;
import com.example.pornhelper.table.FavoriteStudio;

import java.util.ArrayList;
import java.util.List;

public final class FavoriteMapper {

    // 私有构造，防止实例化
    private FavoriteMapper() {}

    /*-------------- actor -------------- */
    public static ActorLite toActorLite(FavoriteActor f) {
        if (f == null) return null;
        return new ActorLite(
                f.getActorId(),
                f.getName(),
                f.getAvatar(),
                f.getVideoCount(),
                f.getViews(),
                f.getRating(),
                f.getRank(),
                true,
                f.getUrl()
        );
    }

    public static List<ActorLite> toActorLiteList(List<FavoriteActor> src) {
        List<ActorLite> out = new ArrayList<>();
        if (src == null) return out;
        for (FavoriteActor f : src) {
            ActorLite l = toActorLite(f);
            if (l != null) out.add(l);
        }
        return out;
    }

    /*-------------- video -------------- */
    public static VideoLite toVideoLite(FavoriteVideo v) {
        if (v == null) return null;
        return new VideoLite(
                v.getVideoId(),
                v.getTitle(),
                v.getCover(),
                v.getDuration(),
                v.getViews(),
                "",
                v.getRating(),
                v.getSource(),
                "",
                true,
                v.getUrl()
        );
    }

    public static List<VideoLite> toVideoLiteList(List<FavoriteVideo> src) {
        List<VideoLite> out = new ArrayList<>();
        if (src == null) return out;
        for (FavoriteVideo v : src) {
            VideoLite l = toVideoLite(v);
            if (l != null) out.add(l);
        }
        return out;
    }

    /*-------------- studio -------------- */
    public static Studio toFilmStudio(FavoriteStudio s) {
        if (s == null) return null;
        return new Studio(
                s.getStudioId(),
                s.getName(),
                s.getLogo(),
                s.getVideoCount(),
                s.getRating(),
                s.getRank(),
                true,
                s.getUrl()
        );
    }

    public static List<Studio> toFilmStudioList(List<FavoriteStudio> src) {
        List<Studio> out = new ArrayList<>();
        if (src == null) return out;
        for (FavoriteStudio s : src) {
            Studio l = toFilmStudio(s);
            if (l != null) out.add(l);
        }
        return out;
    }
}