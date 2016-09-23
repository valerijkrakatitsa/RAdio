package com.vkrakatitsa.radio.Model;

public class VKSong {

    private String m_strSongURL;
    private String m_strSongTitle;
    private String m_strSongArtist;

    public VKSong(String title, String url, String artist){
        m_strSongURL=url;
        m_strSongTitle=title;
        m_strSongArtist=artist;
    }

    public void setSongURL(String strSongURL){
        m_strSongURL=strSongURL;
    }

    public void setSongTitle(String songTitle){
        m_strSongTitle=songTitle;
    }

    public void setSongArtist(String songArtist){
        m_strSongArtist=songArtist;
    }

    public String getSongURL(){
        return m_strSongURL;
    }

    public String getSongTitle(){
        return m_strSongTitle;
    }

    public String getSongArtist(){
        return m_strSongArtist;
    }
}
