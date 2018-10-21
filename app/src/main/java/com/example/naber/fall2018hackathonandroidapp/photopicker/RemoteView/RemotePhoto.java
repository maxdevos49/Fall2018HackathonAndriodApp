package com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView;

import android.util.Log;

import java.util.List;

public class RemotePhoto {

    private String album;
    private List<String> tags;
    private String filePath;
    private String origFileName;
    private String smallThumbnailName;
    private String largeThumbnailName;


    public RemotePhoto(String album, List<String> tags, String filePath, String origFileName) {
        this.album = album;
        this.tags = tags;
        this.filePath = filePath;
        this.origFileName = origFileName;

        String[] namePaths = filePath.split("\\\\|\\/");
        smallThumbnailName = "/thumbnails/" + namePaths[namePaths.length - 1].substring(0, namePaths[namePaths.length - 1].lastIndexOf('.')) + "_small" + namePaths[namePaths.length - 1].substring(namePaths[namePaths.length - 1].lastIndexOf('.'));
        largeThumbnailName = "/thumbnails/" + namePaths[namePaths.length - 1].substring(0, namePaths[namePaths.length - 1].lastIndexOf('.')) + "_big" + namePaths[namePaths.length - 1].substring(namePaths[namePaths.length - 1].lastIndexOf('.'));
        Log.i("RemotePhoto", "Created thumbnails paths of from: " + filePath + " small: " + smallThumbnailName + " large: " + largeThumbnailName);

    }

    public String getAlbum() {
        return album;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getLargeThumbnailName() {
        return largeThumbnailName;
    }

    public String getSmallThumbnailName() {
        return smallThumbnailName;
    }

    public String getOrigFileName() {
        return origFileName;
    }
}
