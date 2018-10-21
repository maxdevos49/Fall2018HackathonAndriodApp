package com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView;

import java.util.List;

public class RemotePhoto {

    private String album;
    private List<String> tags;
    private String filePath;
    private String origFileName;

    public RemotePhoto(String album, List<String> tags, String filePath, String origFileName) {
        this.album = album;
        this.tags = tags;
        this.filePath = filePath;
        this.origFileName = origFileName;
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

    public String getOrigFileName() {
        return origFileName;
    }
}
