-if class com.example.animeapp.model.Anime
-keepnames class com.example.animeapp.model.Anime
-if class com.example.animeapp.model.Anime
-keep class com.example.animeapp.model.AnimeJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
