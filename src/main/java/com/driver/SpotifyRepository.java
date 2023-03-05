package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User curUser : users){
            if(curUser.getMobile().equals(mobile)){
                return curUser;
            }
        }
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        for(Artist curArtist : artists){
            if(curArtist.getName().equals(name)){
                return curArtist;
            }
        }
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = createArtist(artistName);
        for(Album curAlbum : albums){
            if(curAlbum.getTitle().equals(title)) {
                return curAlbum;
            }
        }
        Album album = new Album(title);
        albums.add(album);
        List<Album> alb = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            alb = artistAlbumMap.get(artist);
        }
        alb.add(album);
        artistAlbumMap.put(artist,alb);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean albumPresent = false;
        Album album = new Album();
        for(Album album1 : albums){
            if(album1.getTitle().equals(title)){
                album = album1;
                albumPresent = true;
                break;
            }
        }
        if(!albumPresent){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);
        songs.add(song);

        List<Song> listSongs = new ArrayList<>();
        if(albumSongMap.containsKey(album)){
            listSongs = albumSongMap.get(album);
        }
        listSongs.add(song);
        albumSongMap.put(album,listSongs);
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title)){
                return playlist;
            }
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        List<Song> temp = new ArrayList<>();
        for(Song song : songs){
            if(song.getLength() == length){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User user = new User();
        boolean flag = false;
        for(User user1 : users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new Exception("User does not exist");
        }
        List<User> userList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userList = playlistListenerMap.get(playlist);
        }
        userList.add(user);
        playlistListenerMap.put(playlist,userList);
        creatorPlaylistMap.put(user,playlist);

        List<Playlist> userPlayList = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            userPlayList = userPlaylistMap.get(user);
        }
        userPlayList.add(playlist);
        userPlaylistMap.put(user,userPlayList);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title)){
                return playlist;
            }
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        List<Song> temp = new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song)){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User user = new User();
        boolean flag = false;
        for(User user1 : users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new Exception("User does not exist");
        }
        List<User> userList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userList = playlistListenerMap.get(playlist);
        }
        userList.add(user);
        playlistListenerMap.put(playlist,userList);

        creatorPlaylistMap .put(user,playlist);

        List<Playlist> userPlayList = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            userPlayList = userPlaylistMap.get(user);
        }
        userPlayList.add(playlist);
        userPlaylistMap.put(user,userPlayList);

        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag = false;
        Playlist playlist = new Playlist();
        for(Playlist currPlayList : playlists){
            if(currPlayList.getTitle().equals(playlistTitle)){
                playlist = currPlayList;
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new Exception("Playlist does not exist");
        }

        User user = new User();
        boolean flag2 = false;
        for(User user1 : users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
                flag2 = true;
                break;
            }
        }
        if(!flag2){
            throw new Exception("User does not exist");
        }
        List<User> userList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userList = playlistListenerMap.get(playlist);
        }
        if(!userList.contains(user)){
            userList.add(user);
        }
        playlistListenerMap.put(playlist,userList);
        if(creatorPlaylistMap.get(user)!=playlist){
            creatorPlaylistMap.put(user,playlist);
        }
        List<Playlist> userPlayList = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            userPlayList = userPlaylistMap.get(user);
        }
        if(!userPlaylistMap.containsKey(user)){
            userPlayList.add(playlist);
        }
        userPlaylistMap.put(user,userPlayList);
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = new User();
        boolean flag = false;
        for(User user1 : users){
            if(user1.getMobile().equals(mobile)){
                user = user1;
                flag = true;
                break;
            }
        }
        if(!flag){
            throw new Exception("User does not exist");
        }
        Song song = new Song();
        boolean flag2 = false;
        for(Song song1 : songs){
            if(song1.getTitle().equals(songTitle)){
                song = song1;
                flag2 = true;
                break;
            }
        }
        if(!flag2){
            throw new Exception("Song does not exist");
        }
        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users = songLikeMap.get(song);
        }
        if(!users.contains(user)){
            users.add(user);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);

            Album album = new Album();
            for(Album cur : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(cur);
                if(temp.contains(song)){
                    album = cur;
                    break;
                }
            }

            Artist artist = new Artist();
            for(Artist curArtist : artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(curArtist);
                if(temp.contains(album)){
                    artist = curArtist;
                    break;
                }
            }
            artist.setLikes(artist.getLikes()+1);
        }
        return song;
    }

    public String mostPopularArtist() {
        String name ="";
        int maxLikes = Integer.MIN_VALUE;
        for(Artist artist : artists){
            maxLikes = Math.max(maxLikes,artist.getLikes());
        }
        for(Artist artist : artists){
            if(maxLikes == artist.getLikes()){
                name = artist.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name = "";
        int maxLikes = Integer.MIN_VALUE;
        for(Song song : songs){
            maxLikes = Math.max(maxLikes,song.getLikes());
        }
        for(Song song : songs){
            if(maxLikes == song.getLikes()){
                name = song.getTitle();
            }
        }
        return name;
    }
}
