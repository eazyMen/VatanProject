package com.kaspsoft.radinvatan.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class DetailList {

    @SerializedName("img")
    private String img;
    @SerializedName("content")
    private String content;
    @SerializedName("title")
    private String title;

    public List<AudioFiles> getAudio_files() {
        return audio_files;
    }

    public void setAudio_files(List<AudioFiles> audio_files) {
        this.audio_files = audio_files;
    }

    private List<AudioFiles> audio_files;

    /*public Map<String, JsonObject> getAudio_files() {
        return audio_files;
    }

    public void setAudio_files(Map<String, JsonObject> audio_files) {
        this.audio_files = audio_files;
    }*/

   /* @SerializedName("audio_files")
    @Expose
    private Map<String, JsonObject> audio_files;*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }





}
